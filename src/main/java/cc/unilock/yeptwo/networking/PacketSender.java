package cc.unilock.yeptwo.networking;

import cc.unilock.yeptwo.YepTwo;
import cc.unilock.yeptwo.mixin.accessor.AchievementAccessor;
import cc.unilock.yeptwo.mixin.accessor.StatBaseAccessor;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.stats.Achievement;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.nio.charset.StandardCharsets;

public class PacketSender {
    private static final ResourceLocation YEP_GENERIC = new ResourceLocation("yep", "generic");

    // id : username : displayname : advType : title : description
    private static final String YEP_ADV_FORMAT = "%s␞%s␟%s␟%s␟%s␟%s";
    // id : username : displayname : message
    private static final String YEP_DEATH_FORMAT = "%s␞%s␟%s␟%s";

    private static final ResourceLocation YEP_ADVANCEMENT = new ResourceLocation("yep", "advancement");
    private static final ResourceLocation YEP_DEATH = new ResourceLocation("yep", "death");

    private static final String YEP_ADV_DEFAULT = "DEFAULT";

    public static void sendAdvancementMessage(EntityPlayer player, Achievement achievement) {
        if (player instanceof EntityPlayerMP spe) {
            if (spe.func_147099_x().canUnlockAchievement(achievement)
                && !spe.func_147099_x().hasAchievementUnlocked(achievement)
                && spe.mcServer.func_147136_ar()
            ) {
                var username = spe.getCommandSenderName();
                var displayname = spe.getDisplayName();
                var title = ((StatBaseAccessor) achievement).getStatName().getUnformattedTextForChat();
                var description = achievement.statId.equals("achievement.openInventory")
                    ? StatCollector.translateToLocal(((AchievementAccessor) achievement).getAchievementDescription()).replace("%1$s", "E")
                    : StatCollector.translateToLocal(((AchievementAccessor) achievement).getAchievementDescription());

                String msg = String.format(YEP_ADV_FORMAT, YEP_ADVANCEMENT, username, displayname, YEP_ADV_DEFAULT, title, description);
                sendMessage(spe, msg);
            }
        }
    }

    public static void sendDeathMessage(Entity entity, DamageSource source) {
        if (entity instanceof EntityPlayerMP spe) {
            var username = spe.getCommandSenderName();
            var displayname = spe.getDisplayName();
            var message = source.func_151519_b(spe).getUnformattedTextForChat();

            String msg = String.format(YEP_DEATH_FORMAT, YEP_DEATH, username, displayname, message);
            sendMessage(spe, msg);
        }
    }

    public static void sendMessage(EntityPlayerMP player, String msg) {
        YepTwo.LOGGER.debug("Sending message \""+msg+"\" for player \""+player.getCommandSenderName()+"\"");

        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        player.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(YEP_GENERIC.toString(), buf));
    }
}
