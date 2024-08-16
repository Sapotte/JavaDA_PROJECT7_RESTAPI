package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurveServiceTest {

    @InjectMocks
    CurveService curveService;

    @Mock
    CurvePointRepository curvePointRepository;

    @Test
    void testFindAllCurvePoints() {
        CurvePoint curvePoint1 = new CurvePoint();
        curvePoint1.setId(1);
        curvePoint1.setTerm(1.5);

        CurvePoint curvePoint2 = new CurvePoint();
        curvePoint2.setId(2);
        curvePoint2.setTerm(2.5);

        List<CurvePoint> testCurvePoints = Arrays.asList(curvePoint1, curvePoint2);

        when(curvePointRepository.findAll()).thenReturn(testCurvePoints);

        List<CurvePoint> resultCurvePoints = curveService.findAllCurvePoints();
        assertEquals(resultCurvePoints.size(), testCurvePoints.size());
        assertEquals(resultCurvePoints.get(0).getId(), testCurvePoints.get(0).getId());
        assertEquals(resultCurvePoints.get(0).getTerm(), testCurvePoints.get(0).getTerm());
        assertEquals(resultCurvePoints.get(1).getId(), testCurvePoints.get(1).getId());
        assertEquals(resultCurvePoints.get(1).getTerm(), testCurvePoints.get(1).getTerm());

        Mockito.verify(curvePointRepository, Mockito.times(1)).findAll();
    }
    @Test
    void testFindCurvePointById_ValidId() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setTerm(2.0);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        CurvePoint resultCurvePoint = curveService.findCurvePointById(1);

        assertEquals(resultCurvePoint.getId(), curvePoint.getId());
        assertEquals(resultCurvePoint.getTerm(), curvePoint.getTerm());

        Mockito.verify(curvePointRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testFindCurvePointById_InvalidId() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> curveService.findCurvePointById(1));
        assertEquals("Could not find curve point with id: 1", exception.getMessage());

        Mockito.verify(curvePointRepository, Mockito.times(1)).findById(1);
    }
    @Test
    void testCreateCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setTerm(1.5);

        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePoint resultCurvePoint = curveService.createCurvePoint(curvePoint);

        assertEquals(resultCurvePoint.getId(), curvePoint.getId());
        assertEquals(resultCurvePoint.getTerm(), curvePoint.getTerm());

        Mockito.verify(curvePointRepository, Mockito.times(1)).save(curvePoint);
    }
    @Test
    void testUpdateCurvePoint_ValidId() {
        when(curvePointRepository.existsById(1)).thenReturn(true);

        curveService.updateCurvePoint(1, 2.0, 3.0);

        Mockito.verify(curvePointRepository, Mockito.times(1)).existsById(1);
        Mockito.verify(curvePointRepository, Mockito.times(1)).updateCurvePoint(1, 2.0, 3.0);
    }
    @Test
    void testDeleteCurvePoint_ValidId() {
        when(curvePointRepository.existsById(1)).thenReturn(true);
        doNothing().when(curvePointRepository).deleteById(1);

        curveService.deleteCurvePoint(1);

        Mockito.verify(curvePointRepository, Mockito.times(1)).existsById(1);
        Mockito.verify(curvePointRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteCurvePoint_InvalidId() {
        when(curvePointRepository.existsById(1)).thenReturn(false);

        when(curvePointRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> curveService.deleteCurvePoint(1));
        assertEquals("Could not find curve point with id: 1", exception.getMessage());

        Mockito.verify(curvePointRepository, Mockito.times(1)).existsById(1);
        Mockito.verify(curvePointRepository, Mockito.times(0)).deleteById(1);
    }
}
