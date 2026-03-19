package com.livajq.arcanetweaks;

import com.Polarice3.Goety.utils.MathHelper;
import com.livajq.arcanetweaks.mobs.MobStats;
import net.bandit.reskillable.common.commands.skills.SkillAttributeBonus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
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
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_ATTRIBUTE_MODIFIERS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_FREEZE_IMMUNITY;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMI_RECIPE_CATEGORY_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> EMI_RECIPE_WHITELIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> DEATH_MESSAGES;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> DRAGON_NUKE_IMMUNE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> LOST_CITIES_DOORS;
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
    private static final ForgeConfigSpec.ConfigValue<Integer> WORLDGEN_TYPE;
    private static final ForgeConfigSpec.ConfigValue<Double> OBLITERATOR_DAMAGE_CAP;
    private static final ForgeConfigSpec.ConfigValue<Double> OBLITERATOR_GROUND_NUKE_DAMAGE_FLAT;
    private static final ForgeConfigSpec.ConfigValue<Double> OBLITERATOR_GROUND_NUKE_DAMAGE_PERCENT;
    private static final ForgeConfigSpec.ConfigValue<Double> RESISTANCE_AMOUNT;
    private static final ForgeConfigSpec.ConfigValue<Double> FIRE_RESISTANCE_AMOUNT;
    private static final ForgeConfigSpec.ConfigValue<Integer> HARDCORE_LIVES_COUNT;
    private static final ForgeConfigSpec.ConfigValue<Boolean> HARDCORE_ICON_VISIBLE;
    private static final ForgeConfigSpec.ConfigValue<Integer> HARDCORE_ICON_SIZE;
    private static final ForgeConfigSpec.ConfigValue<Double> HARDCORE_ICON_POSX;
    private static final ForgeConfigSpec.ConfigValue<Double> HARDCORE_ICON_POSY;
    private static final ForgeConfigSpec.ConfigValue<Double> SEA_SERPENT_REACH;
    private static final ForgeConfigSpec.ConfigValue<Integer> DRAGON_NUKE_COLOR_FIRE;
    private static final ForgeConfigSpec.ConfigValue<Integer> DRAGON_NUKE_COLOR_ICE;
    private static final ForgeConfigSpec.ConfigValue<Integer> DRAGON_NUKE_COLOR_LIGHTNING;
    private static final ForgeConfigSpec.ConfigValue<Double> FORSAKEN_SPORE_DMG_DEALT;
    private static final ForgeConfigSpec.ConfigValue<Double> FORSAKEN_SPORE_DMG_TAKEN;
    
    static {
        BUILDER.push("Mobs");
        
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
        
        BUILDER.push("Mob freeze immunity");
        MOB_FREEZE_IMMUNITY = BUILDER
                .comment("Mobs immune to the freeze effect")
                .defineListAllowEmpty(
                        List.of("mobFreezeImmunity"),
                        List.of(
                                "minecraft:villager",
                                "minecraft:wolf"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        
        BUILDER.push("Mob attribute modifiers");
        BUILDER.comment("Attribute (and tick speed) multipliers for mobs. 1.0 is default (100%) scaling");
        
        MOB_ATTRIBUTE_MODIFIERS = BUILDER
                .comment("Format: id - stat1=val stat2=val stat3=val",
                        "Example: iceandfire:dread_beast - attack=1.3 armor=0.5 health=1.2 speed=2.0 follow=1.35 tick=1.25"
                )
                .defineListAllowEmpty(
                        List.of("mobAttributeModifiers"),
                        List.of(
                                "iceandfire:dread_beast - attack=1.3 armor=0.5 health=1.2 speed=2.0 follow=1.35",
                                "minecraft:wolf - health=2.0"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        
        BUILDER.push("Dragons");
        
        DRAGON_NUKE_COLOR_FIRE = BUILDER.comment("Fire dragon nuke color").defineInRange("dragonNukeColorFire", 0xD30E0E, 0x000000, 0xFFFFFF);
        DRAGON_NUKE_COLOR_ICE = BUILDER.comment("Ice dragon nuke color").defineInRange("dragonNukeColorIce", 0x0AB8EA, 0x000000, 0xFFFFFF);
        DRAGON_NUKE_COLOR_LIGHTNING = BUILDER.comment("Lightning dragon nuke color").defineInRange("dragonNukeColorLightning", 0xFF00EA, 0x000000, 0xFFFFFF);
        
        DRAGON_NUKE_IMMUNE = BUILDER
                .comment("Mobs immune to dragon nuke"
                )
                .defineListAllowEmpty(
                        List.of("dragonNukeImmune"),
                        List.of(
                                "minecraft:villager",
                                "minecraft:wolf"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        
        BUILDER.push("Forsaken");
        
        FORSAKEN_SPORE_DMG_DEALT = BUILDER.comment("Multiplier for the damage dealt to Spore mobs by Forsaken").define("forsakenSporeDmgDealt", 15.0D);
        FORSAKEN_SPORE_DMG_TAKEN = BUILDER.comment("Multiplier for the damage taken by Forsaken from Spore mobs").define("forsakenSporeDmgTaken", 0.05D);
        
        BUILDER.pop();
        
        SEA_SERPENT_REACH = BUILDER.comment("Attack reach bonus for sea serpents. 0.5 = 50% extra reach etc.").define("seaSerpentReach", 1.0D);
        
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
        
        BUILDER.push("Bosses");
        
        BUILDER.push("Apostle");
        APOSTLE_SUPPERBOSS_BIOME = BUILDER.comment("Biome in which a special variant of the Apostle boss can spawn").define("apostleSuperbossBiome", "biomesoplenty:volcano");
        BUILDER.pop();
        
        BUILDER.push("Obliterator");
        OBLITERATOR_DAMAGE_CAP = BUILDER.comment("Maximum damage the Obliterator boss can receive per hit").define("obliteratorDamageCap", 200.0D);
        OBLITERATOR_GROUND_NUKE_DAMAGE_FLAT = BUILDER.comment("Flat damage bonus for the ground nuke attack. Also affects the Tesseract").define("obliteratorGroundNukeDamageFlat", 100.0D);
        OBLITERATOR_GROUND_NUKE_DAMAGE_PERCENT = BUILDER.comment("Bonus damage based on target's maximum health for the ground nuke attack. 0.5 = 50% of target's max health dealt as extra damage. Also affects the Tesseract").define("obliteratorGroundNukeDamagePercent", 0.1D);
        BUILDER.pop();
        
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
        
        BUILDER.push("EMI");
        
        EMI_RECIPE_CATEGORY_BLACKLIST = BUILDER
                .comment("Blacklisted EMI recipe categories. Categories on this list will not show up in-game")
                .defineListAllowEmpty(
                        List.of("emiRecipeCategoryBlacklist"),
                        List.of(
                                "emi:anvil_repairing",
                                "irons_spellbooks:arcane_anvil",
                                "emi:grinding"
                        ),
                        o -> o instanceof String
                );
        
        EMI_RECIPE_WHITELIST = BUILDER
                .comment("EMI recipes that should be added even if their category is blacklisted",
                        "NOTE: Substring based, not id based (or it would've been extremely painful)",
                        "'iron_sword' will whitelist all iron sword recipes. repairing/minecraft/ will whitelist vanilla anvil repairing etc. etc.")
                .defineListAllowEmpty(
                        List.of("emiRecipeWhitelist"),
                        List.of(
                                "iron_sword",
                                "grind",
                                "minecraft:golden_apple"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        
        BUILDER.push("Hardcore");
        
        HARDCORE_LIVES_COUNT = BUILDER.comment("Amount of lives the player has in hardcore mode").define("hardcoreLivesCount", 5);
        HARDCORE_ICON_VISIBLE = BUILDER.comment("Should the hardcore heart icon be displayed on the screen").define("hardcoreIconVisible", true);
        HARDCORE_ICON_SIZE = BUILDER.comment("Hardcore icon size").define("hardcoreIconSize", 16);
        HARDCORE_ICON_POSX = BUILDER.comment("Hardcore icon position X (0-100%)").define("hardcoreIconPosX", 2.0D);
        HARDCORE_ICON_POSY = BUILDER.comment("Hardcore icon position Y (0-100%)").define("hardcoreIconPosY", 2.0D);
        
        BUILDER.pop();
        
        BUILDER.push("Misc");
        
        WORLDGEN_TYPE = BUILDER
                .comment("World generation type",
                        "0: Default (vanilla), 1: Arcane, 2: Biome Blend")
                .define("worldgenType", 1);
        
        RESISTANCE_AMOUNT = BUILDER.comment("Damage reduced by the resistance effect per level (0 - 1)").define("resistanceAmount", 0.1D);
        FIRE_RESISTANCE_AMOUNT = BUILDER.comment("Fire damage reduced by the fire resistance effect per level (0 - 1)").define("fireResistanceAmount", 0.1D);
        
        DEATH_MESSAGES = BUILDER
                .comment("Extra messages that appear on the death screen")
                .defineListAllowEmpty(
                        List.of("deathMessages"),
                        List.of(
                                "Rip bozo",
                                "Most unfortunate",
                                "When monument?"
                        ),
                        o -> o instanceof String
                );
        
        LOST_CITIES_DOORS = BUILDER
                .comment("List of doors to choose from when generating Lost Cities buildings",
                        "The same ID can be included multiple times to make it more common e.g. 'oak', 'oak', 'spruce' = 66% for oak, 33% for spruce door")
                .defineListAllowEmpty(
                        List.of("lostCitiesDoors"),
                        List.of(
                                "minecraft:oak_door",
                                "minecraft:birch_door",
                                "minecraft:iron_door"
                        ),
                        o -> o instanceof String
                );
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
    // =========================================================
    // Runtime values
    // =========================================================
    
    public static Set<String> extraAlliesSet;
    public static Set<String> foodTemperatureImmunitySet;
    public static Set<String> foodThirstImmunitySet;
    public static Set<String> mobFreezeImmunitySet;
    public static Set<String> emiRecipeCategoryBlacklistSet;
    public static Set<String> emiRecipeWhitelistSet;
    public static Set<String> dragonNukeImmuneSet;
    public static Map<ResourceLocation, Set<ResourceLocation>> extraPlantSurfaces = new HashMap<>();
    public static Map<SkillAttributeBonus, Supplier<Attribute>> reskillableAttributeBonuses = new HashMap<>();
    public static Map<EntityType<?>, MobStats> mobAttributeModifiers = new HashMap<>();
    public static List<String> deathMessages;
    public static List<String> lostCitiesDoors;
    public static TagKey<Biome> ritualEndBiome;
    public static TagKey<Biome> ritualAdeptNetherBiome;
    public static TagKey<Biome> ritualExpertNetherBiome;
    public static ResourceKey<Biome> apostleSuperbossBiome;
    public static int worldgenType;
    public static double obliteratorDamageCap;
    public static double obliteratorGroundNukeDamageFlat;
    public static double obliteratorGroundNukeDamagePercent;
    public static double resistanceAmount;
    public static double fireResistanceAmount;
    public static int hardcoreLivesCount;
    public static boolean hardcoreIconVisible;
    public static int hardcoreIconSize;
    public static double hardcoreIconPosX;
    public static double hardcoreIconPosY;
    public static double seaSerpentReach;
    public static int dragonNukeColorFire;
    public static int dragonNukeColorIce;
    public static int dragonNukeColorLightning;
    public static double forsakenSporeDamageDealt;
    public static double forsakenSporeDamageTaken;
    
    // =========================================================
    // Sync
    // =========================================================
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        
        extraAlliesSet = new HashSet<>(EXTRA_ALLIES.get());
        foodTemperatureImmunitySet = new HashSet<>(FOOD_TEMPERATURE_IMMUNITY.get());
        foodThirstImmunitySet = new HashSet<>(FOOD_THIRST_IMMUNITY.get());
        mobFreezeImmunitySet = new HashSet<>(MOB_FREEZE_IMMUNITY.get());
        emiRecipeCategoryBlacklistSet = new HashSet<>(EMI_RECIPE_CATEGORY_BLACKLIST.get());
        emiRecipeWhitelistSet = new HashSet<>(EMI_RECIPE_WHITELIST.get());
        dragonNukeImmuneSet = new HashSet<>(DRAGON_NUKE_IMMUNE.get());
        extraPlantSurfaces = parsePlantSurfaces();
        reskillableAttributeBonuses = parseReskillableBonuses();
        mobAttributeModifiers = parseMobAttributeModifiers();
        deathMessages = new ArrayList<>(DEATH_MESSAGES.get());
        lostCitiesDoors = new ArrayList<>(LOST_CITIES_DOORS.get());
        ritualEndBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_END_BIOMETAG.get()));
        ritualAdeptNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_ADEPT_NETHER_BIOMETAG.get()));
        ritualExpertNetherBiome = TagKey.create(Registries.BIOME, new ResourceLocation(RITUAL_EXPERT_NETHER_BIOMETAG.get()));
        apostleSuperbossBiome = ResourceKey.create(Registries.BIOME, new ResourceLocation(APOSTLE_SUPPERBOSS_BIOME.get()));
        worldgenType = Mth.clamp(WORLDGEN_TYPE.get(), 0, 2);
        obliteratorDamageCap = OBLITERATOR_DAMAGE_CAP.get();
        obliteratorGroundNukeDamageFlat = OBLITERATOR_GROUND_NUKE_DAMAGE_FLAT.get();
        obliteratorGroundNukeDamagePercent = OBLITERATOR_GROUND_NUKE_DAMAGE_PERCENT.get();
        resistanceAmount = RESISTANCE_AMOUNT.get();
        fireResistanceAmount = FIRE_RESISTANCE_AMOUNT.get();
        hardcoreLivesCount = Math.max(HARDCORE_LIVES_COUNT.get(), 1);
        hardcoreIconVisible = HARDCORE_ICON_VISIBLE.get();
        hardcoreIconSize = HARDCORE_ICON_SIZE.get();
        hardcoreIconPosX = MathHelper.clamp(HARDCORE_ICON_POSX.get(), 0.0D, 99.0D);
        hardcoreIconPosY  = MathHelper.clamp(HARDCORE_ICON_POSY.get(), 0.0D, 99.0D);
        seaSerpentReach = SEA_SERPENT_REACH.get();
        dragonNukeColorFire = DRAGON_NUKE_COLOR_FIRE.get();
        dragonNukeColorIce = DRAGON_NUKE_COLOR_ICE.get();
        dragonNukeColorLightning = DRAGON_NUKE_COLOR_LIGHTNING.get();
        forsakenSporeDamageDealt = FORSAKEN_SPORE_DMG_DEALT.get();
        forsakenSporeDamageTaken = FORSAKEN_SPORE_DMG_TAKEN.get();
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
        
        parseSkill(map, SkillAttributeBonus.ATTACK, RESKILLABLE_ATTACK_BONUS.get());
        parseSkill(map, SkillAttributeBonus.GATHERING, RESKILLABLE_GATHERING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.MINING, RESKILLABLE_MINING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.FARMING, RESKILLABLE_FARMING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.BUILDING, RESKILLABLE_BUILDING_BONUS.get());
        parseSkill(map, SkillAttributeBonus.DEFENSE, RESKILLABLE_DEFENSE_BONUS.get());
        parseSkill(map, SkillAttributeBonus.AGILITY, RESKILLABLE_AGILITY_BONUS.get());
        parseSkill(map, SkillAttributeBonus.MAGIC, RESKILLABLE_MAGIC_BONUS.get());
        
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
            ArcaneTweaks.LOGGER.warn("[ArcaneTweaks] Invalid attribute ID in config: " + id + " for skill " + bonus.name());
            map.put(bonus, () -> null);
            return;
        }
        
        map.put(bonus, () -> attr);
    }
    
    private static Map<EntityType<?>, MobStats> parseMobAttributeModifiers() {
        Map<EntityType<?>, MobStats> map = new HashMap<>();
        
        for (String line : MOB_ATTRIBUTE_MODIFIERS.get()) {
            if (!line.contains("-")) continue;
            
            String[] split = line.split("-", 2);
            String idPart = split[0].trim();
            String statsPart = split[1].trim();
            
            ResourceLocation id = new ResourceLocation(idPart);
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
            if (type == null) {
                ArcaneTweaks.LOGGER.warn("[ArcaneTweaks] Unknown entity type in config: " + id);
                continue;
            }
            
            double attack = 1, armor = 1, health = 1, speed = 1, follow = 1, tick = 1;
            
            for (String token : statsPart.split(" ")) {
                if (!token.contains("=")) continue;
                
                String[] kv = token.split("=", 2);
                String key = kv[0];
                double val = Double.parseDouble(kv[1]);
                
                switch (key) {
                    case "attack" -> attack = val;
                    case "armor" -> armor = val;
                    case "health" -> health = val;
                    case "speed" -> speed = val;
                    case "follow" -> follow = val;
                    case "tick" -> tick = val;
                }
            }
            
            map.put(type, new MobStats(attack, armor, health, speed, follow, tick));
        }
        return map;
    }
}