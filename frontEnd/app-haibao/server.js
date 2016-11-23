/**
 * 缓存资源接口模拟
 */
"use strict";
let express = require("express");
let app = express();
let fse = require("fs-extra");
let bodyParser = require("body-parser");
let cwd = process.cwd();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));


function readFile(fileName) {
    let f = fse.readFileSync(cwd + "/build/" + fileName);
    return f.toString();
}

//查找服务器资源
app.post("/source", function(req, res) {
    let query = req.query;
    let params = {};
    for(let prop in query) {
        params[prop] = readFile(query[prop]);
    }
    res.json(params);
});
//测试内容
app.post("/test", function(req, res) {
    res.set("Access-Control-Allow-Origin", "*");
    console.log(req.body);
    res.json(req.body);
});

app.listen(8888, "127.0.0.1", function() {
    console.log("服务器启动了");
});