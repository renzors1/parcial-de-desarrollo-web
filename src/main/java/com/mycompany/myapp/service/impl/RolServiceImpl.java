package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.repository.RolRepository;
import com.mycompany.myapp.service.RolService;
import com.mycompany.myapp.service.dto.RolDTO;
import com.mycompany.myapp.service.mapper.RolMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Rol}.
 */
@Service
@Transactional
public class RolServiceImpl implements RolService {

    private static final Logger LOG = LoggerFactory.getLogger(RolServiceImpl.class);

    private final RolRepository rolRepository;

    private final RolMapper rolMapper;

    public RolServiceImpl(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    @Override
    public RolDTO save(RolDTO rolDTO) {
        LOG.debug("Request to save Rol : {}", rolDTO);
        Rol rol = rolMapper.toEntity(rolDTO);
        rol = rolRepository.save(rol);
        return rolMapper.toDto(rol);
    }

    @Override
    public RolDTO update(RolDTO rolDTO) {
        LOG.debug("Request to update Rol : {}", rolDTO);
        Rol rol = rolMapper.toEntity(rolDTO);
        rol = rolRepository.save(rol);
        return rolMapper.toDto(rol);
    }

    @Override
    public Optional<RolDTO> partialUpdate(RolDTO rolDTO) {
        LOG.debug("Request to partially update Rol : {}", rolDTO);

        return rolRepository
            .findById(rolDTO.getId())
            .map(existingRol -> {
                rolMapper.partialUpdate(existingRol, rolDTO);

                return existingRol;
            })
            .map(rolRepository::save)
            .map(rolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RolDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Rols");
        return rolRepository.findAll(pageable).map(rolMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RolDTO> findOne(Long id) {
        LOG.debug("Request to get Rol : {}", id);
        return rolRepository.findById(id).map(rolMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Rol : {}", id);
        rolRepository.deleteById(id);
    }
}
