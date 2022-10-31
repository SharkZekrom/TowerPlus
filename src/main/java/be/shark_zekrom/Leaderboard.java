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

    public static void leaderboard(HolographicDisplaysAPI api, Location location) {
        Hologram hologram = api.createHologram(location);
        holograms.add(hologram);

        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("game_played",5);
                        leaderboardUpdate(data,Main.leaderboard_game_played,"game_played", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<String[]> data = getData("game_won",5);
                        leaderboardUpdate(data,Main.leaderboard_game_won,"game_won", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 20);


                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("points_scored",5);
                        leaderboardUpdate(data,Main.leaderboard_points_scored,"points_scored", hologram);
                    }
                }.runTaskLater(Main.getInstance(), 40);

            }

        }.runTaskTimer(Main.getInstance(), 0, 60);
    }

    private static void leaderboardUpdate(ArrayList<String[]> data,ArrayList<String> config, String table, Hologram hologram) {
        hologram.getLines().clear();

        int index = 0;

        for (int i = 0; i < config.size(); i++) {
            String line = config.get(i);
            if (line.contains("%player%")) {
                if (data.size() > index) {
                    String[] playerData = data.get(index);
                    line = line.replaceAll("%player%", playerData[0]);
                    line = line.replaceAll("%value%", playerData[1]);
                } else {
                    line = line.replaceAll("%player%", "No data");
                    line = line.replaceAll("%value%", "No data");
                }
                index++;
            }
            hologram.getLines().appendText(line);
        }
    }


    public static ArrayList<String[]> getData(String table, int number) {
        ArrayList<String[]> data = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = Main.db.getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " ORDER BY " + table + " DESC LIMIT " + number + ";");
            rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new String[]{rs.getString("name"), rs.getString(table)});
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
