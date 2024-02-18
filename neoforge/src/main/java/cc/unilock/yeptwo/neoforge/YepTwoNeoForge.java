package cc.unilock.yeptwo.neoforge;

import cc.unilock.yeptwo.PacketSender;
import cc.unilock.yeptwo.YepTwo;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@Mod(YepTwo.MOD_ID)
public class YepTwoNeoForge {
    public YepTwoNeoForge() {
        YepTwo.init();
        NeoForge.EVENT_BUS.register(this);
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
