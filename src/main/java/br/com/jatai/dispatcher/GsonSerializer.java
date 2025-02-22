package br.com.jatai.dispatcher;

import br.com.jatai.adapter.LocalDateTimeAdapter;
import br.com.jatai.adapter.MessageAdapter;
import br.com.jatai.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.time.LocalDateTime;

public class GsonSerializer implements Serializer<Message> {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public byte[] serialize(String s, Message t) {
        return gson.toJson(t).getBytes();
    }

}
