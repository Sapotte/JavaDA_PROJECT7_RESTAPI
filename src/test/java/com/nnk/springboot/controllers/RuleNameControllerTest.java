package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RuleNameControllerTest {
    @InjectMocks
    private RuleNameController ruleNameController;

    @Mock
    RuleNameRepository ruleNameRepository;

    @Mock
    RuleNameService ruleNameService;
    // new Mockito mocks to bind and validate the RuleName object
    @Mock
    RuleName ruleNameMock;
    @Mock
    BindingResult bindingResultMock;

    @Test
    void testHome() {
        // Arrange
        Model model = new BindingAwareModelMap();
        List<RuleName> ruleNames = List.of(
                new RuleName("name1", "desc1", "json1", "template1", "sqlstr1", "sqlPart1"),
                new RuleName("name2", "desc2", "json2", "template2", "sqlstr2", "sqlPart2"));
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        Mockito.when(ruleNameService.findAllRuleNames()).thenReturn(ruleNames);

        // Act
        String viewName = ruleNameController.home(model);

        // Assert
        assertEquals("ruleName/list", viewName);
        assertEquals(ruleNames, model.getAttribute("ruleNames"));
        assertEquals(auth.getName(), model.getAttribute("username"));
    }

    @Test
    void testAddRuleForm() {
        // Act
        ModelAndView mav = ruleNameController.addRuleForm();

        // Assert
        assertEquals("ruleName/add", mav.getViewName());
    }

    @Test
    void testValidateWithError() {
        // Arrange
        RuleName ruleName = new RuleName();
        BindingResult result = new BeanPropertyBindingResult(ruleName, "ruleName");
        result.reject("error");

        // Act
        String viewName = ruleNameController.validate(ruleName, result, new BindingAwareModelMap());

        // Assert
        assertEquals("ruleName/add", viewName);
    }

    @Test
    void testValidateWithNoError() {
        // Arrange
        RuleName ruleName = new RuleName();
        BindingResult result = new BeanPropertyBindingResult(ruleName, "ruleName");
        RuleName newRuleName = new RuleName();

        Mockito.when(ruleNameService.createRuleName(any(RuleName.class))).thenReturn(newRuleName);

        // Act
        String viewName = ruleNameController.validate(ruleName, result, new BindingAwareModelMap());

        // Assert
        assertEquals("ruleName/list", viewName);
    }

    @Test
    void testShowUpdateForm() {
        // Arrange
        RuleName ruleName = new RuleName();
        Model model = new BindingAwareModelMap();
        Integer id = 1;

        Mockito.when(ruleNameService.findRuleNameById(id)).thenReturn(ruleName);

        // Act
        String viewName = ruleNameController.showUpdateForm(id, model);

        // Assert
        assertEquals("ruleName/update", viewName);
        assertEquals(ruleName, model.getAttribute("ruleName"));
    }
}
