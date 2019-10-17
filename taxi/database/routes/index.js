var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var bcrypt = require('bcrypt-nodejs');
//암호화
/* GET home page. */
let client = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "94129412",
  database: "TAXI",
  port:"3306"
})

module.exports = router;
//함수 area
function getDist(lat1,long1,lat2,long2){
  var ret = 0;
  var lat = 111;
  var long = 88.8;
  ret = Math.sqrt(
    Math.pow((Math.abs(lat1-lat2)*lat),2)+
      Math.pow((Math.abs(long1-long2)*long),2)
  );
  return ret.toFixed(2);
}

//통신 area
router.get('/create', function(req, res, next) {
  client.query("SELECT * FROM member;", function(err, result, fields){
    if(err){
      console.log(err);
      console.log("쿼리문에 오류가 있습니다.");
    }
    else{
      res.render('create', {
        results: result
      });
    }
  });
});

router.post('/create', function(req, res, next) {
  var body = req.body;
  client.query("INSERT INTO mem (id, name, age) VALUES (?, ?, ?)", [
    body.id, body.name, body.age
  ], function(){
    res.redirect("/create");
  });
});

router.post('/post',function(req,res,next){
  var body = req.body;

  if(body.type == "login"){
    client.query("SELECT * FROM mem WHERE mem_id=?",
    [body.id]
    , function(err, result, fields){
      if(err){
        console.log(err);
      }
      else if(result[0]){
        console.log(result);
        bcrypt.compare(body.password, result[0].mem_password, function(err, res2) {
                    if (res2) {
                      res.write(result[0].mem_code);
                      res.end();
                      console.log(result[0]);
                    }
                    else {
                      res.write("2");
                      res.end();
                    }
                });
      }else{
        res.write("2");
        res.end();
      }
    });
  }
  else if(body.type == "sign_up"){//회원가입
    //bcrypt.hash(password, null, null, function(err, hash) {}
    bcrypt.hash(body.password, null, null, function(err, hash) {
      if(err){
        console.log(err);
        res.write("0");//통신 실패
        res.end();
      }
      else{
        client.query("insert into mem(mem_id,mem_password,mem_name,mem_code,mem_phonenumber) values (?,?,?,?,?)",
        [body.id,hash,body.name,body.mem_code,body.phone_number]
        , function(err,result,fields){
          if(err){
            console.log(err);
            res.write("0");//통신 실패
            res.end();
          }
          if(body.mem_code == "0"){
            client.query("insert into driver(driver_id,isDrive,customer_on) values(?,false,false)",[body.id], function(err,result2){
              if(err){
                console.log(err);
                res.write("0");//통신 실패
                res.end();
              }
            });
          }
          else if(body.mem_code == "1"){
            client.query("insert into customer(customer_id) values(?)",[body.id], function(err,result2){
              if(err){
                console.log(err);
                res.write("0");//통신 실패
                res.end();
              }
            });
          }
          res.write("1");//통신 성공
          res.end();
        });
      }
    });
  }// 회원가입
  else if(body.type == "isDrive_true"){
    client.query("update driver set isDrive = true where driver_id = ?",[body.id],function(err){
      if(err){
        console.log(err);
        res.write("0");//통신 실패
        res.end();
      }
      res.write("1");//통신 실패
      res.end();
    });
  }
  else if(body.type == "isDrive_false"){
    client.query("update driver set isDrive = false where driver_id = ?",[body.id],function(err){
      if(err){
        console.log(err);
        res.write("0");//통신 실패
        res.end();
      }
      res.write("1");//통신 실패
      res.end();

    });
  }

});
router.post('/location',function(req,res,next){
  var body = req.body;
  var sqlquery;
  var res_sqlquery;
  if(body.type == "location_set"){//좌표저장
    if(body.mem_code == "0"){
      sqlquery = "update driver set longitude = ?, latitude = ? where driver_id=?"
    }
    else if(body.mem_code == "1"){
      sqlquery = "update customer set longitude = ?, latitude = ? where customer_id=?"
    }
    client.query(sqlquery, [body.longitude,body.latitude,body.id], function(err){
      if(err){
        console.log(err);
        res.write("0");
        res.end();
      }
      res.write("1");
      res.end();
    });
  }
  else if(body.type == "get_information"){//좌표획득
    if(body.mem_code == "0"){
      sqlquery = "SELECT customer_id from driver where driver_id = ?"
      res_sqlquery = "SELECT customer_id,longitude,latitude,destination_longitude,destination_latitude from customer where customer_id = ?"
    }
    else if(body.mem_code == "1"){
      //sqlquery = "SELECT driver_id from driver where customer_id = ? and isDrive = true"
      sqlquery = "SELECT driver_id from driver where customer_id = ? and isDrive = true and customer_on = true"
      res_sqlquery = "SELECT driver_id,longitude,latitude from driver where driver_id = ?"
    }
    client.query(sqlquery,[body.id],function(err,result){
      if(err){
        console.log(err);
        res.write("0");
        res.end();
      }
      if(result[0]){
        if(body.mem_code == "0"){
          client.query(res_sqlquery,[result[0].customer_id],function(err,result2){
            if(err){
              console.log(err);
              res.write("0");
              res.end();
            }
            else if(result2[0]){
              var location = "id :" + result2[0].customer_id + "  longitude : "+result2[0].longitude+" latitude : "+  result2[0].latitude + " destination_longitude : " +   result2[0].destination_longitude + " destination_latitude  : " + result2[0].destination_latitude;
              console.log(location);
              res.write(location);
              res.end();
            }
            else{
              res.write("0");
              res.end();
            }
          });
        }
        else if(body.mem_code == "1"){
          client.query(res_sqlquery,[result[0].driver_id],function(err,result2){
            if(err){
              console.log(err);
              res.write("0");
              res.end();
            }
            else if(result2[0]){
              var location = "longitude : "+ result2[0].longitude+" latitude : "+ result2[0].latitude + " id  : " +result2[0].driver_id;
              console.log(location);
              res.write(location);
              res.end();
            }
            else{
              res.write("0");
              res.end();
            }
          });
        }
      }
      else{
        res.write("0");
        res.end();
      }
    });
  }
  else if(body.type == "set_destination"){
    sqlquery = "update customer set destination_longitude = ?, destination_latitude = ? where customer_id=?"
    client.query(sqlquery, [body.longitude,body.latitude,body.id], function(err){
      if(err){
        console.log(err);
        res.write("0");
        res.end();
      }
      res.write("1");
      res.end();
  });
  }
});
//가까운 택시기사 검출
router.post('/matching',function(req,res,next){
  var body = req.body;
  var sqlquery = "";
  var res_sqlquery = "";
  var nearestDist;
  var latitude;
  var longitude;
  var dist;
  var num;
  console.log(body.id);
  if(body.type == "get_nearestTaxi"){
    console.log(body.id);
    longitude = parseFloat(body.longitude);
    latitude = parseFloat(body.latitude);
    sqlquery = "select driver.driver_id, driver.longitude, driver.latitude from driver left outer join negativeList on driver.driver_id = negativeList.driver_id and negativeList.customer_id = ? where negativeList.driver_id is null and driver.isDrive = true and driver.customer_on =  false"
    res_sqlquery = "update driver set customer_id = ? where driver_id = ?"
    client.query(sqlquery,[body.id],function(err,result){
      if(err){
        res.write("0");
        res.end();
      }
      else if(result[0]){
        for(var i = 0; i<result.length;i++){
          if(i == 0){
            nearestDist = getDist(parseFloat(result[i].longitude),parseFloat(result[i].latitude),longitude,latitude);
            num = i;
          }
          else{
            dist = getDist(parseFloat(result[i].longitude),parseFloat(result[i].latitude),longitude,latitude);
            if(nearestDist>dist){
              nearestDist = dist;
              num = i;
            }
          }
        }
        client.query(res_sqlquery,[body.id,result[num].driver_id],function(err){
          if(err){
            res.write("0");
            res.end();
          }
          else{
            client.query(res_sqlquery,[body.id,result[num].driver_id],function(err){
              if(err){
                res.write("0");
                res.end();
              }
              var info ="id : "+ result[num].driver_id+ " longitude : "+result[num].longitude+" latitude : "+ result[num].latitude + " nearestDist : " + nearestDist;
              console.log(info);
              res.write(info);
              res.end();
            });
          }
        });
      }
      else{
        res.write("0");
        res.end();
      }
    });
  }
  else if(body.type == "ok"){
    sqlquery = "update driver set customer_on = true where driver_id = ?"
    res_sqlquery = "delete from negativeList where customer_id = ?"
    client.query(sqlquery,[body.driver_id], function(err){
      if(err){
        res.write("0");
        res.end();
      }
      client.query(res_sqlquery,[body.customer_id], function(err){
        if(err){
          res.write("0");
          res.end();
        }
        res.write("1");
        res.end();
      });
    });
  }
  else if(body.type == "cancel"){
    sqlquery = "update driver set customer_on = false, customer_id = null where driver_id = ?"
    res_sqlquery = "insert into negativeList(customer_id,driver_id) values (?,?) "
    client.query(sqlquery,[body.driver_id], function(err){
      if(err){
        res.write("0");
        res.end();
      }
      else{
        client.query(res_sqlquery,[body.customer_id,body.driver_id],function(err){
          if(err){
            res.write("0");
            res.end();
          }
          res.write("1");
          res.end();
        });
      }
    });
  }
  else if(body.type == "reset"){
    sqlquery = "update driver set customer_on = false and customer_id = null where driver_id = ?"
    client.query(sqlquery,[body.id],function(err){
      if(err){
        console.log(err);
        res.write("0");
        res.end();
      }
      res.write("1");
      res.end();
    });
  }
});
