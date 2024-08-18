package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
public class CurveControllerTest {

    @InjectMocks
    CurveController controller;

    @Mock
    CurveService curveService;

    private Authentication auth;

    @BeforeEach
    void setUp() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        auth = new TestingAuthenticationToken("admin", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void addCurvePointFormTest()  {
        //Act
        ModelAndView mav = controller.addCurvePointForm();

        //Assert
        assertEquals("curvePoint/add", mav.getViewName());
        assertTrue(mav.getModel().containsKey("curvePoint"));
        assertInstanceOf(CurvePoint.class, mav.getModel().get("curvePoint"));
    }

    @Test
    public void updateCurve_Error() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(2.5);
        curvePoint.setValue(3.7);
        BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");
        result.reject("error", "Test error message");

        String view = controller.updateCurve(1, curvePoint, result);

        assertEquals("curvePoint/update/{id}", view);
    }

   @Test
   public void updateCurve_Success() {
       CurvePoint curvePoint = new CurvePoint();
       curvePoint.setTerm(2.5);
       curvePoint.setValue(3.7);
       BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");

       String view = controller.updateCurve(1, curvePoint, result);

       assertEquals("redirect:/curvePoint/list", view);
   }
    @Test
    public void deleteCurve_Success()  {
        int curveId = 1;

        // Act
        String view = controller.deleteCurve(curveId, new ConcurrentModel());

        // Assert
        assertEquals("redirect:/curvePoint/list", view);
    }

    @Test
    public void home_Success() {
        Model model = new ConcurrentModel();
        // Act
        ModelAndView view = controller.home(model);

        // Assert
        assertEquals("curvePoint/list", view.getViewName());
        assertTrue(view.getModel().containsKey("curvePoints"));
        assertTrue(view.getModel().containsKey("username"));
    }
@Test
public void validate_Error() {
    CurvePoint curvePoint = new CurvePoint();
    curvePoint.setTerm(2.5);
    curvePoint.setValue(3.7);
    BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");
    result.reject("error", "Test error message");

    String view = controller.validate(curvePoint, result, new ConcurrentModel());

    assertEquals("curvePoint/add", view);
}

@Test
public void validate_Success()  {
    CurvePoint curvePoint = new CurvePoint();
    curvePoint.setTerm(2.5);
    curvePoint.setValue(3.7);
    BindingResult result = new BeanPropertyBindingResult(curvePoint, "curvePoint");

    String view = controller.validate(curvePoint, result, new ConcurrentModel());

    assertEquals("redirect:/curvePoint/list", view);
}

@Test
public void showUpdateForm_Success(){
    Model model = new ConcurrentModel();

    ModelAndView mav = controller.showUpdateForm(1, model);

    assertEquals("curvePoint/update", mav.getViewName());
}
}
