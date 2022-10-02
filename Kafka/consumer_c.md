# c로 kafka consumer 만들기

## STEP 1 library 준비
* https://github.com/edenhill/librdkafka 참고
* prebuilt packages 설치
```
$ apt install librdkafka-dev
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
  * zookeeper, kafka 실행한 상태여야 함 <a src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/start.md#step2--%EC%8B%A4%ED%96%89">kafka 실행</a>
  * eclipse에 작성해둔 producer를 먼저 한 번 실행해야함 (지난 번 실습 후 아예 종료했다면 그 기록이 남아 있지 않음, 다시 실행시켜야 함), 지우지 않았다면 Run만 눌러 실행하면 됨 <a src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/start.md#step2--%EC%8B%A4%ED%96%89">producer.java</a>
  * 예제 consumer를 실행시키면 kafka 메시지를 확인할 수 있음
  * consumer 실행법
    ```
    % usage ./consumer <broker> <group.id> <topic1> <topic2>..
    ```
  * 각 인자에 해당하는 내용은 kafka의 config 파일들을 확인하면 됨.
  * broker id - server.properties에 적혀있음
  * group.id - consumer.properties 에 적혀있음
  * topic - 각자 지정한 것으로 전달하면 됨, 예제는 TEST를 기준으로 실행함    
    ```
    $ cd ~/librdkafka/examples 
    $ ./consumer 0 test-consumer-group TEST
    ```
  * 실행화면
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img17.JPG" width="500px" height="150px" alt="consumer.c"><br/> 