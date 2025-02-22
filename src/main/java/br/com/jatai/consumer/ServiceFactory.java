package br.com.jatai.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
