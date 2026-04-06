package com.livajq.arcanetweaks.handlers;

import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.init.ArcaneSounds;
import com.livajq.arcanetweaks.init.ArcaneTags;
import com.livajq.arcanetweaks.packet.SyncHardcoreLivesPacket;
import com.livajq.arcanetweaks.packet.UseParcoolStaminaServerPacket;
import com.oblivioussp.spartanweaponry.api.ModToolActions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class SpartanWeaponryHandler {
    
    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (!player.isUsingItem()) return;
        ItemStack stack = player.getUseItem();
        if (stack.isEmpty()) return;
        
        if (!stack.getItem().canPerformAction(stack, ModToolActions.MELEE_BLOCK)) return;

        Entity attackerEntity = event.getSource().getEntity();
        if (!(attackerEntity instanceof LivingEntity attacker)) return;
  
        if (attacker.getType().is(ArcaneTags.DISABLES_MELEE_BLOCK)) {
            player.stopUsingItem();
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    ArcaneSounds.MELEE_BLOCK_BREAK.get(), SoundSource.PLAYERS, 1f, 1f);
            
            int cooldown = Config.blockBreakCooldownBase + (int)(Config.blockBreakCooldownExtra * event.getAmount());
            cooldown = Math.min(Config.blockBreakCooldownMax, cooldown);
            
            player.getCooldowns().addCooldown(stack.getItem(), cooldown);
        }
        
        if (ModList.get().isLoaded("parcool")) {
            int staminaCost = Config.blockStaminaConsumeBase + (int)(Config.blockStaminaConsumeExtra * event.getAmount());
            staminaCost = Math.min(Config.blockStaminaConsumeMax, staminaCost);
            
            PacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new UseParcoolStaminaServerPacket(staminaCost)
            );
        }
    }
}