package be.shark_zekrom;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String getIntervalTime(long longInterval) {

        long cloneLongInterval = longInterval;

        long hours = TimeUnit.MILLISECONDS.toHours(cloneLongInterval);
        cloneLongInterval -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(cloneLongInterval);
        cloneLongInterval -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(cloneLongInterval);
        cloneLongInterval -= TimeUnit.SECONDS.toMillis(seconds);

        String stringInterval = "%02d:%02d.%02d";
        return String.format(stringInterval, hours, minutes, seconds, cloneLongInterval);
    }
}
