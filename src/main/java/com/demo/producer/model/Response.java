package com.demo.producer.model;

import lombok.Data;

@Data
public class Response {
    private int statusCode;
    private Object message;
}
