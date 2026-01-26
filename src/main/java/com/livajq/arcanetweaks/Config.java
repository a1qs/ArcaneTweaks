package com.livajq.arcanetweaks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {
    
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    // =========================================================
    // Definitions
    // =========================================================
    
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_ALLIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_PLANT_SURFACES;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_END_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_ADEPT_NETHER_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_EXPERT_NETHER_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> APOSTLE_SUPPERBOSS_BIOME;
    
    static {
        BUILDER.push("Dread Mobs");
        
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
        BUILDER.push("canSustainPlant Rules");
        BUILDER.comment("The following can be used to add additional blocks for plants to be placed on");
        
        EXTRA_PLANT_SURFACES = BUILDER
                .comment(
                        "Format: plant - block1, block2, block3...",
                        "Example: betterend:dragon_tree_sapling - minecraft:stone, minecraft:grass_block, minecraft:end_stone"
                )
                .defineListAllowEmpty(
                        List.of("extraPlantSurfaces"),
                        List.of("betterend:dragon_tree_sapling - minecraft:stone, minecraft:grass_block, minecraft:end_stone",
                                "betterend:tenanea_sapling - minecraft:obsidian, minecraft:prismarine"
                        ),
                        o -> o instanceof String
                );
       
        BUILDER.pop();
        
        BUILDER.push("Apostle");
        
        APOSTLE_SUPPERBOSS_BIOME = BUILDER.comment("Biome in which a special variant of the Apostle boss can spawn").define("apostleSuperbossBiome", "biomesoplenty:volcano");
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
    
    // =========================================================
    // Runtime values
    // =========================================================
    
    public static Set<String> extraAlliesSet;
    public static Map<ResourceLocation, Set<ResourceLocation>> extraPlantSurfaces = new HashMap<>();
    public static TagKey<Biome> ritualEndBiome;
    public static TagKey<Biome> ritualAdeptNetherBiome;
    public static TagKey<Biome> ritualExpertNetherBiome;
    public static ResourceKey<Biome> apostleSuperbossBiome;
    
    // =========================================================
    // Sync
    // =========================================================
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        
        extraAlliesSet = new HashSet<>(EXTRA_ALLIES.get());
        extraPlantSurfaces = parsePlantSurfaces();
        ritualEndBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_END_BIOMETAG.get()));
        ritualAdeptNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_ADEPT_NETHER_BIOMETAG.get()));
        ritualExpertNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_EXPERT_NETHER_BIOMETAG.get()));
        apostleSuperbossBiome = ResourceKey.create(Registries.BIOME, new ResourceLocation(Config.APOSTLE_SUPPERBOSS_BIOME.get()));
    }
    
    // =========================================================
    // Helpers
    // =========================================================
    
    private static Map<ResourceLocation, Set<ResourceLocation>> parsePlantSurfaces() {
        Map<ResourceLocation, Set<ResourceLocation>> map = new HashMap<>();
        
        for (String line : EXTRA_PLANT_SURFACES.get()) {
            String[] split = line.split("-");
            if (split.length != 2) continue;
            
            ResourceLocation plantId = new ResourceLocation(split[0].trim());
            
            Set<ResourceLocation> blocks = Arrays.stream(split[1].split(","))
                    .map(String::trim)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet());
            
            map.put(plantId, blocks);
        }
        return map;
    }
}