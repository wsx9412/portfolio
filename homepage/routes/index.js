var express = require('express');
var server = require('http');
var calculator = require('../public/javascripts/test.js');
const sv = server.createServer();
var router = express.Router();
/* GET home page. */
router.get('/', function(req, res, next) {
  console.log(calculator.add(1,2));
  res.render('index', { title: 'Express' });
});


module.exports = router;
sv.listen(3000,'localhost');
