package br.com.jatai.model;

import java.time.LocalDateTime;

public class Message<T> {

    private final CorrelationId id;
    private final T payload;
    private final LocalDateTime dateTime;
    private final String comments;

    public Message(CorrelationId id, T payload, String comments, LocalDateTime dateTime){
        this.id = id;
        this.payload = payload;
        this.dateTime = dateTime;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", payload=" + payload +
                ", dateTime=" + dateTime +
                ", comments='" + comments + '\'' +
                '}';
    }

    public T getPayload() {
        return payload;
    }

    public CorrelationId getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
