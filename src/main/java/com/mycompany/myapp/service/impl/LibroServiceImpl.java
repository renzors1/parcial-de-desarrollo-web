package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.repository.LibroRepository;
import com.mycompany.myapp.service.LibroService;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.mapper.LibroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Libro}.
 */
@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private static final Logger LOG = LoggerFactory.getLogger(LibroServiceImpl.class);

    private final LibroRepository libroRepository;

    private final LibroMapper libroMapper;

    public LibroServiceImpl(LibroRepository libroRepository, LibroMapper libroMapper) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    @Override
    public LibroDTO save(LibroDTO libroDTO) {
        LOG.debug("Request to save Libro : {}", libroDTO);
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);
        return libroMapper.toDto(libro);
    }

    @Override
    public LibroDTO update(LibroDTO libroDTO) {
        LOG.debug("Request to update Libro : {}", libroDTO);
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);
        return libroMapper.toDto(libro);
    }

    @Override
    public Optional<LibroDTO> partialUpdate(LibroDTO libroDTO) {
        LOG.debug("Request to partially update Libro : {}", libroDTO);

        return libroRepository
            .findById(libroDTO.getId())
            .map(existingLibro -> {
                libroMapper.partialUpdate(existingLibro, libroDTO);

                return existingLibro;
            })
            .map(libroRepository::save)
            .map(libroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LibroDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Libros");
        return libroRepository.findAll(pageable).map(libroMapper::toDto);
    }

    public Page<LibroDTO> findAllWithEagerRelationships(Pageable pageable) {
        return libroRepository.findAllWithEagerRelationships(pageable).map(libroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LibroDTO> findOne(Long id) {
        LOG.debug("Request to get Libro : {}", id);
        return libroRepository.findOneWithEagerRelationships(id).map(libroMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Libro : {}", id);
        libroRepository.deleteById(id);
    }
}
