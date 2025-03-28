package br.com.jatai.consumer;

import br.com.jatai.adapter.MessageAdapter;
import br.com.jatai.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GsonDeserializer implements Deserializer<Message> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageAdapter())
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) -> {
                String dateTimeString = json.getAsString();
                return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            })
            .create();

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), Message.class);
    }
}
