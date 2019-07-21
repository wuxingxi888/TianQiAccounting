var express = require('express');
var path = require('path');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var createError = require('http-errors');
var bodyParser = require('body-parser');// 引入json解析中间件

// 路由信息（接口地址），存放在routes的根目录
var index = require('./routes/index');
var users = require('./routes/users');
var user = require('./routes/user');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));//设置views文件夹为存放视图文件的目录，即存放模板文件的目录。_dirname为全局变量，存储当前正在执行的脚本所在的目录。
app.set('view engine', 'jade');  //设置视图模板引擎为jade。


app.use(logger('dev')); //加载日志中间件。
app.use(bodyParser.json());//加载解析json的中间件。
app.use(bodyParser.urlencoded({ extended: false }));//加载解析urlencoded请求体的中间件。
app.use(cookieParser()); //加载解析cookie的中间件。
app.use(express.static(path.join(__dirname, 'public')));

//配置路由，（'自定义路径'，上面设置的接口地址）
app.use('/', index);
app.use('/search', users);//查
app.use('/user', user);//增


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
