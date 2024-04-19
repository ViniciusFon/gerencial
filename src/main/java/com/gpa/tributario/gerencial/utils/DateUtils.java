package com.gpa.tributario.gerencial.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDate toLocalDate(String date){

        if("-".equals(date)) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //convert String to LocalDate
        return LocalDate.parse(date.trim(), formatter);
    }
}
