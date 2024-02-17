package cc.unilock.yeptwo.fabric;

import cc.unilock.yeptwo.PacketSender;
import cc.unilock.yeptwo.YepTwo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

public class YepTwoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YepTwo.init();
        ServerLivingEntityEvents.AFTER_DEATH.register(PacketSender::sendDeathMessage);
    }
}
