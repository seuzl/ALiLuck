var http = require('http');

http.createServer(function (request, response) {
    var arr = [];
    request.on("data",function(data){
        arr.push(data);
    });
    request.on("end",function(){
        var data= Buffer.concat(arr).toString(),
            ret;
        try{
            ret = JSON.parse(data);
        }catch(err){}
        console.log(ret);
    });

    // 发送 HTTP 头部
    // HTTP 状态值: 200 : OK
    // 内容类型: text/plain  "Access-Control-Allow-Origin": "*"
    response.writeHead(200, {
        'Access-Control-Allow-Origin': '*'
    });

    // 发送响应数据 "Hello World"
    response.end('Hello World\n');
}).listen(8888);

// 终端打印如下信息
console.log('Server running at http://127.0.0.1:8888/');