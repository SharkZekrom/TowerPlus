package be.shark_zekrom.database;

import be.shark_zekrom.Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;


public class SQLite extends Database{
    String dbname;
    public SQLite(Main instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "database"); // Set the table name here e.g player_kills
    }

    public String game_played = "CREATE TABLE IF NOT EXISTS game_played (" +
            "`uuid` varchar(32) NOT NULL," +
            "`name` varchar(100) NOT NULL," +
            "`game_played` int(100) NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";
    public String game_won = "CREATE TABLE IF NOT EXISTS game_won (" +
            "`uuid` varchar(32) NOT NULL," +
            "`name` varchar(100) NOT NULL," +
            "`game_won` int(100) NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";
    public String points_scored = "CREATE TABLE IF NOT EXISTS points_scored (" +
            "`uuid` varchar(32) NOT NULL," +
            "`name` varchar(100) NOT NULL," +
            "`points_scored` int(100) NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";

    public String kills = "CREATE TABLE IF NOT EXISTS kills (" +
            "`uuid` varchar(32) NOT NULL," +
            "`name` varchar(100) NOT NULL," +
            "`kills` int(100) NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";

    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(game_played);
            s.executeUpdate(game_won);
            s.executeUpdate(points_scored);
            s.executeUpdate(kills);

            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       // initialize();
    }
}