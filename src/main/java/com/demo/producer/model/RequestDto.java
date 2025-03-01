package com.demo.producer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RequestDto {
    @NotBlank
    private String tokenKey;
    @NotBlank
    private String apiID;
    @NotBlank
    private String mobile;
    private String bankCode = "970445";
    @NotBlank
    private String accountNo;
    @NotBlank
    @Pattern(regexp = "^\\d{14}$", message = "Ngày tháng phải có định dạng yyyyMMddHHmmss")
    private String payDate;
    private String additionalData;
    private long debitAmount;
    @NotBlank
    private String respCode;
    @NotBlank
    private String respDesc;
    @NotBlank
    private String traceTransfer;
    private String messageType = "1";
    @NotBlank
    private String checkSum;
    @NotBlank
    private String orderCode;
    @NotBlank
    private String userName;
    private long realAmount;
    private String promotionCode;
    private final String addValue = "{\"payMethod\":\"01\",\"payMethodMMS\":1}";
}
