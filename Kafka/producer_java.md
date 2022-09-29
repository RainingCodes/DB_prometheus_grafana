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
```
```

## STEP 5 message 확인
