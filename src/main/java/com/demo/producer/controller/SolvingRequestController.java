package com.demo.producer.controller;

import com.demo.producer.model.RequestDto;
import com.demo.producer.model.Response;
import com.demo.producer.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SolvingRequestController {
    @Autowired
    private RequestService requestService;
    @PostMapping
    public Response createPayment(@Valid @RequestBody RequestDto requestDto) {
        return requestService.sendMessage(requestDto);
    }
}
