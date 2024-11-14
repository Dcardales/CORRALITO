package com.tecno.corralito.models.response.usuarios.comercio;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ComercioResponseDto implements Serializable {
    private Integer id;
    private String nombreComercio;

}
