# 1. 실습 준비
실습실 컴퓨터 3대 정도에 이용 <br>
목표하고자 하는 환경
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img35.jpg" width="1000px" height="500px" alt="goal"></img><br/>

## 1-1. ubuntu로 부팅하기
참고 사이트 : https://generalthird.tistory.com/16 <br>
각 컴퓨터를 ubuntu로 부팅하기 위해 usb에 ubuntu iso 파일을 담기 <br>
http://mirror.kakao.com/ 에 접속해 ubuntu-release 선택, Ubuntu 20.04.4 LTS로 다운 <br>
http://rufus.akeo.ie/ 에 접속해 Rufus 3.19를 다운
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img36.JPG" width="700px" height="800px" alt="rufus"></img><br/>
위와 같이 설정하고 경고 창은 예 / 확인을 누름 <br>
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img37.JPG" width="700px" height="500px" alt="usb_ubuntu"></img><br/>

각 컴퓨터에 ubuntu를 설치하고, ubuntu로 부팅함

## 1-2. 각 노드 환경 setting 하기
start.md 파일을 참고하여 mysql, prometheus, node_exporter, mysql_exporter 을 설치함
한 노드[노드2]에서 node_exporter, mysql_exporter를 실행함

## 1-3. 노드끼리 통신하기
참고 사이트 : https://server-engineer.tistory.com/840

netstat 설치하기
```
$ sudo apt install net-tools
```

LISTEN 중인 포트 확인(node_exporter, mysql_exporter가 실행중임)
```
$ netstat -nap | grep LISTEN
```
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img38.JPG" width="700px" height="500px" alt="listen"></img><br/>

포트 열기
```
$ sudo iptables -I input 1 -p tcp --dport 9100 -j ACCEPT
$ sudo iptables -I input 1 -p tcp --dport 9104 -j ACCEPT
```

열린 포트 확인
```
$ sudo iptables -L -v
```
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img39.JPG" width="700px" height="500px" alt="port"></img><br/>


[노드2] 의 ip 주소 확인
```
$ hostname -I
```


다른 노드[노드1]에서 접속
```
$ telnet [노드1의 아이피]
```
해당포트 접속시 실행됨
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img40.JPG" width="700px" height="500px" alt="telnet"></img><br/>