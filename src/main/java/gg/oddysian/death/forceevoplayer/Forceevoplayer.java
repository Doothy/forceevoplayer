package gg.oddysian.death.forceevoplayer;

import gg.oddysian.death.forceevoplayer.command.ForceEvoCommand;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Mod(
        modid = Forceevoplayer.MOD_ID,
        name = Forceevoplayer.MOD_NAME,
        version = Forceevoplayer.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class Forceevoplayer {

    public static final String MOD_ID = "forceevoplayer";
    public static final String MOD_NAME = "Forceevoplayer";
    public static final String VERSION = "1.0-SNAPSHOT";
    public static Logger log = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Forceevoplayer INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event){
        log.info("Death's forceevo command has joined the battlefield to evolve all the Pokemon!");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new ForceEvoCommand());
    }
}
