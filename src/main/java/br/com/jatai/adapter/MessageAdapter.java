package br.com.jatai.adapter;

import br.com.jatai.model.CorrelationId;
import br.com.jatai.model.Message;
import com.google.gson.*;
import org.apache.kafka.common.protocol.types.Field;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", message.getPayload().getClass().getName());
        obj.add("payload", context.serialize(message.getPayload()));
        obj.add("correlationId", context.serialize(message.getId()));
        obj.add("comments", context.serialize(message.getComments()));
        obj.add("date", context.serialize(message.getDateTime()));
        return obj;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        var obj = jsonElement.getAsJsonObject();
        var payloadType = obj.get("type").getAsString();
        var correlationId = (CorrelationId) context.deserialize(obj.get("correlationId"), CorrelationId.class);
        var comments = (String) context.deserialize(obj.get("comments"), Field.Str.class);
        var dateTime = (LocalDateTime) context.deserialize(obj.get("dateTime"), Field.Str.class);

        try {
            var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));
            return new Message(correlationId, payload, comments, dateTime);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
