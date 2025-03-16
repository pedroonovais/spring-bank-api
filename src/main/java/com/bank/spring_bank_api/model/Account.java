package com.bank.spring_bank_api.model;

import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    
    private long id;
    private long agency;
    private String holder;
    private String cpf;
    private Date openingDate;
    private double balance;
    @JsonProperty("active")
    private char active;
    private String typeAccount;
    
    public Account(long agency, String holder, String cpf, Date openingDate, double balance, char active,
            String typeAccount) {
        this.id = Math.abs(new Random().nextLong());
        this.agency = agency;
        this.holder = holder;
        this.cpf = cpf;
        this.openingDate = openingDate;
        this.balance = balance;
        this.active = active;
        this.typeAccount = typeAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgency() {
        return agency;
    }

    public String getHolder() {
        return holder;
    }

    public String getCpf() {
        return cpf;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public char isActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    public String getTypeAccount() {
        return typeAccount;
    }
    
    @Override
    public String toString() {
        return "Account [id=" + id + ", agency=" + agency + ", holder=" + holder + ", cpf=" + cpf + ", openingDate="
                + openingDate + ", balance=" + balance + ", active=" + active + ", typeAccount=" + typeAccount + "]";
    }

}
