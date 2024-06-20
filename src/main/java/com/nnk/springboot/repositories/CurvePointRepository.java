package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {
    @Modifying
    @Query("UPDATE CurvePoint SET curveId = :curveId, term = :term, value = :value WHERE id = :id")
    void updateCurvePoint(Integer id, Integer curveId, Double term, Double value);
}
