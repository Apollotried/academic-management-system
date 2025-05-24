package com.idld.notificationservice.Mapper;


import com.idld.notificationservice.dtos.RequestEmailDto;
import com.idld.notificationservice.dtos.ResponseEmailDto;
import com.idld.notificationservice.entities.Email;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImpl implements EmailMapper {

    @Override
    public Email toEntity(RequestEmailDto requestEmailDto) {
        Email email = new Email();
        BeanUtils.copyProperties(requestEmailDto, email);
        email.setStatus("PENDING");
        return email;
    }

    @Override
    public ResponseEmailDto toDto(Email email) {
        ResponseEmailDto responseEmailDto = new ResponseEmailDto();
        BeanUtils.copyProperties(email, responseEmailDto);
        return responseEmailDto;
    }
}

