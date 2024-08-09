package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;

import java.time.Instant;

@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "Id", length = 4)
    private int id;

    @Column(name = "CurveId")
    @Digits(integer = 10, fraction = 0, message = "Must be an integer")
    private Integer curveId;

    @Column(name = "asOfDate")
    private Instant asOfDate;

    @Column(name = "term")
    @Digits(integer = 5, fraction = 0, message = "Invalid number")
    private Double term;

    @Column(name = "value")
    @Digits(integer = 5, fraction = 0, message = "Invalid number")
    private Double value;

    @Column(name = "creationDate")
    private Instant creationDate;

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    public CurvePoint() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(Double term) {
        this.term = term;
    }

    public Instant getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Instant asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Integer getCurveId() {
        return curveId;
    }

    public void setCurveId(Integer curveId) {
        this.curveId = curveId;
    }
}
