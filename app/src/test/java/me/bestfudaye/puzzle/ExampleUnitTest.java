package me.bestfudaye.puzzle;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        long time1 = 2000;
        Date date = new Date(time1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss", Locale.CANADA);
        String timeStr = simpleDateFormat.format(date);
        System.out.print(timeStr);
    }
}