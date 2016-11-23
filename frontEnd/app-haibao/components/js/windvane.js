;(function(win, lib) {
    var Promise = win.Promise;
    var doc = win.document;
    var ua = win.navigator.userAgent;
    //PC浏览器
    var isWin = (/Windows\sPhone\s(?:OS\s)?[\d\.]+/i).test(ua) || (/Windows\sNT\s[\d\.]+/i).test(ua);
    var isWinWV = isWin && win['WindVane_Win_Private'] && win['WindVane_Win_Private'].call;

    var isIOS = (/iPhone|iPad|iPod/i).test(ua);
    var isAndroid = (/Android/i).test(ua);

    //windvane版本信息
    var wvVersion = ua.match(/WindVane[\/\s](\d+[._]\d+[._]\d+)/);

    var hasOwnProperty = Object.prototype.hasOwnProperty;
    // {}
    var WindVane = lib.windvane = win.WindVane || (win.WindVane = {});

    var WindVane_Native = win.WindVane_Native;

    var inc = 1,
        iframePool = [],
        iframeLimit = 3;

    var LOCAL_PROTOCOL = 'hybrid';

    var WV_PROTOCOL = 'wv_hybrid';
    var IFRAME_PREFIX = 'iframe_';
    var SUCCESS_PREFIX = 'suc_';
    var FAILURE_PREFIX = 'err_';
    var DEFERRED_PREFIX = 'defer_';
    var PARAM_PREFIX = 'param_';
    var CHUNK_PREFIX = 'chunk_';

    var CALL_GC_TIME = 60 * 1000 * 10;
    var CHUNK_GC_TIME = 60 * 1000 * 10;
    var PARAM_GC_TIME = 60 * 1000;

    //比较版本的
    function compareVersion(v1, v2) {
        v1 = v1.toString().split('.');
        v2 = v2.toString().split('.');

        for(var i = 0; i < v1.length || i < v2.length; i++) {
            var n1 = parseInt(v1[i],10),  n2 = parseInt(v2[i],10);

            if(window.isNaN(n1)) {
                n1 = 0;
            }
            if(window.isNaN(n2)) {
                n2 = 0;
            }
            if( n1 < n2 ) {
                return -1;
            }
            else if( n1 > n2) {
                return 1;
            }
        }
        return 0;
    }

    if (wvVersion) {
        wvVersion = (wvVersion[1] || '0.0.0').replace(/\_/g, '.');
    } else {
        wvVersion = '0.0.0';
    }

    /**
     * @namespace  lib
     */
    
    /**
     * @namespace windvane
     * @memberOf lib
     */

    var WV_Core = {

        isAvailable: compareVersion(wvVersion, '0') === 1,

        /**
         * @method  call
         * @memberOf lib.windvane
         * @param  {String} obj       要调用的客户端类名
         * @param  {String} method    要调用的客户端方法名
         * @param  {Object} params    要传递给客户端的参数
         * @param  {Function} [success] 执行成功后的回调
         * @param  {Function} [failure] 执行失败后的回调
         * @param  {Number} [timeout]   执行超时，超时后自动以 {ret:['HY_TIMEOUT']}
         * @return {Promise}          如果当前运行环境支持Promise，则返回一个Promise实例。
         */
        call: function(obj, method, params, success, failure, timeout) {
            var sid;
            var deferred;
            //最后一个参数是否数字,超时设定
            if (typeof arguments[arguments.length - 1] === 'number') {
                timeout = arguments[arguments.length - 1];
            }
            //成功和失败的回调函数
            if (typeof success !== 'function') {
                success = null;
            }

            if (typeof failure !== 'function') {
                failure = null;
            }

            //支持promise直接转成promise对象
            if (Promise) {
                deferred = {};
                deferred.promise = new Promise(function(resolve, reject) {
                    deferred.resolve = resolve;
                    deferred.reject = reject;
                });
            }

            //超时设定
            if (timeout > 0) {
                //有超时设置,那么就直接用超时的timeout作为键值
                sid = setTimeout(function() {
                    WV_Core.onFailure(sid, {ret:'HY_TIMEOUT'});
                }, timeout);
            } else {

                sid = WV_Private.getSid();
            }

            //sid用来注册的键值
            WV_Private.registerCall(sid, success, failure, deferred);

            //回收调垃圾, 这里有三个垃圾  必回收 callback, IOS回收参数, 安卓回收chunk?
            WV_Private.registerGC(sid, timeout);

            if (!WV_Core.isAvailable) {
                WV_Core.onFailure(sid, {ret:'HY_NOT_IN_WINDVANE'});
            }
            else {
                //调用函数
                WV_Private.callMethod(obj, method, params, sid);
            }

            //返回一个promise对象
            if (deferred) {
                //返回Promise对象
                return deferred.promise;
            }
        },
        /**
         * 用来触发事件, native调用js绑定好的事件
         * @param eventname
         * @param eventdata
         * @param sid
         */
        fireEvent: function(eventname, eventdata, sid) {
            // 当native需要通知js的时候（通信），用触发事件的方式进行
            var ev = doc.createEvent('HTMLEvents');
            //事件名, 不冒泡, 可以取消
            ev.initEvent(eventname, false, true);
            //都赋值给了event.param对象
            ev.param = WV_Private.parseData(eventdata || WV_Private.getData(sid));
            //分发这个事件到浏览器
            doc.dispatchEvent(ev);
        },

        getParam: function(sid) {
            return WV_Private.getParam(sid);
        },

        setData: function(sid, chunk) {
            WV_Private.setData(sid, chunk);
        },

        onSuccess: function(sid, data) {
            // native代码处理成功后，调用该方法来通知js
            WV_Private.onComplete(sid, data, 'success');
        },

        onFailure: function(sid, data) {
            // native代码处理失败后，调用该方法来通知js
            WV_Private.onComplete(sid, data, 'failure');
        }
    };

    var WV_Private = {
        params: {},
        chunks: {},
        calls: {},

        // sid参数有什么用
        getSid: function() {
            return Math.floor(Math.random() * (1 << 50)) + '' + inc++;
        },
        /**
         * obj解析成字符串, web调用native时候
         * @param obj
         * @returns {*|string}
         */
        buildParam: function(obj) {
            if (obj && typeof obj === 'object') {
                return JSON.stringify(obj);
            } else {
                return obj || '';
            }
        },

        getParam: function(sid) {
            // 因为ios下iframe协议，对于url长度有限制，所以增加一个参数的map。
            return this.params[PARAM_PREFIX + sid] || '';
        },

        /**
         * IOS下设置参数
         * @param sid
         * @param params
         */
        setParam: function(sid, params) {
            this.params[PARAM_PREFIX + sid] = params;
        },
        /**
         * 返回Native解析后的数据
         * @param str
         * @returns {*}
         */
        parseData: function(str) {
            var rst;
            if (str && typeof str === 'string') {
                try {
                    //解析成JSON对象
                    rst = JSON.parse(str);
                } catch(e) {
                    //表示 Native 传递给 H5 的数据出现格式错误
                    rst = {ret:['WV_ERR::PARAM_PARSE_ERROR']};
                }
            } else {
                //直接使用
                rst = str || {};
            }

            return rst;
        },

        setData: function() {
            // android下，回传函数会超长，通过分段set的方式来传递
            this.chunks[CHUNK_PREFIX + sid] = this.chunks[CHUNK_PREFIX + sid] || [];
            this.chunks[CHUNK_PREFIX + sid].push(chunk);
        },
        //获取chunk中保存的数据?
        getData: function(sid) {
            if (this.chunks[CHUNK_PREFIX + sid]) {
                return this.chunks[CHUNK_PREFIX + sid].join('');
            } else {
                return '';
            }
        },
        //注册函数事件, 注册的内容用于Native->web时候使用
        registerCall: function(sid, success, failure, deferred) {
            if (success) {
                this.calls[SUCCESS_PREFIX + sid] = success;
            }

            if (failure) {
                this.calls[FAILURE_PREFIX + sid] = failure;
            }

            if (deferred) {
                //异步回调
                this.calls[DEFERRED_PREFIX + sid] = deferred;
            }
        },

        /**
         * 返回安装好的各个回调函数对象
         * @param sid
         * @returns {{}}
         */
        unregisterCall: function(sid) {
            var sucId = SUCCESS_PREFIX + sid;
            var failId = FAILURE_PREFIX + sid;
            var defId = DEFERRED_PREFIX + sid;

            var call = {};

            if (this.calls[sucId]) {
                call.success = this.calls[sucId];
                delete this.calls[sucId];
            }
            if (this.calls[failId]) {
                call.failure = this.calls[failId];
                delete this.calls[failId];
            }
            if (this.calls[defId]) {
                call.deferred = this.calls[defId];
                delete this.calls[defId];
            }

            return call;
        },

        //利用iframe完成
        useIframe: function(sid, url) {
            var iframeid = IFRAME_PREFIX + sid;
            var iframe = iframePool.pop();

            //创建一个iframe共享池
            if (!iframe) {
                iframe = doc.createElement('iframe');
                iframe.setAttribute('frameborder', '0');
                iframe.style.cssText = 'width:0;height:0;border:0;display:none;';
            }

            //利用iframe设置src唤起
            iframe.setAttribute('id', iframeid);
            iframe.setAttribute('src', url);

            if (!iframe.parentNode) {
                //说明iframe没有在页面中
                setTimeout(function() {
                    doc.body.appendChild(iframe);
                },5);
            }
        },

        retrieveIframe : function(sid) {
            var iframeid = IFRAME_PREFIX + sid;
            var iframe = doc.querySelector('#' + iframeid);

            if (iframePool.length >= iframeLimit) {
                doc.body.removeChild(iframe);
            } else {
                iframePool.push(iframe);
            }
        },
        /**
         * web调用native执行位置
         * @param obj       调用的客户端类名
         * @param method    调用的客户端方法名
         * @param params    额外的参数
         * @param sid       sid当前的key
         */
        callMethod: function(obj, method, params, sid) {


            params = WV_Private.buildParam(params);

            if (isWin) {
                //这里没啥用啊
                if (isWinWV) {
                    win['WindVane_Win_Private'].call(obj, method, sid, params);
                } else {
                    //表示 JSBridge 在不支持的 Windows 设备中（如浏览器）被调用
                    this.onComplete(sid, {ret: 'HY_NO_HANDLER_ON_WP'}, 'failure');
                }

            } else {
                // hybrid://objectName:sid/methodName?params

                var uri = LOCAL_PROTOCOL + '://' + obj + ':' + sid + '/' + method + '?' + params;
                
                if (isIOS) {
                    // iOS下用iframe调用, params是已经序列好的了
                    this.setParam(sid, params);

                    this.useIframe(sid, uri);

                } else if (isAndroid) {
                    // Android下用window.prompt调用调用

                    var value = WV_PROTOCOL + ':';
                    window.prompt(uri, value);

                } else {
                    //表示 JSBridge 在不支持的设备中（如浏览器）被调用。
                    this.onComplete(sid, {ret: 'HY_NOT_SUPPORT_DEVICE'}, 'failure');
                }
            }
        },

        registerGC: function(sid, timeout) {
            // 垃圾回收
            var that = this;
            //根据指定的timeout时间和已经默认的时间, 取最大值
            var callGCTime = Math.max(timeout || 0, CALL_GC_TIME);

            var paramGCTime = Math.max(timeout || 0, PARAM_GC_TIME);
            var chunkGCTime = Math.max(timeout || 0, CHUNK_GC_TIME);

            setTimeout(function(){
                //删除注册函数
                that.unregisterCall(sid);
            }, callGCTime);

            if (isIOS) {
                // ios下处理params的回收
                setTimeout(function(){
                    if (that.params[PARAM_PREFIX + sid]) {
                        delete that.params[PARAM_PREFIX + sid];
                    }
                }, paramGCTime);

            } else if (isAndroid) {
                // android下处理chunk的回收
                setTimeout(function(){
                    if (that.chunks[CHUNK_PREFIX + sid]) {
                        delete that.chunks[CHUNK_PREFIX + sid];
                    }
                }, chunkGCTime);
            }
        },

        //调用成功后 type 返回函数类型
        onComplete: function(sid, data, type) {

            //清除定时器 应该是传过来的
            clearTimeout(sid);

            var call = this.unregisterCall(sid);

            var success = call.success;
            var failure = call.failure;
            var deferred = call.deferred;

            data = data ? data : this.getData(sid);
            data = this.parseData(data);

            var ret = data.ret;

            if (typeof ret === 'string') {
                data = data.value || data;
                if (!data.ret) {
                    data.ret = [ret];
                }
            }

            if (type === 'success') {
                success && success(data);    
                deferred && deferred.resolve(data);

            } else if (type === 'failure') {
                failure && failure(data);    
                deferred && deferred.reject(data);

            }
            
            if (isIOS) {
                //iOS下回收iframe
                this.retrieveIframe(sid);
                if (this.params[PARAM_PREFIX + sid]) {
                    delete this.params[PARAM_PREFIX + sid];    
                }
            } else if (isAndroid) {
                if (this.chunks[CHUNK_PREFIX + sid]) {
                    delete this.chunks[CHUNK_PREFIX + sid];
                }
            }
        }
    };

    //将WV_Core核心方法赋值给WindVane对象(这个对象一开始是空的)
    for (var key in WV_Core) {
        if (!hasOwnProperty.call(WindVane, key)) {
            WindVane[key] = WV_Core[key];
        }
    }
})(window, window['lib'] || (window['lib'] = {}))
