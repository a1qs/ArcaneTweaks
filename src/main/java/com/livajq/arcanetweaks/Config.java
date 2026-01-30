package com.livajq.arcanetweaks;

import net.bandit.reskillable.common.commands.skills.SkillAttributeBonus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;
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
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FOOD_TEMPERATURE_IMMUNITY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> FOOD_THIRST_IMMUNITY;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_END_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_ADEPT_NETHER_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> RITUAL_EXPERT_NETHER_BIOMETAG;
    private static final ForgeConfigSpec.ConfigValue<String> APOSTLE_SUPPERBOSS_BIOME;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_ATTACK_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_GATHERING_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_MINING_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_FARMING_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_BUILDING_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_DEFENSE_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_AGILITY_BONUS;
    private static final ForgeConfigSpec.ConfigValue<String> RESKILLABLE_MAGIC_BONUS;
    
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
        
        BUILDER.push("Food Bonuses");
        
        FOOD_TEMPERATURE_IMMUNITY = BUILDER
                .comment("Food items that apply temperature immunity")
                .defineListAllowEmpty(
                        List.of("foodTemperatureImmunity"),
                        List.of(
                                "minecraft:bread",
                                "minecraft:apple"
                        ),
                        o -> o instanceof String
                );
        
        FOOD_THIRST_IMMUNITY = BUILDER
                .comment("Food items that apply thirst immunity")
                .defineListAllowEmpty(
                        List.of("foodThirstImmunity"),
                        List.of(
                                "minecraft:carrot",
                                "minecraft:potato"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        
        BUILDER.push("Reskillable attribute bonuses");
        BUILDER.comment("Attribute bonus that should be applied for each skill. NONE for no bonus, DEFAULT to use default (reskillable) ones");
        
        RESKILLABLE_ATTACK_BONUS = BUILDER.define("attack", "minecraft:generic.luck");
        RESKILLABLE_GATHERING_BONUS = BUILDER.define("gathering", "DEFAULT");
        RESKILLABLE_MINING_BONUS = BUILDER.define("mining", "DEFAULT");
        RESKILLABLE_FARMING_BONUS = BUILDER.define("farming", "DEFAULT");
        RESKILLABLE_BUILDING_BONUS = BUILDER.define("building", "DEFAULT");
        RESKILLABLE_DEFENSE_BONUS = BUILDER.define("defense", "DEFAULT");
        RESKILLABLE_AGILITY_BONUS = BUILDER.define("agility", "DEFAULT");
        RESKILLABLE_MAGIC_BONUS = BUILDER.define("magic", "DEFAULT");
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
    
    // =========================================================
    // Runtime values
    // =========================================================
    
    public static Set<String> extraAlliesSet;
    public static Set<String> foodTemperatureImmunitySet;
    public static Set<String> foodThirstImmunitySet;
    public static Map<ResourceLocation, Set<ResourceLocation>> extraPlantSurfaces = new HashMap<>();
    public static Map<SkillAttributeBonus, Supplier<Attribute>> reskillableAttributeBonuses = new HashMap<>();
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
        foodTemperatureImmunitySet = new HashSet<>(FOOD_TEMPERATURE_IMMUNITY.get());
        foodThirstImmunitySet = new HashSet<>(FOOD_THIRST_IMMUNITY.get());
        extraPlantSurfaces = parsePlantSurfaces();
        reskillableAttributeBonuses = parseReskillableBonuses();
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
    
    private static Map<SkillAttributeBonus, Supplier<Attribute>> parseReskillableBonuses() {
        Map<SkillAttributeBonus, Supplier<Attribute>> map = new HashMap<>();
        
        parseSkill(map, SkillAttributeBonus.ATTACK,    RESKILLABLE_ATTACK_BONUS.get());
        parseSkill(map, SkillAttributeBonus.GATHERING, RESKILLABLE_GATHERING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.MINING,    RESKILLABLE_MINING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.FARMING,   RESKILLABLE_FARMING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.BUILDING,  RESKILLABLE_BUILDING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.DEFENSE,   RESKILLABLE_DEFENSE_BONUS.get());
        parseSkill(map, SkillAttributeBonus.AGILITY,   RESKILLABLE_AGILITY_BONUS.get());
        parseSkill(map, SkillAttributeBonus.MAGIC,     RESKILLABLE_MAGIC_BONUS.get());
        
        return map;
    }
    
    private static void parseSkill(Map<SkillAttributeBonus, Supplier<Attribute>> map, SkillAttributeBonus bonus, String id) {
        if (id.equalsIgnoreCase("DEFAULT")) return;
        
        if (id.equalsIgnoreCase("NONE")) {
            map.put(bonus, () -> null);
            return;
        }
        
        ResourceLocation rl = new ResourceLocation(id);
        Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(rl);
        
        if (attr == null) {
            System.err.println("[ArcaneTweaks] Invalid attribute ID in config: " + id + " for skill " + bonus.name());
            map.put(bonus, () -> null);
            return;
        }
        
        map.put(bonus, () -> attr);
    }
}