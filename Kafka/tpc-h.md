# TPC Benchmark H와 Kafka

## 1. Intro
 - Benchmark : 컴퓨팅에서 특정 오브젝트(하드웨어 or 소프트웨어 등)에 대해 일반적으로 수많은 표준 테스트와 시도를 수행함으로써 오브젝트의 상대적인 <b>성능 측정</b>을 목적으로 컴퓨터 프로그램을 실행하는 행위 <br>
  https://ko.wikipedia.org/wiki/%EB%B2%A4%EC%B9%98%EB%A7%88%ED%81%AC_(%EC%BB%B4%ED%93%A8%ED%8C%85)
 - TPC-H : TPC-H is a Decision Support Benchmark
The TPC-H is a decision support benchmark. It consists of a suite of business oriented ad-hoc queries and concurrent data modifications. The queries and the data populating the database have been chosen to have broad industry-wide relevance. This benchmark illustrates decision support systems that examine large volumes of data, execute queries with a high degree of complexity, and give answers to critical business questions. The performance metric reported by TPC-H is called the TPC-H Composite Query-per-Hour Performance Metric (QphH@Size), and reflects multiple aspects of the capability of the system to process queries. These aspects include the selected database size against which the queries are executed, the query processing power when queries are submitted by a single stream, and the query throughput when queries are submitted by multiple concurrent users. The TPC-H Price/Performance metric is expressed as Price/QphH@Size for Version 2 and Price/kQphH@Size for Version 3. <br>
TPC-H는 의사결정 지원 밴치마크고 비스니스에 관련된 데이터를 다루는 쿼리를 갖고 있다. 성능 측정 도구로 QphH@Size가 있다.
(https://www.tpc.org/tpch/default5.asp)

## 2. ToolKit Download
- toolkit : tpc-h에 관한 테이블, 데이터 등을 생성해주는 쿼리를 담은 도구
- https://www.tpc.org/tpc_documents_current_versions/current_specifications5.asp 접속해서 Download TPC-H_Tools_v3.0.1.zip 를 클릭 (버전은 접속 할 때에 따라 다를 수 있음)
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img20.JPG" width="1000px" height="500px" alt="toolkit Download"><br/> 

- 사용자 정보를 잘 입력하면 메일로 toolkit을 다운로드할 수 있는 링크가 전송됨
- 링크를 접속하면 TPC-H_Tools_v3.0.1.zip를 다운받을 수 있음
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img21.JPG" width="1000px" height="300px" alt="toolkit Download2"><br/> 

- 다운받은 것을 압축 해제하면 명세서(specification)와 도구들(tool)이 들어있음

## 3. Toolkit Compile
- Toolkit에는 dbgen이란 폴더가 있음 이름에서 유추할 수 있듯이 DB를 만드는 것과 관련한 도구가 들어 있음
- makefile.suite가 들어있는데 이것을 Makefile로 복사
```
$ cd ~/TPC-H V3.0.1/dbgen
$ cp makefile.suite Makefile
```
- Makefile 내용에 컴파일러, DATABASE, MACHINE, WORKLOAD 부분을 설정
```
CC      = gcc
DATABASE= SQLSERVER
MACHINE = LINUX
WORKLOAD = TPCH
```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img22.JPG" width="800px" height="500px" alt="toolkit Compile1"><br/> 

- 컴파일 하면 dbgen, qgen 두 실행파일이 생성됨
```
$ make
```
- dbgen 실행, 종료는 Ctrl + C
```
$ ./dbgen
```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img23.JPG" width="800px" height="300px" alt="dbgen execution"><br/> 
- 실행 후에는 여러 테이블 데이터들(supplier.tbl, customer.tbl, orders.tbl, lineitem.tbl)이 생김 우리가 주목할 것은 lineitem.tbl

## 4. postgresql에 lineitem table 생성 
- lineitem table의 Columns : 
  * ORDERKEY : identifier
  * PARTKEY : identifier
  * SUPPKEY : identifier
  * LINENUMBER : integer
  * QUANTITY : decimal
  * EXTENDEDPRICE : decimal
  * DISCOUNT : decimal
  * TAX : decimal
  * RETURNFLAG : fixed text, size 1
  * LINESTATUS : fixed text, size 1
  * SHIPDATE : date
  * COMMITDATE : date
  * RECEIPTDATE : date
  * SHIPINSTRUCT : fixed text, size 25
  * SHIPMODE : fixed text, size 44
  * COMMENT : variable text size 44
  
- postgresql에 접속 후 lineitem table 생성 (dss.ddl 파일에 생성문이 다 포함되어 있음)
  ```
  $ psql
  user1=#CREATE TABLE LINEITEM ( 
    L_ORDERKEY    INTEGER NOT NULL,
    L_PARTKEY     INTEGER NOT NULL,
    L_SUPPKEY     INTEGER NOT NULL,
    L_LINENUMBER  INTEGER NOT NULL,
    L_QUANTITY    DECIMAL(15,2) NOT NULL,
    L_EXTENDEDPRICE  DECIMAL(15,2) NOT NULL,
    L_DISCOUNT    DECIMAL(15,2) NOT NULL,
    L_TAX         DECIMAL(15,2) NOT NULL,
    L_RETURNFLAG  CHAR(1) NOT NULL,
    L_LINESTATUS  CHAR(1) NOT NULL,
    L_SHIPDATE    DATE NOT NULL,
    L_COMMITDATE  DATE NOT NULL,
    L_RECEIPTDATE DATE NOT NULL,
    L_SHIPINSTRUCT CHAR(25) NOT NULL,
    L_SHIPMODE     CHAR(10) NOT NULL,
    L_COMMENT      VARCHAR(44) NOT NULL);
  ```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img24.JPG" width="800px" height="500px" alt="lineitem table creation with postgresql"><br/> 

## 5. Kafka Setting
- zookeeper 실행 (새 터미널)
```
$ cd ~/kafka_2.13-3.3.0
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```
- kafka 실행 (새 터미널)
```
$ cd ~/kafka_2.13-3.3.0
$ bin/kafka-server-start.sh config/server.properties
```
- topic 생성 (새 터미널)
```
$ cd ~/kafka_2.13-3.3.0
$ bin/kafka-topics.sh --create --topic LINEITEM --bootstrap-server localhost:9092
```
- topic 생성 확인 (바로 위 터미널)
```
$ bin/kafka-topics.sh --describe --topic LINEITEM --bootstrap-server localhost:9092
```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img25.JPG" width="800px" height="200px" alt="topic creation"><br/>

## 6. Producer.java & consumer.c & postgresql(result check)
<a href="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/tpc-h_src/Producer.java">Producer.java</a>
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img26.JPG" width="800px" height="500px" alt="Producer.java"><br/> 

<a href="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/tpc-h_src/consumer.c">consumer.c</a>
- compile (실행위치 ~/src/c/librdkafka/examples)
```
$ gcc -g -O2 -fPIC -Wall -Wsign-compare -Wfloat-equal -Wpointer-arith -Wcast-align -I../src consumer.c -o consumer  ../src/librdkafka.a -lm -ldl -lpthread -lrt -lpthread -lrt -lpq
```
- execution (위와 같은 터미널)
```
$ ./consumer 0 test-consumer-group LINEITEM
```

<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img27.JPG" width="800px" height="500px" alt="Consumer.c"><br/> 

- postgresql 확인(4번에서 만든 터미널 이어서)
```
user=#select * from LINEITEM;
```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img28.JPG" width="2000px" height="800px" alt="Postgresql check"><br/> 