package fr.fasar.postform;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class Utils {


    public static long msBeforeTomorrowUTC(Instant now) {
        Instant tomorrow = now.plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        return tomorrow.toEpochMilli() - now.toEpochMilli();
    }


    public static String dayOfYear() {
        Instant now = Instant.now();
        ZonedDateTime day = now.atZone(ZoneOffset.UTC);
        return String.format("%04d%02d%02d", day.get(ChronoField.YEAR), day.get(ChronoField.MONTH_OF_YEAR), day.get(ChronoField.DAY_OF_MONTH));
    }
}
