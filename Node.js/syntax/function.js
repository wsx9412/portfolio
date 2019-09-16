abc(1,4);
sum(2,3);
var fs = require('fs');
function abc(a,b){
  console.log(a);
  console.log(b);
  console.log(a+b);
  console.log(a*b);
}

function sum(first, second){
  console.log(second);
  fs.writeFile('./result.txt',Math.round(1.6),function(err,write));
}
