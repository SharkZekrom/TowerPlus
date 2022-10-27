package be.shark_zekrom;

import com.onarandombox.MultiverseCore.api.MVWorldManager;

public class WorldManager {

    public static void cloneWorld() {


        MVWorldManager worldManager = Main.core.getMVWorldManager();

        worldManager.cloneWorld("creative", "TowerPlus_" + (GameManager.games.size() + 1));

    }

    public static void deleteWorld(int ID) {

        MVWorldManager worldManager = Main.core.getMVWorldManager();

        worldManager.deleteWorld("TowerPlus_" + ID);

    }


    public static void deleteAllWorld() {

        MVWorldManager worldManager = Main.core.getMVWorldManager();

        worldManager.getMVWorlds().forEach(world -> {
            if (world.getName().startsWith("TowerPlus_")) {
                worldManager.deleteWorld(world.getName());
            }
        });
    }

}
