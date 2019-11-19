var server = require('http');
const sv = server.createServer();

sv.on('request',function(req,res){

  sv.on('message',function(req,res){

  });
  sv.on('data',function(req,res){

  });
});
sv.listen(9000,'localhost');
sv.emit()
