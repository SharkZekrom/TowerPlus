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

    GameStatus gameStatus;
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameManager() {
        gameId = games.size() + 1;
        gameBluePoints = 0;
        gameRedPoints = 0;
        gameStatus = GameStatus.WAITING;

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
