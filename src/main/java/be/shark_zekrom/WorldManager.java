package be.shark_zekrom;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.entity.Player;

public class WorldManager {

    public static void cloneWorld() {

        MVWorldManager worldManager = Main.multiverseCore.getMVWorldManager();
        worldManager.cloneWorld(Main.worldToClone, Main.worldPrefix + Main.id);
    }

    public static void deleteWorld(int id) {

        MVWorldManager worldManager = Main.multiverseCore.getMVWorldManager();
        worldManager.deleteWorld(Main.worldPrefix + id);
    }


    public static void deleteAllWorld() {

        MVWorldManager worldManager = Main.multiverseCore.getMVWorldManager();

        worldManager.getMVWorlds().forEach(world -> {
            if (world.getName().startsWith(Main.worldPrefix)) {
                worldManager.deleteWorld(world.getName());
            }
        });
    }

}
