package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoEspecifico.IProductoEspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto-especifico")
public class ProductoEspController {

}