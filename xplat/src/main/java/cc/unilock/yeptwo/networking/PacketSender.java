package cc.unilock.yeptwo.networking;

import cc.unilock.yeptwo.YepTwo;
import cc.unilock.yeptwo.networking.payload.SimplePayload;
import io.netty.buffer.Unpooled;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.nio.charset.StandardCharsets;

public class PacketSender {
    private static final Identifier YEP_GENERIC = new Identifier("yep", "generic");

    // id : username : displayname : advType : title : description
    private static final String YEP_ADV_FORMAT = "%s␞%s␟%s␟%s␟%s␟%s";
    // id : username : displayname : message
    private static final String YEP_DEATH_FORMAT = "%s␞%s␟%s␟%s";

    private static final Identifier YEP_ADVANCEMENT = new Identifier("yep", "advancement");
    private static final Identifier YEP_DEATH = new Identifier("yep", "death");

    private static final String YEP_ADV_DEFAULT = "DEFAULT";
    private static final String YEP_ADV_GOAL = "GOAL";
    private static final String YEP_ADV_TASK = "TASK";
    private static final String YEP_ADV_CHALLENGE = "CHALLENGE";


    public static void sendAdvancementMessage(PlayerEntity player, AdvancementEntry advancement) {
        if (player instanceof ServerPlayerEntity spe) {
            var display = advancement.value().display().orElse(null);

            if (spe.getAdvancementTracker().getProgress(advancement).isDone()
                    && display != null
                    && display.shouldAnnounceToChat()
                    && spe.getWorld().getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)
            ) {
                var username = spe.getName().getString();
                var displayname = spe.getDisplayName().getString();
                var title = display.getTitle().getString();
                var description = display.getDescription().getString();

                String advType = switch (display.getFrame()) {
                    case CHALLENGE -> YEP_ADV_CHALLENGE;
                    case GOAL -> YEP_ADV_GOAL;
                    case TASK -> YEP_ADV_TASK;
                    default -> YEP_ADV_DEFAULT;
                };

                String msg = String.format(YEP_ADV_FORMAT, YEP_ADVANCEMENT, username, displayname, advType, title, description);
                sendMessage(spe, msg);
            }
        }
    }

    public static void sendDeathMessage(Entity entity, DamageSource source) {
        if (entity instanceof ServerPlayerEntity spe) {
            var username = spe.getName().getString();
            var displayname = spe.getDisplayName().getString();
            var message = source.getDeathMessage(spe).getString();

            String msg = String.format(YEP_DEATH_FORMAT, YEP_DEATH, username, displayname, message);
            sendMessage(spe, msg);
        }
    }

    private static void sendMessage(ServerPlayerEntity player, String msg) {
        YepTwo.LOGGER.debug("Sending message \""+msg+"\" for player \""+player.getName().getString()+"\"");

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(new SimplePayload(YEP_GENERIC, buf)));
    }
}
