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

    private static final String VCD_ADV_FORMAT = "%s␞%s␟%s␟%s";
    private static final String VCD_DEATH_FORMAT = "%s␞%s␟%s";

    private static final Identifier VCD_ADV_DEFAULT = new Identifier("vcd", "adv_default");
    private static final Identifier VCD_ADV_GOAL = new Identifier("vcd", "adv_goal");
    private static final Identifier VCD_ADV_TASK = new Identifier("vcd", "adv_task");
    private static final Identifier VCD_ADV_CHALLENGE = new Identifier("vcd", "adv_challenge");

    private static final Identifier VCD_DEATH = new Identifier("vcd", "death");

    public static void sendAdvancementMessage(PlayerEntity player, Advancement advancement) {
        if (player instanceof ServerPlayerEntity spe) {
            var display = advancement.getDisplay();

            if (spe.getAdvancementTracker().getProgress(advancement).isDone()
                    && display != null
                    && display.shouldAnnounceToChat()
                    && spe.getWorld().getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)
            ) {
                var username = spe.getDisplayName().getString();
                var title = display.getTitle().getString();
                var description = display.getDescription().getString();

                Identifier yepId = switch (display.getFrame()) {
                    case CHALLENGE -> VCD_ADV_CHALLENGE;
                    case GOAL -> VCD_ADV_GOAL;
                    case TASK -> VCD_ADV_TASK;
                    default -> VCD_ADV_DEFAULT;
                };

                String msg = String.format(VCD_ADV_FORMAT, yepId, username, title, description);
                sendMessage(spe, msg);
            }
        }
    }

    public static void sendDeathMessage(Entity entity, DamageSource source) {
        if (entity instanceof ServerPlayerEntity spe) {
            var username = spe.getDisplayName().getString();
            var message = source.getDeathMessage(spe).getString();

            String msg = String.format(VCD_DEATH_FORMAT, VCD_DEATH, username, message);
            sendMessage(spe, msg);
        }
    }

    private static void sendMessage(ServerPlayerEntity player, String msg) {
        PacketByteBuf payload = new PacketByteBuf(Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8)));

        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(YEP_GENERIC, payload));
    }
}
