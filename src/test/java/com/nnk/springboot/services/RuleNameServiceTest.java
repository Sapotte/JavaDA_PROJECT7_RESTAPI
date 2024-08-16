package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest {

    @Mock
    RuleNameRepository ruleNameRepository;

    @InjectMocks
    RuleNameService ruleNameService;

    @Test
    public void testFindAllRuleNames() {
        RuleName rule1 = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");
        RuleName rule2 = new RuleName("name2", "description2", "json2", "template2", "sqlStr2", "sqlPart2");

        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(rule1, rule2));

        List<RuleName> ruleNames = ruleNameService.findAllRuleNames();
        
        assertEquals(2, ruleNames.size());
        assertEquals(rule1, ruleNames.get(0));
        assertEquals(rule2, ruleNames.get(1));
    }

    @Test
    public void testFindRuleNameById_found() {
        RuleName rule = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");
        rule.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        RuleName foundRule = ruleNameService.findRuleNameById(1);

        assertEquals(rule, foundRule);
    }

    @Test
    public void testFindRuleNameById_notFound() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
           ruleNameService.findRuleNameById(1);
        });
        assertEquals("Could not find rule name with id: 1", exception.getMessage());
    }
    @Test
    public void testCreateRuleName() {
        RuleName rule = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");

        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(rule);

        RuleName createdRule = ruleNameService.createRuleName(rule);

        assertEquals(rule.getName(), createdRule.getName());
        assertEquals(rule.getDescription(), createdRule.getDescription());
        assertEquals(rule.getJson(), createdRule.getJson());
        assertEquals(rule.getTemplate(), createdRule.getTemplate());
        assertEquals(rule.getSqlStr(), createdRule.getSqlStr());
        assertEquals(rule.getSqlPart(), createdRule.getSqlPart());
    }
    @Test
    public void testUpdateRuleName_found() {
        RuleName rule = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");
        rule.setId(1);

        when(ruleNameRepository.existsById(1)).thenReturn(true);
        doNothing().when(ruleNameRepository).updateRuleName(eq(1), any(), any(), any(), any(), any(), any());

        ruleNameService.updateRuleName(1, "name2", "description2", "json2", "template2", "sqlStr2", "sqlPart2");

        verify(ruleNameRepository, times(1)).updateRuleName(eq(1), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testUpdateRuleName_notFound() {
        when(ruleNameRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ruleNameService.updateRuleName(1, "name2", "description2", "json2", "template2", "sqlStr2", "sqlPart2");
        });

        assertEquals("Could not find curve point with id: 1", exception.getMessage());
    }
    @Test
    public void testDeleteRuleName_found() {
        when(ruleNameRepository.existsById(1)).thenReturn(true);
        doNothing().when(ruleNameRepository).deleteById(1);

        ruleNameService.deleteRuleName(1);

        verify(ruleNameRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteRuleName_notFound() {
        when(ruleNameRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ruleNameService.deleteRuleName(1);
        });

        assertEquals("Could not find curve point with id: 1", exception.getMessage());
    }
}
