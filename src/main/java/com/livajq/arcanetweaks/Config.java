package com.livajq.arcanetweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
    //#####################################################################//
    
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_ALLIES =
            BUILDER.comment("Entities treated as allies by dread mobs")
                    .defineListAllowEmpty(
                            List.of("dreadMobAllies"),
                            List.of(
                                    "minecraft:villager",
                                    "minecraft:zombie"
                            ),
                            o -> o instanceof String
                    );
    
    //#####################################################################//
    
    static final ForgeConfigSpec SPEC = BUILDER.build();
    
    
    
    public static Set<String> extraAlliesSet = new HashSet<>();
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        extraAlliesSet = new HashSet<>(EXTRA_ALLIES.get());
    }
}