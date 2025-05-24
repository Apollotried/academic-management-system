package com.idld.inscriptionservice.repository;

import com.idld.inscriptionservice.Entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Inscription i WHERE i.studentId = :studentId")
    void deleteByStudentId(Long studentId);

    @Query("SELECT i.courseId FROM Inscription i WHERE i.studentId = :studentId")
    List<Long> findCourseIdsByStudentId(Long studentId);

    @Query("SELECT i FROM Inscription i WHERE i.courseId = :courseId")
    List<Inscription> findByCourseId(Long courseId);


}
