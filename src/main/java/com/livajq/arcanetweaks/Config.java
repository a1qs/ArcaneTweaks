package com.livajq.arcanetweaks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {
    
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    // =========================================================
    // Definitions
    // =========================================================
    
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_ALLIES;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_END_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_ADEPT_NETHER_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_EXPERT_NETHER_BIOMETAG;
    
    
    static {
        BUILDER.push("General");
        
        EXTRA_ALLIES = BUILDER
                .comment("Entities treated as allies by dread mobs")
                .defineListAllowEmpty(
                        List.of("dreadMobAllies"),
                        List.of(
                                "minecraft:villager",
                                "minecraft:wolf"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        BUILDER.push("Rituals");
        
        RITUAL_END_BIOMETAG = BUILDER.comment("Biome tag that allows the 'End' ritual to be performed").define("ritualEndBiome", "forge:is_plains");
        RITUAL_ADEPT_NETHER_BIOMETAG = BUILDER.comment("Biome tag that allows the 'Adept Nether' ritual to be performed").define("ritualAdeptNetherBiome", "minecraft:is_forest");
        RITUAL_EXPERT_NETHER_BIOMETAG = BUILDER.comment("Biome tag that allows the 'Expert Nether' ritual to be performed").define("ritualExpertNetherBiome", "minecraft:is_taiga");
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
    
    // =========================================================
    // Runtime values
    // =========================================================
    
    public static Set<String> extraAlliesSet;
    public static TagKey<Biome> ritualEndBiome;
    public static TagKey<Biome> ritualAdeptNetherBiome;
    public static TagKey<Biome> ritualExpertNetherBiome;
    
    // =========================================================
    // Sync
    // =========================================================
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        
        extraAlliesSet = new HashSet<>(EXTRA_ALLIES.get());
        ritualEndBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_END_BIOMETAG.get()));
        ritualAdeptNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_ADEPT_NETHER_BIOMETAG.get()));
        ritualExpertNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_EXPERT_NETHER_BIOMETAG.get()));
    }
}