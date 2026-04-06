package com.livajq.arcanetweaks.packet;

import com.alrex.parcool.common.capability.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseParcoolStaminaServerPacket {
    private final int amount;
    
    public UseParcoolStaminaServerPacket(int amount) {
        this.amount = amount;
    }
    
    public static UseParcoolStaminaServerPacket decode(FriendlyByteBuf buf) {
        return new UseParcoolStaminaServerPacket(buf.readInt());
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(amount);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            
            player.getCapability(Capabilities.STAMINA_CAPABILITY).ifPresent(stamina -> {
                stamina.consume(amount);
            });
        });
        
        ctx.get().setPacketHandled(true);
    }
}
