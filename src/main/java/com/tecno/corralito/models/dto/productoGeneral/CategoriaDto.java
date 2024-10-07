package com.tecno.corralito.models.dto.productoGeneral;

import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto implements Serializable {

    private Integer idCategoia;
    private String nombreCategoria;

    public CategoriaDto(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public static CategoriaDto fromEntity(Categoria entity) {
        if (entity == null) {
            return null;
        }

        return CategoriaDto.builder()
                .idCategoia(entity.getIdCategoia())
                .nombreCategoria(entity.getNombreCategoria())
                .build();
    }
}