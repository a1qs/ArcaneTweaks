package com.livajq.arcanetweaks.handlers;


import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.common.capability.bossminion.BossMinionData;
import com.livajq.arcanetweaks.common.capability.bossminion.BossMinionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class CapabilitiesHandler {
    
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Mob mob) {
            BossMinionProvider provider = new BossMinionProvider();
            provider.getBackend().setLevel(mob.level());
            event.addCapability(new ResourceLocation(ArcaneTweaks.MODID, "boss_minion"), provider);
        }
    }
    
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(BossMinionData.class);
    }
}

