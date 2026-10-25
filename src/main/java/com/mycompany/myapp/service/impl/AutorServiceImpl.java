package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Autor;
import com.mycompany.myapp.repository.AutorRepository;
import com.mycompany.myapp.service.AutorService;
import com.mycompany.myapp.service.dto.AutorDTO;
import com.mycompany.myapp.service.mapper.AutorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Autor}.
 */
@Service
@Transactional
public class AutorServiceImpl implements AutorService {

    private static final Logger LOG = LoggerFactory.getLogger(AutorServiceImpl.class);

    private final AutorRepository autorRepository;

    private final AutorMapper autorMapper;

    public AutorServiceImpl(AutorRepository autorRepository, AutorMapper autorMapper) {
        this.autorRepository = autorRepository;
        this.autorMapper = autorMapper;
    }

    @Override
    public AutorDTO save(AutorDTO autorDTO) {
        LOG.debug("Request to save Autor : {}", autorDTO);
        Autor autor = autorMapper.toEntity(autorDTO);
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }

    @Override
    public AutorDTO update(AutorDTO autorDTO) {
        LOG.debug("Request to update Autor : {}", autorDTO);
        Autor autor = autorMapper.toEntity(autorDTO);
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }

    @Override
    public Optional<AutorDTO> partialUpdate(AutorDTO autorDTO) {
        LOG.debug("Request to partially update Autor : {}", autorDTO);

        return autorRepository
            .findById(autorDTO.getId())
            .map(existingAutor -> {
                autorMapper.partialUpdate(existingAutor, autorDTO);

                return existingAutor;
            })
            .map(autorRepository::save)
            .map(autorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AutorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Autors");
        return autorRepository.findAll(pageable).map(autorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutorDTO> findOne(Long id) {
        LOG.debug("Request to get Autor : {}", id);
        return autorRepository.findById(id).map(autorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Autor : {}", id);
        autorRepository.deleteById(id);
    }
}
