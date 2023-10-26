package com.example.oosd2023project_android;

import java.io.Serializable;
import java.util.Date;

public class CreditCards implements Serializable {
    private int creditCardId;
    private String cCName;
    private String cCNumber;
    private String cExpiry;
    private int customerId;

    public CreditCards(int creditCardId, String cCName, String cCNumber, String cExpiry, int customerId) {
        this.creditCardId = creditCardId;
        this.cCName = cCName;
        this.cCNumber = cCNumber;
        this.cExpiry = cExpiry;
        this.customerId = customerId;
    }

    public int getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(int creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getcCName() {
        return cCName;
    }

    public void setcCName(String cCName) {
        this.cCName = cCName;
    }

    public String getcCNumber() {
        return cCNumber;
    }

    public void setcCNumber(String cCNumber) {
        this.cCNumber = cCNumber;
    }

    public String getcExpiry() {
        return cExpiry;
    }

    public void setcExpiry(String cExpiry) {
        this.cExpiry = cExpiry;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return creditCardId +". " + cCName ;
    }
}
