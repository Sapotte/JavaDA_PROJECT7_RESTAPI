package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE CurvePoint SET term = :term, value = :value WHERE id = :id")
    void updateCurvePoint(Integer id, Double term, Double value);
}
