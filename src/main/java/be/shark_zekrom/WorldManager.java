package be.shark_zekrom;

import com.onarandombox.MultiverseCore.api.MVWorldManager;

public class WorldManager {

    public static void cloneWorld() {

        MVWorldManager worldManager = Main.core.getMVWorldManager();
        worldManager.cloneWorld(Main.worldToClone, Main.worldPrefix + Main.id);
    }

    public static void deleteWorld(int id) {

        MVWorldManager worldManager = Main.core.getMVWorldManager();
        worldManager.deleteWorld(Main.worldPrefix + id);
    }


    public static void deleteAllWorld() {

        MVWorldManager worldManager = Main.core.getMVWorldManager();

        worldManager.getMVWorlds().forEach(world -> {
            if (world.getName().startsWith(Main.worldPrefix)) {
                worldManager.deleteWorld(world.getName());
            }
        });
    }

}
