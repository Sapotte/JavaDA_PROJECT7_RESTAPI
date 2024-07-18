package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurveControllerTest {

    @InjectMocks
    private CurveController controller;

    @Mock
    private CurveService curveService;

    private Authentication auth;

    @BeforeEach
    void setUp() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        auth = new TestingAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void homeTest() throws Exception {
        //Arrange
        Model model = new ConcurrentModel();
        List<CurvePoint> curvePointList = new ArrayList<>();
        when(curveService.findAllCurvePoints()).thenReturn(curvePointList);

        //Act
        String view = controller.home(model);

        //Assert
        assertEquals("curvePoint/list", view);
        assertSame(curvePointList, model.getAttribute("curvePoints"));
        assertEquals(auth.getName(), model.getAttribute("username"));
    }

    @Test
    public void addCurvePointFormTest() throws Exception {
        //Act
        ModelAndView mav = controller.addCurvePointForm();

        //Assert
        assertEquals("curvePoint/add", mav.getViewName());
        assertTrue(mav.getModel().containsKey("curvePoint"));
        assertTrue(mav.getModel().get("curvePoint") instanceof CurvePoint);
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        //Arrange
        Model model = new ConcurrentModel();
        int curveId = 1;
        CurvePoint curvePoint = new CurvePoint();
        when(curveService.findCurvePointById(curveId)).thenReturn(curvePoint);

        //Act
        String view = controller.showUpdateForm(curveId, model);

        //Assert
        assertEquals("curvePoint/update", view);
        assertSame(curvePoint, model.getAttribute("curvePoint"));
    }
    // New Test Case Starts from here
    @Test
    public void updateCurve_Error() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(2.5);
        curvePoint.setValue(3.7);
        BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");
        result.reject("error", "Test error message");

        String view = controller.updateCurve(1, curvePoint, result);

        assertEquals("curvePoint/update/{id}", view);
    }

   @Test
   public void updateCurve_Success() throws Exception {
       CurvePoint curvePoint = new CurvePoint();
       curvePoint.setTerm(2.5);
       curvePoint.setValue(3.7);
       BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");

       String view = controller.updateCurve(1, curvePoint, result);

       assertEquals("redirect:/curvePoint/list", view);
   }
    @Test
    public void deleteCurve_Success() throws Exception {
        int curveId = 1;

        // Act
        String view = controller.deleteCurve(curveId, new ConcurrentModel());

        // Assert
        assertEquals("redirect:/curvePoint/list", view);
    }

    @Test
    public void deleteCurve_Failure() throws Exception {
        int curveId = 1;
        Model model = new ConcurrentModel();

        // Simulate failure
        Mockito.doThrow(new RuntimeException("Test exception")).when(curveService).deleteCurvePoint(curveId);

        // Act
        String view = controller.deleteCurve(curveId, model);

        // Assert
        assertEquals("redirect:/curvePoint/list?errorDelete", view);
        assertEquals("Test exception", model.getAttribute("message"));
    }
}
