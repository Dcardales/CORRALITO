package com.tecno.corralito.models.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ErrorResponse {
    private int statusCode;
    private String message;

}
