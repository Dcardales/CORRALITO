package com.tecno.corralito.models.response.usuarios.comercio;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateComercioRequest {

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    @Size(max = 100, message = "El nombre del comercio no puede exceder los 100 caracteres")
    private String nombreComercio;

    @NotBlank(message = "El NIT no puede estar vacío")
    @Size(max = 20, message = "El NIT no puede exceder los 20 caracteres")
    private String nit;

    @Size(max = 100, message = "La razón social no puede exceder los 100 caracteres")
    private String razonSocial;

    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    private String direccionComercio;

    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    private String telefono;

}
