package com.example.hangoutfinance_app;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class modify {
    public String member, type, amount;
    public Map<String, Boolean> stars = new HashMap<>();

    public modify(){

    }

    public modify(String member, String type, String amount){
        this.member=member;
        this.type=type;
        this.amount=amount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String,Object> dataMap= new HashMap<>();
        dataMap.put("MEMBER NAME", member);
        dataMap.put("EXPENSE TYPE", type);
        dataMap.put("AMOUNT", amount);
        dataMap.put("stars", stars);
        return dataMap;
    }
}
