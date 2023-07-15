package com.ekumfi.juice.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class Phone {
    private String number, type;

    public Phone() {

    }

    public Phone(String number, String type) {
        this.number = number;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return this.number.equals(((Phone) obj).getNumber());

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
