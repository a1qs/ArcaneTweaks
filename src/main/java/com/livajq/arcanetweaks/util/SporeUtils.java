package com.livajq.arcanetweaks.util;

import com.Harbinger.Spore.Core.Sitems;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorData;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeArmorMutations;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeBaseArmor;
import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.livajq.arcanetweaks.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SporeUtils {
    
    public static ImmutableMultimap.Builder<Attribute, AttributeModifier> applySporeAttributes(EquipmentSlot slot, ArmorItem armor, ItemStack stack, Multimap<Attribute, AttributeModifier> modifiers) {
        
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(modifiers);
        
        if (slot != armor.getType().getSlot()) return builder;
        if (armor instanceof SporeBaseArmor) return builder;
        if (!(armor instanceof SporeArmorData data)) return builder;
        
        SporeArmorMutations variant = data.getVariant(stack);
        if (variant == SporeArmorMutations.DEFAULT) return builder;
        
        UUID uuid = UUID.nameUUIDFromBytes(("mutation_" + slot).getBytes());
        
        //double baseArmor = armor.getDefense();
        //double armorValue = data.calculateTrueDefense(stack, baseArmor) + modifyProtection(stack, baseArmor, data);
        //if (armorValue != 0.0D) builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", armorValue, AttributeModifier.Operation.ADDITION));
        //
        //double baseToughness = armor.getToughness();
        //double toughnessValue = data.calculateTrueToughness(stack, baseToughness) + modifyToughness(stack, baseToughness, data);
        //if (toughnessValue != 0.0D) builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor modifier", toughnessValue, AttributeModifier.Operation.ADDITION));
        
        if (variant == SporeArmorMutations.DROWNED) builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, "Mutation swim speed", 0.25F, AttributeModifier.Operation.ADDITION));
        
        if (variant == SporeArmorMutations.REINFORCED || variant == SporeArmorMutations.SKELETAL) {
            double amount = (variant == SporeArmorMutations.REINFORCED) ? -0.01D : 0.01D;
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Mutation movement speed", amount, AttributeModifier.Operation.ADDITION));
        }
        
        return builder;
    }
    
    public static double modifyProtection(ItemStack stack, double value, SporeArmorData data) {
        SporeArmorMutations variant = data.getVariant(stack);
        if (variant == SporeArmorMutations.REINFORCED) return value * 0.2F;
        else if (variant == SporeArmorMutations.SKELETAL) return value * -0.2F;
        else return 0.0F;
    }
    
    public static double modifyToughness(ItemStack stack, double value, SporeArmorData data) {
        return data.getVariant(stack) == SporeArmorMutations.SKELETAL ? 1.0F : 0.0F;
    }
    
    public static float getDamageForMutation(ArrayList<ItemStack> pieces, DamageSource source, float value) {
        float amplifier = 1.0F;
        for (ItemStack piece : pieces) {
            if (!(piece.getItem() instanceof SporeArmorData data)) continue;
            if (data.getVariant(piece) == SporeArmorMutations.CHARRED && source.is(DamageTypeTags.IS_FIRE)) amplifier -= 0.25F;
            else if (data.getVariant(piece) == SporeArmorMutations.DROWNED && source.is(DamageTypeTags.IS_FIRE)) amplifier += 0.25F;
        }
        
        return value * amplifier;
    }
    
    public static Integer getArmorColor(ItemStack stack) {
        if (stack.getItem() instanceof SporeArmorData data) return data.getVariant(stack).getColor();
        else return null;
    }
    
    public static void setBonusForArmorMutation(Player player) {
        Map<ArmorMutations, Integer> mutations = new HashMap<>();
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            ItemStack piece = player.getItemBySlot(slot);
            
            if (!(piece.getItem() instanceof SporeArmorData data)) continue;
            if (data.getVariant(piece) == null) continue;
            
            ArmorMutations mutation = ArmorMutations.getMutation(piece, data);
            if (mutation == null) continue;
            
            mutations.merge(mutation, 1, Integer::sum);
        }
        
        mutations.forEach((mutation, count) -> {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(mutation.id);
            if (effect == null) return;
            
            int finalAmplifier = (mutation.amplifier + 1) * count - 1;
            player.addEffect(new MobEffectInstance(effect, mutation.duration, finalAmplifier, false, true));
        });
    }
    
    private enum ArmorMutations {
        REINFORCED(Config.sporeMutationEffectReinforced),
        SKELETAL(Config.sporeMutationEffectSkeletal),
        DROWNED(Config.sporeMutationEffectDrowned),
        CHARRED(Config.sporeMutationEffectCharred);
        
        private final ResourceLocation id;
        private final int duration;
        private final int amplifier;
        
        ArmorMutations(Config.SporeMutationEffect effect) {
            id = effect.id();
            duration = effect.duration();
            amplifier = effect.amplifier();
        }
        
        public static ArmorMutations getMutation(ItemStack item, SporeArmorData data) {
            SporeArmorMutations sporeMutation = data.getVariant(item);
            return switch (sporeMutation) {
                case REINFORCED -> REINFORCED;
                case SKELETAL -> SKELETAL;
                case DROWNED -> DROWNED;
                case CHARRED -> CHARRED;
                default -> null;
            };
        }
    }
}