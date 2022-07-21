package com.common.config;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {

  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  @Override
  public Date parse(String text, Locale locale) throws ParseException {
    return formatter.parse(text);
  }

  @Override
  public String print(Date date, Locale locale) {
    return formatter.format(date);
  }
}
