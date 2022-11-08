package com.dev.spring.service;

import com.dev.spring.database.repository.CompanyRepository;
import com.dev.spring.dto.CompanyReadDto;
import com.dev.spring.listener.entity.AccessType;
import com.dev.spring.listener.entity.EntityEvent;
import com.dev.spring.mapper.CompanyReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyReadMapper companyReadMapper;

    public List<CompanyReadDto> findAll() {
        return companyRepository.findAll().stream()
                .map(companyReadMapper::map)
                .collect(toList());
    }

    public Optional<CompanyReadDto> findById(Integer id){
       return companyRepository.findById(id)
               .map(entity -> {
                   eventPublisher.publishEvent(new EntityEvent(entity, AccessType.READ));
                   return companyReadMapper.map(entity);
               });
    }
}
