package com.idld.resultatservice.mapper;

import com.idld.resultatservice.Dtos.ResultDTORequest;
import com.idld.resultatservice.Dtos.ResultDto;
import com.idld.resultatservice.entities.Result;
import org.springframework.stereotype.Component;

@Component
public interface ResultMapperInterf {
    public ResultDto resultToResultDto(Result result);

    public Result resultDtoToResult(ResultDTORequest resultDto);
}
