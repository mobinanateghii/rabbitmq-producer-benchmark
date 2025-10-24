package com.example.demo.service;

import com.example.demo.service.generator.RandomNameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RandomNameGenerator randomNameGenerator;
    private final Long DEFAULT_COUNT = 5L;
    private final Long DEFAULT_BULK_COUNT = 5000L;

    public String getMessage(){
        return randomNameGenerator.generate(DEFAULT_COUNT).toString();
    }

    public String getBulkMessage() {
        return randomNameGenerator.generate(DEFAULT_BULK_COUNT).toString();
    }
}
