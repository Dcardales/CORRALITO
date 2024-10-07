package com.tecno.corralito.models.dto.productoGeneral;



import com.tecno.corralito.models.entity.productoGeneral.Zona;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaDto implements Serializable {

    private Integer idZona;
    private String nombreZona;

    public ZonaDto(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    // ZonaDTO.java

    public static ZonaDto fromEntity(Zona entity) {
        if (entity == null) {
            return null;
        }

        return ZonaDto.builder()
                .idZona(entity.getIdZona())
                .nombreZona(entity.getNombreZona())
                .build();
    }
}
