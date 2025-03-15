package com.bank.spring_bank_api.model;

import java.util.Date;
import java.util.Random;

public class Account {
    
    private long id;
    private long agency;
    private String holder;
    private long cpf;
    private Date openingDate;
    private double balance;
    private String active;
    private String typeAccount;
    
    public Account(long agency, String holder, long cpf, Date openingDate, double balance, String active,
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

    public long getCpf() {
        return cpf;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public double getBalance() {
        return balance;
    }

    public String isActive() {
        return active;
    }

    public void setActive(String active) {
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
