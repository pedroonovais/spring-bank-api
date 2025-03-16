package com.bank.spring_bank_api.dto;

public class TransactionRequest {
    private long id;
    private double value;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    
}
