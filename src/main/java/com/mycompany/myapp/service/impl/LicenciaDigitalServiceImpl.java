package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.LicenciaDigital;
import com.mycompany.myapp.repository.LicenciaDigitalRepository;
import com.mycompany.myapp.service.LicenciaDigitalService;
import com.mycompany.myapp.service.dto.LicenciaDigitalDTO;
import com.mycompany.myapp.service.mapper.LicenciaDigitalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.LicenciaDigital}.
 */
@Service
@Transactional
public class LicenciaDigitalServiceImpl implements LicenciaDigitalService {

    private static final Logger LOG = LoggerFactory.getLogger(LicenciaDigitalServiceImpl.class);

    private final LicenciaDigitalRepository licenciaDigitalRepository;

    private final LicenciaDigitalMapper licenciaDigitalMapper;

    public LicenciaDigitalServiceImpl(LicenciaDigitalRepository licenciaDigitalRepository, LicenciaDigitalMapper licenciaDigitalMapper) {
        this.licenciaDigitalRepository = licenciaDigitalRepository;
        this.licenciaDigitalMapper = licenciaDigitalMapper;
    }

    @Override
    public LicenciaDigitalDTO save(LicenciaDigitalDTO licenciaDigitalDTO) {
        LOG.debug("Request to save LicenciaDigital : {}", licenciaDigitalDTO);
        LicenciaDigital licenciaDigital = licenciaDigitalMapper.toEntity(licenciaDigitalDTO);
        licenciaDigital = licenciaDigitalRepository.save(licenciaDigital);
        return licenciaDigitalMapper.toDto(licenciaDigital);
    }

    @Override
    public LicenciaDigitalDTO update(LicenciaDigitalDTO licenciaDigitalDTO) {
        LOG.debug("Request to update LicenciaDigital : {}", licenciaDigitalDTO);
        LicenciaDigital licenciaDigital = licenciaDigitalMapper.toEntity(licenciaDigitalDTO);
        licenciaDigital = licenciaDigitalRepository.save(licenciaDigital);
        return licenciaDigitalMapper.toDto(licenciaDigital);
    }

    @Override
    public Optional<LicenciaDigitalDTO> partialUpdate(LicenciaDigitalDTO licenciaDigitalDTO) {
        LOG.debug("Request to partially update LicenciaDigital : {}", licenciaDigitalDTO);

        return licenciaDigitalRepository
            .findById(licenciaDigitalDTO.getId())
            .map(existingLicenciaDigital -> {
                licenciaDigitalMapper.partialUpdate(existingLicenciaDigital, licenciaDigitalDTO);

                return existingLicenciaDigital;
            })
            .map(licenciaDigitalRepository::save)
            .map(licenciaDigitalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LicenciaDigitalDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LicenciaDigitals");
        return licenciaDigitalRepository.findAll(pageable).map(licenciaDigitalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LicenciaDigitalDTO> findOne(Long id) {
        LOG.debug("Request to get LicenciaDigital : {}", id);
        return licenciaDigitalRepository.findById(id).map(licenciaDigitalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LicenciaDigital : {}", id);
        licenciaDigitalRepository.deleteById(id);
    }
}
