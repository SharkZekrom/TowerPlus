package be.shark_zekrom;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    public static final Map<UUID, FastBoard> boards = new HashMap<>();
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


    ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
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

    public static int getGameIdByPlayer(Player player) {
        int id = 0;
        for (int i = 0; i < games.size(); i++) {
            GameManager game = GameManager.getGameById(i + 1);
            if (game.getPlayers().contains(player)) {
                id = i + 1;
            }
        }

        return id;

    }


    public GameManager() {
        gameId = games.size() + 1;
        gameBluePoints = 0;
        gameRedPoints = 0;
        gameStatus = GameStatus.WAITING;
        countdown = 30;
        players = new ArrayList<>();
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


    public static void updateBoard(FastBoard board) {
        GameManager game = getGameById(getGameIdByPlayer(board.getPlayer()));

        switch (game.getGameStatus()) {
            case WAITING:
                board.
                board.updateLines(
                        "Waiting for players",
                        "",
                        game.getPlayers().size() + "/10"
                );
                break;
            case STARTING:
                board.updateLines(
                        "",
                        ""
                );
                break;
            case INGAME:
                board.updateLines(
                        "",
                        ""
                );
                break;
            case ENDING:
                board.updateLines(
                        "",
                        ""
                );
                break;
        }
    }
}
