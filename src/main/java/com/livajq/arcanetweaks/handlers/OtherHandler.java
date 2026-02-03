package com.livajq.arcanetweaks.handlers;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.init.ArcaneSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class OtherHandler {
    private static final String TAG_PLAYED_SOUND = "arcanetweaks_played_sound";
    private static int messageTime = 0;
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        
        if (player.level().isClientSide) return;
        
        if (messageTime-- > 0) {
            player.displayClientMessage(
                    Component.literal("I did it for me. I liked it. I was good at it. And I was really… I was alive").withStyle(ChatFormatting.BLUE),
                    true
            );
        }
        
        CompoundTag data = player.getPersistentData();
        
        if (player.tickCount < 1200) return;
        
        if (data.getBoolean(TAG_PLAYED_SOUND)) return;
        
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ArcaneSounds.SOUNDHEHE.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
        
        ItemStack stack = new ItemStack(Items.PRISMARINE_CRYSTALS);
        stack.setHoverName(Component.literal("§bPrismarine(?) Crystal"));
        
        CompoundTag display = stack.getOrCreateTagElement("display");
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("99.1% pure").withStyle(ChatFormatting.BLUE))));
        lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("Jesus Christ it's not a rock, it's a mineral...").withStyle(ChatFormatting.RED))));
        display.put("Lore", lore);
        
        boolean added = player.getInventory().add(stack);
        if (!added) player.drop(stack, false);
        
        messageTime = 100;
        
        data.putBoolean(TAG_PLAYED_SOUND, true);
    }
    
    //certain eyes used as dimension teleporters instead
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Item item = event.getItemStack().getItem();
        if (item != ItemInit.ANCIENT_TOMB_EYE.get() && item != ItemInit.BLOODY_ALTAR_EYE.get()) return;
        event.setCanceled(true);
        Player player = event.getEntity();
        if (player.level().isClientSide() || !player.isCrouching()) return;
        
        ServerPlayer serverPlayer = (ServerPlayer) player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        
        ResourceKey<Level> targetDim;
        if (item == ItemInit.ANCIENT_TOMB_EYE.get()) targetDim = Level.NETHER;
        else targetDim = Level.END;
        if (serverPlayer.level().dimension() == targetDim) return;
        
        ServerLevel targetLevel = server.getLevel(targetDim);
        if (targetLevel == null) return;
        
        serverPlayer.changeDimension(targetLevel, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity e = repositionEntity.apply(false);

                BlockPos spawn = destWorld.getSharedSpawnPos();
                Vec3 pos = Vec3.atBottomCenterOf(spawn);
                
                e.teleportTo(pos.x, pos.y, pos.z);
                return e;
            }
        });
    }
}