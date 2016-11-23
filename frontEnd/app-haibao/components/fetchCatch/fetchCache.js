(function(win) {
    //下一个版本, 改进版本号的功能
    function FetchCache(url, call) {
        this.url = url;
        this.call = call;

        this.hasCache = {};
        this.reqCache = {
            version: ""
        };
    }
    FetchCache.prototype = {
        constructor: FetchCache,
        run: function() {
            var self = this;
            self._checkCache();
            var request = new Request(this.url);

            //请求没有的数据
            if(self.reqCache.version.length > 0) {
                request.get(self.reqCache, function (data) {
                    self.data = data;
                    self._package();
                });
            }else {
                //不需要请求, 直接打包
                self._package();
            }
        },
        _checkCache: function() {
            var resource = document.body.getAttribute("data-version").split(";");
            for(var i = 0, len = resource.length; i < len; i++) {
                var curStr = resource[i];
                var val = window.localStorage.getItem(curStr);
                //防止键值不存在
                if(!curStr) {
                    break;
                }
                //组装已经有的和要请求的缓存
                if(val) {
                    this.hasCache[curStr] = val;
                }else {
                    this.hasCache[curStr] = "";
                    this.reqCache["version"] += curStr + ";";
                }
            }
        },
        _package: function() {
            var cssEle = null,
                jsEle = null,
                cssFragment = document.createDocumentFragment(),
                jsFragment = document.createDocumentFragment();
            //将请求数据加入hasCache中, 达到比对结果
            for(var prop in this.data) {
                this.hasCache[prop] = this.data[prop];
            }
            for(var prop in this.hasCache) {
                if(/css_/.test(prop)) {
                    cssEle = document.createElement("style");
                    cssEle.appendChild(document.createTextNode(this.hasCache[prop]));
                    cssFragment.appendChild(cssEle);
                }
                if(/js_/.test(prop)) {
                    jsEle = document.createElement("script");
                    jsEle.appendChild(document.createTextNode(this.hasCache[prop]));
                    jsFragment.appendChild(jsEle);
                }
            }
            document.head.appendChild(cssFragment);
            document.body.appendChild(jsFragment);
            //数据填充完毕,开启回掉
            this.call();
            this._setCache(this.data);
        },
        _setCache(data) {
            //将新请求的数据存入localStorage中
            for(var prop in data) {
                window.localStorage.setItem(prop, this.data[prop]);
            }
        }
    };
    win.FetchCache = FetchCache;
})(window);