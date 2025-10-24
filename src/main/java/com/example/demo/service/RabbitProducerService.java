package com.example.demo.service;

import com.example.demo.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitProducerService {
    private final RabbitTemplate rabbitTemplate;

    private final MessageService messageService;

    @Value("${spring.rabbitmq.template.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String ROUTING_KEY;


    public void sendSequential(Long messageCount, boolean isBulk) {
        long start = System.currentTimeMillis();

        MessageDto message = MessageDto.builder()
                .content(isBulk ? messageService.getBulkMessage() : messageService.getMessage())
                .totalStreamCount(messageCount)
                .build();
        for (int i = 0; i < messageCount; i++) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        }

        long end = System.currentTimeMillis();
        log.info("Sequential ::: is bulk={}  => Sent {} messages in {} ms", isBulk, messageCount , (end - start));
    }

    public void sendParallel(Long messageCount, boolean isBulk) {
        long start = System.currentTimeMillis();

        MessageDto message = MessageDto.builder()
                .content(isBulk ? messageService.getBulkMessage() : messageService.getMessage())
                .totalStreamCount(messageCount)
                .build();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        LongStream.range(0, messageCount).forEach(i ->
                executor.submit(() ->
                        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message)
                )
        );
        executor.shutdown();
        while (!executor.isTerminated());

        long end = System.currentTimeMillis();
        log.info("Parallel::: is bulk={}  => Sent {} messages in {} ms", isBulk, messageCount , (end - start));
    }

}
