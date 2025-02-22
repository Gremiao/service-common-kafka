package br.com.jatai.dispatcher;

import br.com.jatai.model.CorrelationId;
import br.com.jatai.model.Message;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaDispatcher<T> implements Closeable {

    private final KafkaProducer<String, Message<T>> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.104:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    public void send(String topic, String key, CorrelationId correlationId, T payload, String comments, LocalDateTime dateTime) throws ExecutionException, InterruptedException {
        var future = sendAsync(topic, key, correlationId, payload, comments, dateTime);
        future.get();
    }

    public Future<RecordMetadata> sendAsync(String topic, String key, CorrelationId correlationId, T payload, String comments, LocalDateTime dateTime) {
        var value = new Message<>(correlationId.continueWith("_"+ topic), payload, comments, dateTime);
        var record = new ProducerRecord<>(topic, key, value);

        Callback callback = (data, ex) -> {
            if(ex != null){
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando: "+data.topic()+":::partition "+ data.partition()+" offset "+data.offset()+" / timestamp "+data.timestamp());
        };
        return producer.send(record, callback);
    }

    @Override
    public void close(){
        producer.close();
    }
}
