package com.livajq.arcanetweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
    //#####################################################################//
    
    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);  //dummy for generating the file
    
    //#####################################################################//
    
    static final ForgeConfigSpec SPEC = BUILDER.build();
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    
    }
}