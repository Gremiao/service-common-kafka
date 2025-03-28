package br.com.jatai.adapter;

import br.com.jatai.model.CorrelationId;
import br.com.jatai.model.Message;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", message.getPayload().getClass().getName());
        obj.add("payload", context.serialize(message.getPayload()));
        obj.add("correlationId", context.serialize(message.getId()));
        obj.addProperty("comments", message.getComments());
        obj.add("dateTime", context.serialize(message.getDateTime()));
        return obj;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        var obj = jsonElement.getAsJsonObject();
        CorrelationId correlationId = context.deserialize(obj.get("correlationId"), CorrelationId.class);
        String payload = new Gson().toJson(obj.get("payload"));
        String comments = obj.get("comments").getAsString();
        LocalDateTime dateTime = context.deserialize(obj.get("dateTime"), LocalDateTime.class);

        return new Message<>(correlationId, payload, comments, dateTime);
    }
}
