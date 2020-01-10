# Database and Application
![홈](https://github.com/wsx9412/portfolio/blob/master/Tetris/picture/Tetris.png)

Java의 AWT, SWING을 이용하여 테트리스를 구현함  

---  
> 시연영상

  - https://www.youtube.com/watch?v=xVN-XV4qxY8
---

> 개발환경

  - 개발 툴 : Eclipse
  - 개발 언어 : Java
  - 플랫폼 : SofrWare

> 제작기간
  - 20.01.08 ~ 20.01.10  

---
> 개발한 기능  

  - 7개의 블럭이 한 사이클동안 겹치지 않고 랜덤으로 등장
  - 한 줄에 블럭이 가득차면 그 줄의 블럭을 모두 제거하고 위에 있는 블럭을 아래로 내리는 기능
  - spacebar를 눌렀을때 블럭을 최대한 내릴 수 있는 곳까지 한 순간에 내리는 기능
  - 블럭을 회전시키는 기능
  - 블럭을 생성할 수 없을 경우에 GameOver 메세지를 출력하는 기능

> 개발할 기능  

  - 줄이 제거될때마다 점수를 추가하여 오른쪽 위에 출력하기
  - 점수가 올라 일정 수준이 될 때마다 속도의 증가시키기
  - 소켓 통신을 통해 멀티플레이가 가능하도록 하기
  - 목표 제작기간 ~20.01.15

---

> 주소
 - Ui : <https://github.com/wsx9412/portfolio/blob/master/Tetris/Tetris/src/Ui.java>
 - Block Class <https://github.com/wsx9412/portfolio/blob/master/Tetris/Tetris/src/Block/Block.java>  이 클래스를 상속받아 각종 블럭을 생성
 - Block Factory <https://github.com/wsx9412/portfolio/blob/master/Tetris/Tetris/src/BlockFactory.java>
