# postgresql grafana dashboard 만들기
참고 사이트 : https://dejavuqa.tistory.com/16

## postgresql
 * postgresql 설치 
```
$ sudo apt-get update
$ sudo apt-get install postgresql postgresql-contrib
```

 * postgres 기본 계정, postgresql 관리 계정 : postgres (기본 생성) <br>
 * postgres 계정으로 접속 후 ubuntu root 계정과 동일한 이름의 postgresql user 생성
 * ubuntu root 계정과 동일한 이름의 db 생성
```
$ sudo -i -u postgres
postgres $ createuser --interactive
postgres $ ceatedb node1
```
 * 실행 화면(postgresql version 14.4)
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img53.JPG" width="500px" height="200px" alt="create user & create db"></img><br/>
 
* postgresql에 node1 계정으로 node1 db에 접속
* 접속 후 접소 정보 확인
* table create, record insert 등의 자유롭게 쿼리 생성 가능 (mysql과 문법 차이점 : varchar2가 없으므로 전부 varchar로 변경)
* postgres 종료
```
$ sudo -u node1 psql -d node1
node1=# \conninfo
node1=# (자유롭게 query 실행)
node1=# \q

```
* 실행 화면
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img54.JPG" width="500px" height="200px" alt="postgresql node1"></img><br/>

## postgresql exporter
 * https://github.com/prometheus-community/postgres_exporter 에 설명이 있음
 * postgresql exporter를 build & 실행을 위해 설치해야하는 프로그램
    - make
    - curl
  ```
    $ sudo apt install make
    $ sudo apt install curl
  ```
 * src 불러오기 & build
```
$ git clone https://github.com/prometheus-community/postgres_exporter.git
$ cd postgres_exporter
$ make build
```
 * 실행
```
$ ./postgres_exporter
```
 * 웹 localhost:9187 접속
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img55.JPG" width="1000px" height="900px" alt="postgres_exporter"></img><br/>

 * prometheus.yml에 등록
 ```
 $ vi prometheus.yml
 - job_name : 'pgexporter'
        scrape_interval : 5s
        static_configs:
      - targets: ['[본인 컴퓨터 ip]:9187']
```
<br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img56.JPG" width="500px" height="50px" alt="prometheus.yml"></img><br/>

 * prometheus 실행 후 웹으로 localhost:9090 접속 Status > Targets
  <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img57.JPG" width="1000px" height="200px" alt="prometheus targets"></img><br/>
 
* grafana 실행 후 localhost:3000 접속 후 Dashboard import
   - https://grafana.com/grafana/dashboards/9628-postgresql-database/ 
    <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img58.JPG" width="1000px" height="900px" alt="grafana1"></img><br/>
 <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img59.JPG" width="1000px" height="900px" alt="grafana2"></img><br/>
 <br>
<img src="https://github.com/RainingCodes/mysql_performance_schema/blob/main/img/img60.JPG" width="1000px" height="900px" alt="grafana3"></img><br/>
