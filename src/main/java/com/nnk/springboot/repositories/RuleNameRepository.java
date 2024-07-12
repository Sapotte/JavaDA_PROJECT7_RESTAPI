package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.RuleName;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE RuleName SET name = :name, description = :description, json = :json, template = :template, sqlStr = :sqlStr, sqlPart = :sqlPart WHERE id = :id")
    void updateRuleName(Integer id, String name, String description, String json,  String template, String sqlStr, String sqlPart);
}
