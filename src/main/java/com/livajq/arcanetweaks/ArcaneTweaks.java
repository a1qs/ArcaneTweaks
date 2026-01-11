package com.livajq.arcanetweaks;

import com.Polarice3.Goety.api.ritual.RitualType;
import com.livajq.arcanetweaks.compat.goety.ritualtype.AdeptNetherCustomRitualType;
import com.livajq.arcanetweaks.compat.goety.ritualtype.EndCustomRitualType;
import com.livajq.arcanetweaks.compat.goety.ritualtype.ExpertNetherCustomRitualType;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ArcaneTweaks.MODID)
public class ArcaneTweaks {
    public static final String MODID = "arcanetweaks";
    public static final String NAME = "ArcaneTweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneTweaks(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + "/arcanetweaks.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            RitualType.addRitualType("end", new EndCustomRitualType());
            RitualType.addRitualType("adept_nether", new AdeptNetherCustomRitualType());
            RitualType.addRitualType("expert_nether", new ExpertNetherCustomRitualType());
        });
    }
    
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
        
        }
    }
}
