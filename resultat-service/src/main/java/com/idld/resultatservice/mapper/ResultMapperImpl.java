package com.idld.resultatservice.mapper;

import com.idld.resultatservice.Dtos.ResultDTORequest;
import com.idld.resultatservice.Dtos.ResultDto;
import com.idld.resultatservice.entities.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ResultMapperImpl implements ResultMapperInterf{
    @Override
    public ResultDto resultToResultDto(Result result) {
        if (result==null){
            return null;
        }
        ResultDto resultDto = new ResultDto();
        BeanUtils.copyProperties(result, resultDto);

        return resultDto;
    }

    @Override
    public Result resultDtoToResult(ResultDTORequest resultDto) {
        if (resultDto==null){
            return null;
        }
        Result result = new Result();
        BeanUtils.copyProperties(resultDto, result);
        return result;

    }
}
