package com.idld.inscriptionservice.Mapper;

import com.idld.inscriptionservice.DTOs.RequestInscriptionDTO;
import com.idld.inscriptionservice.DTOs.ResponseInscriptionDTO;
import com.idld.inscriptionservice.Entity.Inscription;
import org.hibernate.annotations.Cache;
import org.springframework.stereotype.Component;

@Component
public interface InscriptionMapperInterface {
    public ResponseInscriptionDTO toDto(Inscription inscription);
    public Inscription toEntity(RequestInscriptionDTO requestInscriptionDTO);
}