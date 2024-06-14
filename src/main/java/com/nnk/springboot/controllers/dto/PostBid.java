package com.nnk.springboot.controllers.dto;

import java.util.Objects;

public class PostBid {
    private String account;
    private String type;
    private Integer quantity;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostBid postBid = (PostBid) o;
        return Objects.equals(account, postBid.account) && Objects.equals(type, postBid.type) && Objects.equals(quantity, postBid.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, type, quantity);
    }
}
