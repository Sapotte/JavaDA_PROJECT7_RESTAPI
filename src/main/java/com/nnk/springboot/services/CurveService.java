package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveService {
    @Autowired
    CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    public CurvePoint findCurvePointById(int id) {
        return curvePointRepository.findById(id).orElseThrow(()-> new RuntimeException("Could not find curve point with id: " + id));
    }

    public CurvePoint createCurvePoint(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public void updateCurvePoint(Integer id, Double term, Double value) {
        if(!curvePointRepository.existsById(id)) {
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        curvePointRepository.updateCurvePoint(id, term, value);
    }

    public void deleteCurvePoint(Integer id) {
        if(!curvePointRepository.existsById(id)) {
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        curvePointRepository.deleteById(id);
    }
}
