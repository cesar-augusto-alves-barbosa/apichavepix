package io.github.cesar_augusto_alves_barbosa.apichavepix.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateUtilsTest {

    @Test
    public void testFormatDate() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 13, 10, 30);
        String formattedDate = DateUtils.formatDate(dateTime);
        assertEquals("13/03/2025", formattedDate);

        assertEquals("", DateUtils.formatDate(null));
    }

    @Test
    public void testParseDate() {
        String dateString = "13/03/2025";
        LocalDateTime dateTime = DateUtils.parseDate(dateString);
        assertNotNull(dateTime);
        assertEquals(LocalDateTime.of(2025, 3, 13, 0, 0), dateTime);

        assertNull(DateUtils.parseDate(null));
        assertNull(DateUtils.parseDate(""));
        assertNull(DateUtils.parseDate("   "));

        assertThrows(DateTimeParseException.class, () -> DateUtils.parseDate("data inválida"));
    }
}
