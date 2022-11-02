package be.shark_zekrom;

import be.shark_zekrom.database.Errors;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.HologramLines;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Leaderboard {

    public static ArrayList<Hologram> hologramsHolographicDisplaysAPI = new ArrayList<>();
    public static ArrayList<eu.decentsoftware.holograms.api.holograms.Hologram> hologramsDecentHolograms = new ArrayList<>();

    public static void leaderboardHolographicDisplaysAPI(HolographicDisplaysAPI api, Location location) {
        Hologram hologram = api.createHologram(location);
        hologramsHolographicDisplaysAPI.add(hologram);

        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("game_played",5);
                        leaderboardUpdateHolographicDisplaysAPI(data,Main.leaderboard, Main.leaderboard_game_played, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<String[]> data = getData("game_won",5);
                        leaderboardUpdateHolographicDisplaysAPI(data,Main.leaderboard, Main.leaderboard_game_won, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 20);


                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("points_scored",5);
                        leaderboardUpdateHolographicDisplaysAPI(data,Main.leaderboard, Main.leaderboard_points_scored, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 40);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("kills",5);
                        leaderboardUpdateHolographicDisplaysAPI(data,Main.leaderboard, Main.leaderboard_kills, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 60);

            }

        }.runTaskTimer(Main.getInstance(), 0, 80);
    }


    public static void leaderboardDecentHolograms(Location location) {
        eu.decentsoftware.holograms.api.holograms.Hologram hologram = DHAPI.createHologram("TowerPlusLeaderboard", location);
        hologramsDecentHolograms.add(hologram);

        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("game_played",5);
                        leaderboardUpdateDecentHolograms(data,Main.leaderboard, Main.leaderboard_game_played, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<String[]> data = getData("game_won",5);
                        leaderboardUpdateDecentHolograms(data,Main.leaderboard, Main.leaderboard_game_won, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 20);


                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("points_scored",5);
                        leaderboardUpdateDecentHolograms(data,Main.leaderboard, Main.leaderboard_points_scored, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 40);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ArrayList<String[]> data = getData("kills",5);
                        leaderboardUpdateDecentHolograms(data,Main.leaderboard, Main.leaderboard_kills, hologram);
                    }
                }.runTaskLater(Main.getInstance(), 60);

            }

        }.runTaskTimer(Main.getInstance(), 0, 80);
    }




    private static void leaderboardUpdateHolographicDisplaysAPI(ArrayList<String[]> data, ArrayList<String> config,String leaderboard, Hologram hologram) {
        hologram.getLines().clear();
        int index = 0;
        for (String line : config) {
            line = line.replaceAll("%category%", leaderboard);
            if (line.contains("%player%")) {
                if (data.size() > index) {
                    String[] playerData = data.get(index);
                    line = line.replaceAll("%player%", playerData[0]).replaceAll("%value%", playerData[1]);
                } else {
                    line = line.replaceAll("%player%", Main.leaderboard_noData).replaceAll("%value%", Main.leaderboard_noData);
                }
                index++;
            }
            hologram.getLines().appendText(line);
        }


    }

    private static void leaderboardUpdateDecentHolograms(ArrayList<String[]> data, ArrayList<String> config,String leaderboard, eu.decentsoftware.holograms.api.holograms.Hologram hologram) {

        int index = 0;
        int indexLine = 0;

        for (String line : config) {
            if (line.contains("%player%")) {
                if (data.size() > index) {
                    String[] playerData = data.get(index);
                    line = line.replaceAll("%player%", playerData[0]);
                    line = line.replaceAll("%value%", playerData[1]);
                    line = line.replaceAll("%category%", leaderboard);
                } else {
                    line = line.replaceAll("%player%", Main.leaderboard_noData);
                    line = line.replaceAll("%value%", Main.leaderboard_noData);
                    line = line.replaceAll("%category%", leaderboard);

                }
                index++;
            }


            if (DHAPI.getHologramLine(hologram.getPage(0), indexLine) == null) {
                DHAPI.addHologramLine(hologram.getPage(0), line);

            } else {
                DHAPI.setHologramLine(hologram, indexLine, line);
            }
            indexLine++;
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

        if (Bukkit.getServer().getPluginManager().getPlugin("DecentHolograms") != null) {
            DHAPI.getHologram("TowerPlusLeaderboard").delete();
        } else if (Bukkit.getServer().getPluginManager().getPlugin("HolographicDisplays") != null) {
            for (Hologram hologram : hologramsHolographicDisplaysAPI) {
                hologram.delete();
            }
        }
    }







}
