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
