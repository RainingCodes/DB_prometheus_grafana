# c로 kafka consumer 만들고 postgresql table에 넣기

## STEP 1 library 준비
* https://github.com/edenhill/librdkafka 참고
* 설치
```
$ sudo apt install librdkafka-dev
```
* build
```
$ git clone https://github.com/edenhill/librdkafka.git
$ cd librdkafka
$ sudo ./configure
$ sudo make
$ sudo make install 
```

## STEP 2 consumer.c를 컴파일한 consumer 실행시키기
  * zookeeper, kafka 실행한 상태여야 함 <a href="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/start.md#step2--%EC%8B%A4%ED%96%89">kafka 실행</a>
  * eclipse에 작성해둔 producer를 먼저 한 번 실행해야함, PC를 껐다가 다시켰을 경우는 반드시 수행 <a href="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/start.md#step2--%EC%8B%A4%ED%96%89">producer.java</a>
  * 예제 consumer를 실행시키면 kafka 메시지를 확인할 수 있음
  * consumer 실행법
    ```
    % usage ./consumer <broker> <group.id> <topic1> <topic2>..
    ```
  * 각 인자에 해당하는 내용은 kafka의 config 파일들을 확인하면 됨.
  * broker id - server.properties에 적혀있음
  * group.id - consumer.properties 에 적혀있음
  * topic - 각자 지정한 것으로 전달하면 됨, 예제는 TEST를 기준으로 실행함  
  * 종료시에는 Ctrl + C를 눌러 종료  
    ```
    $ cd ~/librdkafka/examples 
    $ ./consumer 0 test-consumer-group TEST
    ```
  * 실행화면
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img17.JPG" width="1000px" height="300px" alt="consumer.c"><br/> 

## STEP 3 postgresql에 table 추가
  * 접속하기
  ```
  $ psql
  ```
  * test란 table 생성
    - column : topic, val 로 구성
  ```
  user1=# create table test ( Topic varchar(20), val integer);
  user1=# select * from test;
  ```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img18.JPG" width="300px" height="50px" alt="test table creation"><br/> 

## STEP 4 consumer.c와 postgresql 연결하기

  * library 다운로드
  ```
  $ sudo apt-get install libpq-dev
  ```
  * libpq의 원본 파일을 consumer.c 컴파일 할 때 추가 하기 위해 consumer.c와 같은 디렉터리로 이동
  ```
  $ cp -r /usr/include/postgresql/* ~/librdkafka/examples/
  ```
  * consumer.c에 postgresql 연결 관련 코드 추가
   - https://www.postgresql.org/docs/current/libpq-example.html 참고해서 변형
  파일 <a href="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/consumer.c">consumer.c</a>

  * 변경사항을 적용한 consumer.c 컴파일 (실행위치 ~/librdkafka/examples)
  ```
  $ gcc -g -O2 -fPIC -Wall -Wsign-compare -Wfloat-equal -Wpointer-arith -Wcast-align -I../src consumer.c -o consumer  ../src/librdkafka.a -lm -ldl -lpthread -lrt -lpthread -lrt -lpq
  ```

  * 실행
  ```
  $ ./consumer 0 test-consumer-group TEST TEST2
  ```
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img19.JPG" width="1000px" height="300px" alt="producer, consumer, postgresql"><br/> 
