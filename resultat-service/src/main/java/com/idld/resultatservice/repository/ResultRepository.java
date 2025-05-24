package com.idld.resultatservice.repository;

import com.idld.resultatservice.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(long studentId);

    List<Result> findByCourseId(long courseId);

    public Result findByStudentIdAndCourseId(Long studentId, Long courseId);


}
