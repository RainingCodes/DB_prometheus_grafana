# Kafka 구축 및 quickstart
- https://kafka.apache.org/quickstart 의 Step을 따라서 실습
- 단일 컴퓨터로 실습

## STEP 1: Kafka 설치
두 가지 방법중 편한 방법을 이용해서 다운받으면 됨, 2번 방법이 속도가 더 빠른 편 
1. https://kafka.apache.org/downloads 에서 다운로드 : 다운로드 받을 버전의 binary downloads 에서 고른 후 압축 해제
2. https://kafka.apache.org/quickstart 의 STEP1:GET KAFKA에 Download를 클릭해서 나오는 사이트에 sugget된 링크를 통해서 다운로드 받을 수 있음 (https://dlcdn.apache.org/kafka/3.2.1/kafka_2.13-3.2.1.tgz)

다운받은 버전 : kafka_2.13-3.2.1 <br>
압축 해제 위치(홈에 해제) : ~/kafka_2.13-3.2.1

## STEP2 : 실행
1. Kafka를 실행하기 전에 Zookeeper를 실행시켜야함
   * 터미널을 열어 kafka 압축을 해제한 폴더로 이동 후 실행
  ```
  $ cd kafka_2.13-3.2.1
  $ bin/zookeeper-server-start.sh config/zookeeper.properties
  ```
2. 새 터미널을 열어 kafka server 실행
 ```
 $ cd kafka_2.13-3.2.1
 $ bin/kafka-server-start.sh config/server.properies
 ```

## STEP3 : TOPIC 생성
일부 설명 발췌 <br>
* Kafka는 분산된 events(records, messages) streaming하는 플랫폼
* events는 topics를 기준으로 조직되고 저장되며 이는 파일시스템의 폴더와 같다.
* events --> file / topics --> foler
* events를 생성하기전에 topic을 먼저 만들어야 한다.
* 새 터미널을 열어 'TEST'라는 토픽을 만들어보자
 ```
 $ cd kafka_2.13-3.2.1
 $ bin/kafka-topics.sh --create --topic TEST --bootstrap-server localhost:9092
 ```

 * kafka-topics.sh를 실행할 때 arguments를 주지 않으면 사용법을 표시함

<img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img1.png" width="500px" height="300px" alt="kafka-topics.sh no args"></img><br/>
  
 * 생성된 topic 상세 정보 확인
 ```
 $ bin/kafka-topics.sh --describe --topic TEST --bootstrap-server localhost:9092
 ```
 <img src="" width="500px" height="400px" alt=""></img><br/>
  

## STEP4 : TOPIC에 EVENTS 작성
일부 설명 발췌<br>
* kafka client는 events를 쓰거나(sender/producer) 읽을 때(receiver, consumer) 네트워크를 통해 Kafka broker와 통신함
*  새로운 터미널을 열어 console producer(kafka client)를 실행하여 TEST topic에 events를 작성
*  작성을 멈추고 싶으면 Ctrl + C를 눌러 종료
```
 $ bin/kafka-console-producer.sh --topic TEST --bootstrap-server localhost:9092
 > 1. first event
 > 2. second event
 > [Ctrl+C] 
```

## STEP5 : 이벤트 읽기
* TEST topic으로 온 events 읽기
* 멈출 때 Ctrl + c 를 눌러 종료 가능
```
$ bin/kafka-console-consumer.sh --topic TEST --from-beginning --bootstrap-server localhost:9092
```
 <img src="" width="500px" height="400px" alt=""></img><br/>
  
