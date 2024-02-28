package cc.unilock.yeptwo.forge;

import cc.unilock.yeptwo.YepTwo;
import cc.unilock.yeptwo.networking.PacketSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(YepTwo.MOD_ID)
public class YepTwoForge {
    public YepTwoForge() {
        YepTwo.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        PacketSender.sendAdvancementMessage(event.getEntity(), event.getAdvancement());
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        PacketSender.sendDeathMessage(event.getEntity(), event.getSource());
    }
}
