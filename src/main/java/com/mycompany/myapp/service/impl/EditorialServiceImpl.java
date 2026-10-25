package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Editorial;
import com.mycompany.myapp.repository.EditorialRepository;
import com.mycompany.myapp.service.EditorialService;
import com.mycompany.myapp.service.dto.EditorialDTO;
import com.mycompany.myapp.service.mapper.EditorialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Editorial}.
 */
@Service
@Transactional
public class EditorialServiceImpl implements EditorialService {

    private static final Logger LOG = LoggerFactory.getLogger(EditorialServiceImpl.class);

    private final EditorialRepository editorialRepository;

    private final EditorialMapper editorialMapper;

    public EditorialServiceImpl(EditorialRepository editorialRepository, EditorialMapper editorialMapper) {
        this.editorialRepository = editorialRepository;
        this.editorialMapper = editorialMapper;
    }

    @Override
    public EditorialDTO save(EditorialDTO editorialDTO) {
        LOG.debug("Request to save Editorial : {}", editorialDTO);
        Editorial editorial = editorialMapper.toEntity(editorialDTO);
        editorial = editorialRepository.save(editorial);
        return editorialMapper.toDto(editorial);
    }

    @Override
    public EditorialDTO update(EditorialDTO editorialDTO) {
        LOG.debug("Request to update Editorial : {}", editorialDTO);
        Editorial editorial = editorialMapper.toEntity(editorialDTO);
        editorial = editorialRepository.save(editorial);
        return editorialMapper.toDto(editorial);
    }

    @Override
    public Optional<EditorialDTO> partialUpdate(EditorialDTO editorialDTO) {
        LOG.debug("Request to partially update Editorial : {}", editorialDTO);

        return editorialRepository
            .findById(editorialDTO.getId())
            .map(existingEditorial -> {
                editorialMapper.partialUpdate(existingEditorial, editorialDTO);

                return existingEditorial;
            })
            .map(editorialRepository::save)
            .map(editorialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EditorialDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Editorials");
        return editorialRepository.findAll(pageable).map(editorialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EditorialDTO> findOne(Long id) {
        LOG.debug("Request to get Editorial : {}", id);
        return editorialRepository.findById(id).map(editorialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Editorial : {}", id);
        editorialRepository.deleteById(id);
    }
}
