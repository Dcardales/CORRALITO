package com.tecno.corralito.models.response.general;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ErrorResponse implements Serializable {
    private int statusCode;
    private String message;

}
