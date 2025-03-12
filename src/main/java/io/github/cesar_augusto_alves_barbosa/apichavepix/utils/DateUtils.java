package io.github.cesar_augusto_alves_barbosa.apichavepix.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : "";
    }

    public static LocalDateTime parseDate(String date) {
        return date != null && !date.isBlank() ? LocalDate.parse(date, FORMATTER).atStartOfDay() : null;
    }
}
