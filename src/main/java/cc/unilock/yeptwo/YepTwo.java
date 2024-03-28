package cc.unilock.yeptwo;

import cc.unilock.yeptwo.compat.ModCompat;
import cc.unilock.yeptwo.networking.PacketSender;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = YepTwo.MOD_ID, version = Tags.VERSION, name = "YepTwo", acceptedMinecraftVersions = "[1.7.10]", acceptableRemoteVersions = "*")
public class YepTwo {
    public static final String MOD_ID = "yeptwo";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Hello from Yep!");

        MinecraftForge.EVENT_BUS.register(this);
        ModCompat.init();
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        PacketSender.sendDeathMessage(event.entityLiving, event.source);
    }

    @SubscribeEvent
    public void onAchievementEvent(AchievementEvent event) {
        PacketSender.sendAdvancementMessage(event.entityPlayer, event.achievement);
    }
}
