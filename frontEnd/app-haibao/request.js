/**
 * ajax简单的封装
 */
(function(win) {
    /**
     * ajax请求部分
     * @param url
     * @constructor
     */
    function Request(url) {
        this.url = url;
    }
    Request.prototype = {
        constructor: Request,
        get: function (data, call) {
            var url;
            data = data || "";
            this.xhr = new XMLHttpRequest();
            if (typeof data === "object") {
                url = this.url + Util.encodeGet(this.url, data);
            } else {
                url = this.url + data;
            }
            this.xhr.withCredentials = true;
            this.xhr.open("GET", url, true);
            this.xhr.send(null);
            this.bindCall(call);
        },
        post: function (data, call, token) {
            data = data || "";
            this.xhr = new XMLHttpRequest();
            this.xhr.open("POST", this.url, true);
            if (typeof data === "object") {
                try {
                    data = JSON.stringify(data);
                } catch (e) {
                    console.log("JSON格式错误");
                    return false;
                }
                this.xhr.setRequestHeader('access_token', token);
            } else {
                this.xhr.setRequestHeader("access_token", token);
            }

            console.log(data);
            this.xhr.send(data);
            this.bindCall(call);
        },
        bindCall: function (call) {
            var self = this;
            this.xhr.onreadystatechange = function () {
                if (self.xhr.readyState === 4) {
                    if (self.xhr.status >= 200 && self.xhr.status < 300 || self.xhr.status === 304) {
                        call(JSON.parse(self.xhr.responseText));
                    }
                }
            }
        }
    };
    var Util = {
        encodeGet: function (url, json) {
            var result = url.indexOf("?") === -1 ? "?" : "";
            for (var prop in json) {
                result += prop + "=" + json[prop].toString()
            }
            return result;
        }
    };
    win.Request = Request;
})(window);