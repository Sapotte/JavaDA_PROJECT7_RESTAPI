package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurveService {
    private static final Logger LOG = LoggerFactory.getLogger(CurveService.class);

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
            LOG.error("Could not find curve point with id: " + id);
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        curvePointRepository.updateCurvePoint(id, term, value);
        LOG.info("Updated curve point with id: " + id);
    }

    public void deleteCurvePoint(Integer id) {
        if(!curvePointRepository.existsById(id)) {
            LOG.error("Could not find curve point with id: " + id);
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        curvePointRepository.deleteById(id);
        LOG.info("Deleted curve point with id: " + id);
    }
}
