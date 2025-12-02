package cc.unilock.yeptwo.networking.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public record YepPayload(String msg) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, YepPayload> CODEC = CustomPacketPayload.codec(YepPayload::write, YepPayload::new);
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("yep", "generic");
    public static final Type<YepPayload> TYPE = new Type<>(ID);

    public YepPayload(FriendlyByteBuf buf) {
        this("null");
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
