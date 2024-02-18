package cc.unilock.yeptwo;

import io.netty.buffer.Unpooled;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
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

    private static final String YEP_ADV_DEFAULT = "default";
    private static final String YEP_ADV_GOAL = "goal";
    private static final String YEP_ADV_TASK = "task";
    private static final String YEP_ADV_CHALLENGE = "challenge";


    public static void sendAdvancementMessage(PlayerEntity player, Advancement advancement) {
        if (player instanceof ServerPlayerEntity spe) {
            var display = advancement.getDisplay();

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

        PacketByteBuf payload = new PacketByteBuf(Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8)));
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(YEP_GENERIC, payload));
    }
}
