var http = require('http');
var fs = require('fs');
var url = require('url');
const express = require('express');
const router = express.Router();
const mysql = require('mysql');

let client = mysql.createConnection({
  user:"root",
  password:"9412"
  database:"TAXI"
})
module.exports = router;

var app = http.createServer(function(request,response){
    var _url = request.url;
    var queryData = url.parse(_url,true).query;
    var pathname = url.parse(_url,true).pathname;
    if(pathname === '/'){
      if(queryData.id === undefined){
        fs.readdir('./data', function(error,filelist){
          var title = 'Welcome';
          var description = 'Hello, Node.js';
          /*
          var list = '<ul>
            <li><a href="?id=HTML">HTML</a></li>
            <li><a href="?id=CSS">CSS</a></li>
            <li><a href="?id=JavaScript">JavaScript</a></li>
          </ul>'
          */
          var list = '<ul>';

          var i = 0;
          while(i<filelist.length){
            list = list + `<li><a href="/?id=${filelist[i]}">${filelist[i]}</a></li>`
            i++;
          }
          list = list + '</ul>';
          var template = `
          <!doctype html>
          <html>
          <head>
            <title>WEB1 - ${title}</title>
            <meta charset="utf-8">
          </head>
          <body>
            <h1><a href="/">WEB</a></h1>
            ${list}
            <h2>${title}</h2>
            <p>
            ${description}
            </p>
          </body>
          </html>
          `;
            response.writeHead(200);
            response.end(template);
        })
    }else{
      fs.readdir('./data', function(error,filelist){
      var list = '<ul>';

      var i = 0;
      while(i<filelist.length){
        list = list + `<li><a href="/?id=${filelist[i]}">${filelist[i]}</a></li>`
        i++;
      }fs.readFile(`data/${queryData.id}`,'utf8',function(err,description){
      var title = queryData.id;
      var template = `
      <!doctype html>
      <html>
      <head>
        <title>WEB1 - ${title}</title>
        <meta charset="utf-8">
      </head>
      <body>
        <h1><a href="/">WEB</a></h1>
        ${list}
        <h2>${title}</h2>
        <p>
        ${description}
        </p>
      </body>
      </html>
      `;
        response.writeHead(200);
        response.end(template);
    })})}
  }
  else{
    response.writeHead(404);
    response.end('Not Found');
  }



    //return;

});
app.listen(3000);
