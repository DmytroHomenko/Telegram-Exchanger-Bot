package com.example.telegrambotgetcurrencyrate.service;

import com.example.telegrambotgetcurrencyrate.model.CurrencyModel;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class CurrencyService {

    public static String getCurrencyRate(String message, CurrencyModel model) throws IOException, ParseException {
        URL url = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode="
                + message + "&json");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()){
            result.append(scanner.nextLine());
        }
        result.deleteCharAt(0);
        result.deleteCharAt(result.length()-1);
        JSONObject object = new JSONObject(result.toString());


//        String json = IOUtils.toString(new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode="
//                + message + "&json"), StandardCharsets.UTF_8);
//        JSONObject object = new JSONObject(json);

        model.setCur_ID(object.getInt("r030"));
        model.setCur_Name(object.getString("txt"));
        model.setCur_OfficialRate(object.getDouble("rate"));
        model.setCur_Abbreviation(object.getString("cc"));
        model.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(object.getString("exchangedate")));

        return "Official rate of UAH to " + model.getCur_Abbreviation() + "\n" +
                "on the date: " + getFormatDate(model) + "\n" +
                "is: " + model.getCur_OfficialRate() + " " + model.getCur_Abbreviation();
    }

    private static String getFormatDate(CurrencyModel model) {
        return new SimpleDateFormat("dd MMM yyyy").format(model.getDate());
    }
}
