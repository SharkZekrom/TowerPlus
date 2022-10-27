package be.shark_zekrom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameManager {

    public static ArrayList<GameManager> games = new ArrayList<>();
    int gameId;
    public int getGameId() {
        return gameId;
    }

    int gameBluePoints;
    public int getGameBluePoints() {
        return gameBluePoints;
    }
    public void setGameBluePoints(int gameBluePoints) {
        this.gameBluePoints = gameBluePoints;
    }

    int gameRedPoints;
    public int getGameRedPoints() {
        return gameRedPoints;
    }
    public void setGameRedPoints(int gameRedPoints) {
        this.gameRedPoints = gameRedPoints;
    }

    GameStatus gameStatus;
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    int countdown;
    public int getCountdown() {
        return countdown;
    }
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    int players;
    public int getPlayers() {
        return players;
    }
    public void setPlayers(int players) {
        this.players = players;
    }

    ArrayList<Player> redPlayers;
    public ArrayList<Player> getRedPlayers() {
        return redPlayers;
    }
    public void addRedPlayer(Player player) {
        bluePlayers.add(player);
    }
    public void removeRedPlayer(Player player) {
        bluePlayers.remove(player);
    }

    ArrayList<Player> bluePlayers;
    public ArrayList<Player> getBluePlayers() {
        return bluePlayers;
    }
    public void addBluePlayer(Player player) {
        bluePlayers.add(player);
    }
    public void removebluePlayer(Player player) {
        bluePlayers.remove(player);
    }


    public GameManager() {
        gameId = games.size() + 1;
        gameBluePoints = 0;
        gameRedPoints = 0;
        gameStatus = GameStatus.WAITING;
        countdown = 30;
        players = 0;
        redPlayers = new ArrayList<>();
        bluePlayers = new ArrayList<>();
        WorldManager.cloneWorld();
        GameManager.games.add(this);

    }


    public static GameManager getGameById(int id) {
        for (GameManager game : GameManager.games) {
            if (game.getGameId() == id) {
                return game;
            }
        }
        return null;
    }




    public enum GameStatus {
        WAITING,
        STARTING,
        INGAME,
        ENDING

    }
}
