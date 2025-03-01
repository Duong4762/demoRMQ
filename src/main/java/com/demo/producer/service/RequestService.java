package com.demo.producer.service;

import com.demo.producer.exception.InvalidException;
import com.demo.producer.exception.StatusCode;
import com.demo.producer.model.RequestDto;
import com.demo.producer.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class RequestService {
    @Autowired
    private RedisTemplate<String, RequestDto> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public Response sendMessage(RequestDto requestDto) {
        log.info("Received from API: {}", requestDto);
        Response response = new Response();
        validateRequest(requestDto);
        String message = null;
        try {
            message = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            log.error("Loi khi chuyen du lieu thanh string ", e);
            throw new RuntimeException("Loi khi chuyen du lieu thanh string");
        }
        log.info("Sending message: {}", message);
        Object responseFromQueue = rabbitTemplate.convertSendAndReceive("payment", "payment.create", message);
        log.info("Sent completely");
        response.setStatusCode(StatusCode.COMPLETELY.getCode());
        response.setMessage(responseFromQueue);
        return response;
    }
    public void validateRequest(RequestDto requestDto) {
        log.info("Validating request: {}", requestDto);
        if(requestDto.getRealAmount() > requestDto.getDebitAmount()) {
            log.info("Real amount is greater than debit amount");
            throw new InvalidException("Real amount khong duoc lon hon debit amount");
        }
        if(requestDto.getRealAmount() != requestDto.getDebitAmount()){
            if(requestDto.getPromotionCode()==null|| requestDto.getPromotionCode().trim().equals("")) {
                log.info("Promotion code is empty");
                throw new InvalidException("Khong duoc de trong promotion code");
            }
        }
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        if(hashOps.hasKey("requests", requestDto.getTokenKey())){
            log.info("Token exists");
            throw new InvalidException("Token exists");
        } else {
            log.info("Token not exists");
        }
        log.info("Request has been validated");
    }
}
