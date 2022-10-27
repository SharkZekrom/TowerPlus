package be.shark_zekrom;

import org.bukkit.Bukkit;

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

    int gameStatus;
    public int getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameManager(int id, int bluePoints, int redPoints, GameStatus status) {
        gameId = id;
        gameBluePoints = bluePoints;
        gameRedPoints = redPoints;
        gameStatus = status.ordinal();

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
