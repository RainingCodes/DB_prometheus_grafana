package kafkaProducer.producer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {
	private static final String TOPIC= "LINEITEM"; 
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		FileReader reader = new FileReader("/home/user1/TPC-H V3.0.1/dbgen/lineitem.tbl");
		BufferedReader br = new BufferedReader(reader);
        
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
 
        String message = null;

        // producer 생성
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        
        //while((message = br.readLine()) != null) {
       for(int i = 0; i < 100; i++) {			//to test with small data
    	   message = br.readLine();
    	   producer.send(new ProducerRecord<String,String>(TOPIC, message));
        	System.out.println("message sent from Producer.java : " + message + ", TOPIC : " + TOPIC);
        	Thread.sleep(1000); // 1초
        }
        
	}
}
