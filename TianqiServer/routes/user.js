var express = require('express');
var router = express.Router();
var URL = require('url');
//加载mysql模块
var mysql = require('mysql');
var dbConfig = require('../config/db');

// 使用DBConfig.js的配置信息创建一个MySQL连接池
var pool = mysql.createPool(dbConfig.mysql);

// 响应一个JSON数据
var responseJSON = function (res, ret) {
    if (typeof ret === 'undefined') {
        res.json({
            code: '-200', msg: '操作失败'
        });
    } else {
        res.json(ret);
    }
};

//SQL语句
var addSql = 'INSERT INTO user_info(username,password,e_mail) VALUES(?,?,?)';
var queryUser = 'SELECT * FROM `user_info` WHERE username = ?';


router.get('/regist', function(req, res, next) {
    //解析请求参数
   var params = URL.parse(req.url, true).query;
   var addSqlparams = [params.username, params.password,params.e_mail];

// 从连接池获取连接
    pool.getConnection(function (err, connection) {

        //首先查询数据库看用户是否存在
        connection.query(queryUser,params.username,function (err, result) {

            if(result.length != 0){
                res.json({"status":"error","msg":"user_exist"});/*用户已经被注册*/
                return;
            }else {
                //建立连接 增加一个用户信息
                connection.query(addSql, addSqlparams, function (err, result) {
                    if (err) {
                        console.log('[INSERT ERROR] - ', err.message);
                        return;
                    }
                    if(result!=null){
                        res.json({"status":"success","msg":"regist_ok"});/*注册成功*/
                    }
                });

            }
            // 释放连接
            connection.release();
        })


    });
});

router.get('/login',function (req, res, next) {
    //解析请求参数
    var params = URL.parse(req.url, true).query;

    // 从连接池获取连接
    pool.getConnection(function (err, connection) {

        connection.query(queryUser,params.username,function (err, result) {

            if(err){
                res.json({"status":"error","msg":"error_system"});/*系统错误*/
            }else if(result.length == 0){
                res.json({"status":"error","msg":"error_usernameNo_exist"});/*用户名不存在*/
            } else {
                if(result[0].password == params.password){
                    res.json({"status":"success","msg":"login_ok"});//登录成功
                }
                else{
                    res.json({"status":"error","msg":"error_password"});//密码错误
                }
            }
            // 释放连接
            connection.release();
        })
    });
});

module.exports =  router;




