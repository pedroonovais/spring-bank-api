package com.bank.spring_bank_api.dto;

public class PixRequest {
    private long originId;
    private long destinationId;
    private double value;
    
    public long getOriginId() {
        return originId;
    }
    public void setOriginId(long originId) {
        this.originId = originId;
    }
    public long getDestinationId() {
        return destinationId;
    }
    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    
}
