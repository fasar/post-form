package fr.fasar.postform;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RecorderServiceTest {
    @Test
    public void beforeTomorrowUTCTest() {
        Instant now = Instant.now();
        Instant thisMorning = now.truncatedTo(ChronoUnit.DAYS);
        long oneDayMs = RecorderService.msBeforeTomorrowUTC(thisMorning);
        Assert.assertEquals(24*60*60*1000, oneDayMs);
    }

    @Test
    public void dayOfYearTest() {
        String s = RecorderService.dayOfYear();
        Assert.assertEquals(8, s.length());
    }
}