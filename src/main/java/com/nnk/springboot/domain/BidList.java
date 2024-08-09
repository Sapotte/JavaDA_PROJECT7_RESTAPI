package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Entity
@Table(name = "bidlist")
public class BidList {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "BidListId", nullable = false)
    private int bidListId;

    @NotNull(message = "This field is required")
    @Size(max = 30)
    @Column(name = "account", nullable = false, length = 30)
    private String account;

    @NotNull(message = "This field is required")
    @Size(max = 30)
    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Column(name = "bidQuantity")
    @Digits(integer = 12, fraction = 0, message = "Must be an integer")
    private Double bidQuantity;

    @Column(name = "askQuantity")
    @Digits(integer = 12, fraction = 0, message = "Must be an integer")
    private Double askQuantity;

    @Column(name = "bid")
    @Digits(integer = 12, fraction = 0, message = "Invalid number")
    private Double bid;

    @Column(name = "ask")
    @Digits(integer = 12, fraction = 0, message = "Invalid number")
    private Double ask;

    @Column(name = "benchmark", length = 125)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Must contain only alphabetic characters")
    private String benchmark;

    @Column(name = "bidListDate")
    private Instant bidListDate;

    @Column(name = "commentary", length = 125)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Must contain only alphabetic characters")
    private String commentary;

    @Column(name = "security", length = 125)
    private String security;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "trader", length = 125)
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Must contain only alphabetic characters")
    private String trader;

    @Column(name = "book", length = 125)
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Must contain only alphabetic characters")
    private String book;

    @Column(name = "creationName", length = 125)
    private String creationName;

    @Column(name = "creationDate")
    private Instant creationDate;

    @Column(name = "revisionName", length = 125)
    private String revisionName;

    @Column(name = "revisionDate")
    private Instant revisionDate;

    @Column(name = "dealName", length = 125)
    private String dealName;

    @Column(name = "dealType", length = 125)
    private String dealType;

    @Column(name = "sourceListId", length = 125)
    private String sourceListId;

    @Column(name = "side", length = 125)
    private String side;

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public BidList() {
    }

    public int getBidListId() {
        return bidListId;
    }

    public void setBidListId(int bidListId) {
        this.bidListId = bidListId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSourceListId() {
        return sourceListId;
    }

    public void setSourceListId(String sourceListId) {
        this.sourceListId = sourceListId;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public Instant getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Instant revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getRevisionName() {
        return revisionName;
    }

    public void setRevisionName(String revisionName) {
        this.revisionName = revisionName;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Instant getBidListDate() {
        return bidListDate;
    }

    public void setBidListDate(Instant bidListDate) {
        this.bidListDate = bidListDate;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAskQuantity() {
        return askQuantity;
    }

    public void setAskQuantity(Double askQuantity) {
        this.askQuantity = askQuantity;
    }

    public Double getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(Double bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
