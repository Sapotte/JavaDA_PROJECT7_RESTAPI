package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {
    private static final Logger LOG = LoggerFactory.getLogger(RuleNameService.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> findAllRuleNames() {
        return ruleNameRepository.findAll();
    }

    public RuleName findRuleNameById(int id) {
        return ruleNameRepository.findById(id).orElseThrow(()-> new RuntimeException("Could not find rule name with id: " + id));
    }

    public RuleName createRuleName(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public void updateRuleName(Integer id, String name, String description, String json,  String template, String sqlStr, String sqlPart) {
        if(!ruleNameRepository.existsById(id)) {
            LOG.error("Update Rulename failed");
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        ruleNameRepository.updateRuleName(id, name, description, json, template, sqlStr, sqlPart);
        LOG.info("Rule name updated");
    }

    public void deleteRuleName(Integer id) {
        if(!ruleNameRepository.existsById(id)) {
            LOG.error("Delete Rulename failed");
            throw new RuntimeException("Could not find curve point with id: " + id);
        }
        ruleNameRepository.deleteById(id);
        LOG.info("Rule name deleted");
    }
}
