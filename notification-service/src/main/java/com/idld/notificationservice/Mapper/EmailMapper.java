package com.idld.notificationservice.Mapper;


import com.idld.notificationservice.dtos.RequestEmailDto;
import com.idld.notificationservice.dtos.ResponseEmailDto;
import com.idld.notificationservice.entities.Email;

public interface EmailMapper {

    Email toEntity(RequestEmailDto requestEmailDto);

    ResponseEmailDto toDto(Email email);
}

