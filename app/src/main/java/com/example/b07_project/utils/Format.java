package com.example.b07_project.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.lang.*;
import java.text.DecimalFormat;

public class Format {
    public static String formatCurrency (double price){
        double tempPrice = price;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        BigDecimal bd = new BigDecimal(tempPrice);
        bd.setScale(2, RoundingMode.HALF_EVEN);
        tempPrice = bd.doubleValue();
        return ("$" + (formatter.format(tempPrice)));
    }

    public static Double bankersRound (double price){
        double tempPrice2 = price;
        BigDecimal bd = new BigDecimal(tempPrice2).setScale(2, RoundingMode.HALF_EVEN);
        tempPrice2 = bd.doubleValue();
        return tempPrice2;
    }



}
