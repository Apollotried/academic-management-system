package com.idld.resultatservice.mapper;

import com.idld.resultatservice.Dtos.ResultDTORequest;
import com.idld.resultatservice.Dtos.ResultDto;
import com.idld.resultatservice.entities.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ResultMapperImplTest {
    private final ResultMapperImpl resultMapper = new ResultMapperImpl();


    @Test
    void testToResultDto_ShouldMapCorrectly() {
        Result result = new Result();
        result.setStudentId(2L);
        result.setCourseId(3L);
        result.setGrade(4);

        ResultDto resultDto = resultMapper.resultToResultDto(result);

        assertNotNull(resultDto);
        assertEquals(result.getStudentId(), resultDto.getStudentId());
        assertEquals(result.getCourseId(), resultDto.getCourseId());
        assertEquals(result.getGrade(), resultDto.getGrade());

    }

    @Test
    void testToResultDto_NullInput_ShouldReturnNull() {
        ResultDto resultDto = resultMapper.resultToResultDto(null);

        assertNull(resultDto);
    }

}