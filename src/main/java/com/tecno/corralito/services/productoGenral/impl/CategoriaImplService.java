package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.repository.productoGeneral.CategoriaRepository;
import com.tecno.corralito.services.productoGenral.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoriaImplService implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaImplService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> listAll() {
        Iterable<Categoria> iterable = categoriaRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Categoria save(CategoriaDto categoriaDto) {

        if (categoriaDto.getIdCategoia() != null && existsById(categoriaDto.getIdCategoia())) {
            Categoria categoriaExistente = findById(categoriaDto.getIdCategoia());
            categoriaExistente.setNombreCategoria(categoriaDto.getNombreCategoria());
            return categoriaRepository.save(categoriaExistente);
        } else {
            Categoria categoriaNueva = Categoria.builder()
                    .nombreCategoria(categoriaDto.getNombreCategoria())
                    .build();
            return categoriaRepository.save(categoriaNueva);
        }

    }

    @Override
    public Categoria findById(Integer id) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
        return optionalCategoria.orElse(null);
    }

    @Override
    public void delete(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }

    @Override
    public boolean existsById(Integer id) {
        return categoriaRepository.existsById(id);
    }
}
