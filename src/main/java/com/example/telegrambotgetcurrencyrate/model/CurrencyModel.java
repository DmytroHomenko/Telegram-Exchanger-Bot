package com.example.telegrambotgetcurrencyrate.model;

import lombok.Data;

import java.util.Date;

@Data
public class CurrencyModel {
    Integer cur_ID;
    String cur_Name;
    Double cur_OfficialRate;
    String cur_Abbreviation;
    Date date;
}
