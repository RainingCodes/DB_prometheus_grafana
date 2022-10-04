# java로 kafka producer 만들기

## STEP 1 eclipse 설치
* https://www.eclipse.org/downloads/ 
* eclipse-inst-jre-linux64.tar.gz 가 다운받아짐
* 압축 해제 후 실행
```
$ tar -zxvf eclipse-inst-jre-linux64.tar.gz
$ cd eclipse-inst.ini
$ ./eclipse-insi
```
 * Eclipse IDE for Java Developers 선택후 설치 
   <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img4.png" width="300px" height="500px" alt="eclipse 1"></img><br/>
 * eclipse란 폴더가 생기며 이동후 실행
  ```
  $ cd ~/eclipse/java-2022-09/eclipse
  $ ./eclipse
  ```
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img5.png" width="1000px" height="800px" alt="eclipse 2"></img><br/>


## STEP 3 maven project 만들기
* https://bong-sik.tistory.com/5 참고
* 처음에 나오는 Welcome 창은 없앰
* Create a project...
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img6.png" width="1000px" height="800px" alt="maven1"></img><br/>
* Maven>Maven Project, Next
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img7.png" width="500px" height="700px" alt="maven2"></img><br/>
* Use default Workspace location, Next
* org.apache.tapestry / quickstart 선택
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img8.png" width="1000px" height="800px" alt="maven3"></img><br/>
* Group Id : kafkaProducer, Artifact Id : producer, Finish
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img9.png" width="1000px" height="800px" alt="maven4"></img><br/>
* Console창에 Y 입력 후 Enter
  <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img10.png" width="1000px" height="800px" alt="maven5"></img><br/>
* https://kafka.apache.org/documentation/#apis 를 참고해서 pom.xml에 추가
  ```
  <dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.2.1</version>
  </dependency>
  <dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-streams</artifactId>
	<version>3.2.1</version>
  </dependency>
  <dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.2.1</version>
  </dependency>
  ```
    <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img11.png" width="1000px" height="800px" alt="maven6"></img><br/>

## STEP 4 Producer.java
 * Topic은 이미 만들어진 상태라고 가정 (<a src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/start.md#step3--topic-%EC%83%9D%EC%84%B1">topic 생성</a>)
 * producer/src/test/java 우클릭 > New > Class 클릭
<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img12.JPG" width="300px" height="200px" alt="producer.java1"></img><br/>

 * package 선택 후 Name : Producer 입력, main 함수 자동 완성 click
 <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img13.JPG" width="300px" height="500px" alt="producer.java2"></img><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img14.JPG" width="300px" height="500px" alt="producer.java3"></img><br/> 
 
 * 5개의 랜덤 숫자를 전송하는 Producer 프로그램 작성
```
//static 변수 선언
private static final String TOPIC_NAME = "TEST"; 

//main 함수 부분, throws InterruptedException 추가 필요
Random random = new Random();
        
Properties prop = new Properties();
prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
 

String message = null;

// producer 생성
KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

// sending message 5 times 
for(int i = 0; i < 5; i++) {

      message = Integer.toString(random.nextInt(100)); // 1~100 중 랜덤숫자
      producer.send(new ProducerRecord<String,String>(TOPIC_NAME, message));
      System.out.println("message sent from Producer.java : " + message);
      Thread.sleep(1000); // 1초

}
```

## STEP 4-2 Producer.java version2
  * TEST, TEST2 두 토픽에 각각 랜덤한 숫자 5개를 생성하는 프로그램 작성
  ```
package kafkaProducer.producer;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {
	private static final String[] TOPIC_NAMES = {"TEST", "TEST2"}; 
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Random random = new Random();
        
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
 
        String message = null;

        // producer 생성
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        // sending message 5 times 
        for (int i = 0; i < TOPIC_NAMES.length; i++) {
        	for(int j = 0; j < 5; j++) {
                message = Integer.toString(random.nextInt(100)); // 1~100 중 랜덤숫자
                producer.send(new ProducerRecord<String,String>(TOPIC_NAMES[i], message));
                System.out.println("message sent from Producer.java : " + message + ", TOPIC : " + TOPIC_NAMES[i]);
                Thread.sleep(1000); // 1초
            }
        }
        
	}
}
  ```
  
  * 실행화면
 <br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img15.JPG" width="1000px" height="800px" alt="producer.java4"><br/>


## STEP 5 message 확인
* kafka도 켜 두어야 함 (<a src="bin/kafka-console-consumer.sh --topic TEST --from-beginning --bootstrap-server localhost:9092"> kafka 실행</a>)
```
$ bin/kafka-console-consumer.sh --topic TEST --from-beginning --bootstrap-server localhost:9092
```

<br><img src="https://github.com/RainingCodes/Industry-Academic-Cooperation1/blob/main/Kafka/img/img16.JPG" width="800px" height="100px" alt="producer.java6"><br/> 