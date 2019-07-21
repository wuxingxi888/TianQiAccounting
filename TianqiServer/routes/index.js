var express = require('express');
var router = express.Router();

//编写执行函数
router.get("/", function (req, res) {
    res.render('index', {title: '首页'});
});
module.exports = router;