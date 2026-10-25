package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CalificacionUsuario;
import com.mycompany.myapp.repository.CalificacionUsuarioRepository;
import com.mycompany.myapp.service.CalificacionUsuarioService;
import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
import com.mycompany.myapp.service.mapper.CalificacionUsuarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.CalificacionUsuario}.
 */
@Service
@Transactional
public class CalificacionUsuarioServiceImpl implements CalificacionUsuarioService {

    private static final Logger LOG = LoggerFactory.getLogger(CalificacionUsuarioServiceImpl.class);

    private final CalificacionUsuarioRepository calificacionUsuarioRepository;

    private final CalificacionUsuarioMapper calificacionUsuarioMapper;

    public CalificacionUsuarioServiceImpl(
        CalificacionUsuarioRepository calificacionUsuarioRepository,
        CalificacionUsuarioMapper calificacionUsuarioMapper
    ) {
        this.calificacionUsuarioRepository = calificacionUsuarioRepository;
        this.calificacionUsuarioMapper = calificacionUsuarioMapper;
    }

    @Override
    public CalificacionUsuarioDTO save(CalificacionUsuarioDTO calificacionUsuarioDTO) {
        LOG.debug("Request to save CalificacionUsuario : {}", calificacionUsuarioDTO);
        CalificacionUsuario calificacionUsuario = calificacionUsuarioMapper.toEntity(calificacionUsuarioDTO);
        calificacionUsuario = calificacionUsuarioRepository.save(calificacionUsuario);
        return calificacionUsuarioMapper.toDto(calificacionUsuario);
    }

    @Override
    public CalificacionUsuarioDTO update(CalificacionUsuarioDTO calificacionUsuarioDTO) {
        LOG.debug("Request to update CalificacionUsuario : {}", calificacionUsuarioDTO);
        CalificacionUsuario calificacionUsuario = calificacionUsuarioMapper.toEntity(calificacionUsuarioDTO);
        calificacionUsuario = calificacionUsuarioRepository.save(calificacionUsuario);
        return calificacionUsuarioMapper.toDto(calificacionUsuario);
    }

    @Override
    public Optional<CalificacionUsuarioDTO> partialUpdate(CalificacionUsuarioDTO calificacionUsuarioDTO) {
        LOG.debug("Request to partially update CalificacionUsuario : {}", calificacionUsuarioDTO);

        return calificacionUsuarioRepository
            .findById(calificacionUsuarioDTO.getId())
            .map(existingCalificacionUsuario -> {
                calificacionUsuarioMapper.partialUpdate(existingCalificacionUsuario, calificacionUsuarioDTO);

                return existingCalificacionUsuario;
            })
            .map(calificacionUsuarioRepository::save)
            .map(calificacionUsuarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalificacionUsuarioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CalificacionUsuarios");
        return calificacionUsuarioRepository.findAll(pageable).map(calificacionUsuarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalificacionUsuarioDTO> findOne(Long id) {
        LOG.debug("Request to get CalificacionUsuario : {}", id);
        return calificacionUsuarioRepository.findById(id).map(calificacionUsuarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CalificacionUsuario : {}", id);
        calificacionUsuarioRepository.deleteById(id);
    }
}
