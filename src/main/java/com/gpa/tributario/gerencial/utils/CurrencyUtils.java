package com.gpa.tributario.gerencial.utils;

import java.math.BigDecimal;


public class CurrencyUtils {

    public static BigDecimal toBigDecimal(String valor){

        return BigDecimal.valueOf(toDouble(valor));
    }

    public static Double toDouble(String valor){

        String newValue  = valor.replace("R$","").replace(".","").replace(",",".");

        if("-".equals(newValue.trim())) return 0D;

        return Double.parseDouble(newValue);
    }
}
