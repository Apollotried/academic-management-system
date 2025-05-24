package com.idld.inscriptionservice.Mapper;

import com.idld.inscriptionservice.DTOs.RequestInscriptionDTO;
import com.idld.inscriptionservice.DTOs.ResponseInscriptionDTO;
import com.idld.inscriptionservice.Entity.Inscription;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class InscriptionMapper implements InscriptionMapperInterface {

    @Override
    public Inscription toEntity(RequestInscriptionDTO requestInscriptionDTO) {
        if (requestInscriptionDTO == null) {
            return null;
        }
        Inscription inscription = new Inscription();
        BeanUtils.copyProperties(requestInscriptionDTO, inscription);
        return inscription;
    }

    @Override
    public ResponseInscriptionDTO toDto(Inscription inscription) {
        if (inscription == null) {
            return null;
        }
        ResponseInscriptionDTO responseDTO = new ResponseInscriptionDTO();
        BeanUtils.copyProperties(inscription, responseDTO);
        return responseDTO;
    }
}