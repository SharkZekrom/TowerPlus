package be.shark_zekrom;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static String getIntervalTime(long longInterval) {

        long cloneLongInterval = System.currentTimeMillis() - longInterval;

        long hours = TimeUnit.MILLISECONDS.toHours(cloneLongInterval);
        cloneLongInterval -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(cloneLongInterval);
        cloneLongInterval -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(cloneLongInterval);
        cloneLongInterval -= TimeUnit.SECONDS.toMillis(seconds);

        String stringInterval = "%02d:%02d:%02d";
        return String.format(stringInterval, hours, minutes, seconds, cloneLongInterval);
    }


    public static void next(Inventory inventory) {
        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextmeta = next.getItemMeta();
        nextmeta.setDisplayName("next");
        next.setItemMeta(nextmeta);
        inventory.setItem(50, next);
    }

    public static void previous(Inventory inventory) {
        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta previousmeta = previous.getItemMeta();
        previousmeta.setDisplayName("previous");
        previous.setItemMeta(previousmeta);
        inventory.setItem(48, previous);
    }
}
