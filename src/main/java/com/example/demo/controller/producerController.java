package com.example.demo.controller;

import com.example.demo.service.RabbitProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages/")
@RequiredArgsConstructor
public class producerController {

    private final RabbitProducerService producerService;


    @PostMapping("/sequential/normal")
    public ResponseEntity<Void> sendNormalMsgSequentially(@RequestParam Long count) {
        try {
            producerService.sendSequential(count, false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error in producing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sequential/bulk")
    public ResponseEntity<Void> sendBulkMessageSequentially(@RequestParam Long count) {
        try {
            producerService.sendSequential(count, true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error in producing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/parallel/normal")
    public ResponseEntity<Void> sendNormalMsgParallel(@RequestParam Long count) {
        try {
            producerService.sendParallel(count, false);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error in producing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/parallel/bulk")
    public ResponseEntity<Void> sendBulkMessageParallel(@RequestParam Long count) {
        try {
            producerService.sendParallel(count, true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error in producing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}

