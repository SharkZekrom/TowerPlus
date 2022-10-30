package be.shark_zekrom.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import be.shark_zekrom.Main;
import org.bukkit.entity.Player;


public abstract class Database {
    Main plugin;
    Connection connection;
    // The name of the table we created back in SQLite class.

    public int tokens = 0;

    public Database(Main instance) {
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

   // public void initialize() {
    //        connection = getSQLConnection();
    //        try {
    //            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid = ?");
    //            ResultSet rs = ps.executeQuery();
    //            close(ps, rs);
    //
    //        } catch (SQLException ex) {
    //            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
    //        }
    //    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }





    public void initializeAccount(Player player, String table) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (uuid," + table + ") VALUES(?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, player.getUniqueId().toString());                                             // YOU MUST put these into this line!! And depending on how many
            ps.setInt(2, 0);

            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }


    public boolean hasAccount(String uuid, String table) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT uuid FROM " + table + " WHERE uuid = '" + uuid + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("uuid").equalsIgnoreCase(uuid)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return false;
    }
}
