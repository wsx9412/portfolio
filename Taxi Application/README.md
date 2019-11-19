# Database and Application
![홈](https://github.com/wsx9412/web/blob/master/Taxi%20Application/picture/taxi.png?raw=true){: .center}

Node.js를 통한 HTTP통신을 통해 DB를 조회 및 수정하여 가장 근접한 택시를 호출하는 어플리케이션

---

> 졸업작품

  - 택시 어플리케이션을 만들기 위해 node.js를 이용한 데이터베이스(mysql) 관리 시스템  
---

> 개발환경

  - 개발 툴 : MySQL, Android Studio, Atom
  - 개발 언어 : MySQL Query, Java, Node.js
  - 프레임워크 : Express
  - 플랫폼 : Database server, Application
  - 사용API : bcrypt, T-map API  

> 제작기간
  - 2019.08 ~ 2019.10  

---
> 개발한 기능

  - 회원가입(아이디, 비밀번호(bcrypt로 인한 암호화), 이름, 핸드폰번호, 회원종류(기사, 승객))  
  - 로그인 (중복 로그인 방지 / 데이터베이스를 이용하여 중복로그인을 방지함)  
  - 기사의 위치를 실시간으로 반영하여 관리   
  - 주소를 검색하여 원하는 주소를 획득 가능  
  - 주소를 지오코딩하여 좌표로 변환 가능  
  - 좌표를 리버스지오코딩 하여 주소를 획득 하는것이 가능  
  - 승객의 요청에 따라 도착지와 출발지를 수신하고 최근접택시에 승객의 요청정보 송신하여 매칭  
  - 매칭 후 기사의 수락여부에 따라 택시정보를 수신하여 고객에게 재송신 / 거부했을 시 그 다음 최근접택시에 반복  
  - 도착지 도착했을때 DB에 존재하는 기사와 승객의 매칭정보를 제거  

---

> 주소
 - Database : <https://github.com/wsx9412/web/blob/master/Taxi%20Application/database/routes/index.js>
 - HTTP(Android) : <https://github.com/wsx9412/web/blob/master/Taxi%20Application/java%20source/JsonTask.java>
 - Login(Android) : <https://github.com/wsx9412/web/blob/master/Taxi%20Application/java%20source/MainActivity.java>
 - Customer(Android) : <https://github.com/wsx9412/web/blob/master/Taxi%20Application/java%20source/activity_map.java>
 - Driver(Android) : <https://github.com/wsx9412/web/blob/master/Taxi%20Application/java%20source/activity_driver.java>
