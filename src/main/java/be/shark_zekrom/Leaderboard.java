package be.shark_zekrom;

import be.shark_zekrom.database.Errors;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Leaderboard {

    public static ArrayList<Hologram> holograms = new ArrayList<>();

    public static void initialize(HolographicDisplaysAPI api) {
        leaderboardGame_won(api);
    }

    public static void leaderboardGame_won(HolographicDisplaysAPI api) {
        Hologram hologram = api.createHologram(new Location(Bukkit.getWorld("world"), 0, -58, 0));
        holograms.add(hologram);



        new BukkitRunnable() {
            @Override
            public void run() {


                new BukkitRunnable() {
                    @Override
                    public void run() {

                        LinkedHashMap<String, Integer> data = getData("game_played",5);
                        leaderboardUpdate(data,"game_played", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        LinkedHashMap<String, Integer> data = getData("game_won",5);
                        leaderboardUpdate(data,"game_won", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 20);


                new BukkitRunnable() {
                    @Override
                    public void run() {

                        LinkedHashMap<String, Integer> data = getData("points_scored",5);
                        leaderboardUpdate(data,"points_scored", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 40);

            }

        }.runTaskTimer(Main.getInstance(), 0, 60);
    }

    private static void leaderboardUpdate(LinkedHashMap<String, Integer> data,String table, Hologram hologram) {
        hologram.getLines().clear();
        hologram.getLines().appendText("Leaderboard");
        hologram.getLines().appendText(table);
        hologram.getLines().appendText(" ");


        int index = 1;
        for (String key : data.keySet()) {
            hologram.getLines().appendText( index + ". " + Bukkit.getOfflinePlayer(UUID.fromString(key)).getName() + " : " + data.get(key));
            index++;
        }
        if (data.size() < 5) {
            for (int i = 0; i < 5 - data.size(); i++) {
                hologram.getLines().appendText(index + ". ");
                index++;
            }
        }
    }


    public static LinkedHashMap<String, Integer> getData(String table, int number) {
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = Main.db.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " ORDER BY " + table + " DESC LIMIT " + number + ";");
            rs = ps.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("uuid"), rs.getInt(table));
            }
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                Main.getInstance().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return data;
    }




    public static void removeHolograms() {
        for (Hologram hologram : holograms) {
            hologram.delete();
        }
    }


}
