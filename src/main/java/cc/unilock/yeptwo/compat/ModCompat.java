package cc.unilock.yeptwo.compat;

import cc.unilock.yeptwo.YepTwo;
import cc.unilock.yeptwo.compat.chromaticraft.ChromatiCraftCompat;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.MinecraftForge;

public class ModCompat {
    public static void init() {
        if (Loader.isModLoaded("ChromatiCraft")) {
            YepTwo.LOGGER.info("ChromatiCraft detected - loading support");
            MinecraftForge.EVENT_BUS.register(new ChromatiCraftCompat());
        }
    }
}
