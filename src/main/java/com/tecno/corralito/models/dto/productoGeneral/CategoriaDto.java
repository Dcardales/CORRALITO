package com.tecno.corralito.models.dto.productoGeneral;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoriaDto {

    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombreCategoria;


}
