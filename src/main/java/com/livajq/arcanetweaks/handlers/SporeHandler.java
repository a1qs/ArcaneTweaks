package com.livajq.arcanetweaks.handlers;

import com.Harbinger.Spore.Sitems.BaseWeapons.*;
import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.util.SporeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class SporeHandler {
    private static final TagKey<EntityType<?>> FUNGUS_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("spore", "fungus_entities"));
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        
        boolean involvesSpore = attacker != null && victim != null && (isSporeMob(victim) || isSporeMob(attacker));
        if (!involvesSpore) return;
        
        if (attacker instanceof ForsakenEntity) event.setAmount((float) (event.getAmount() * Config.forsakenSporeDamageDealt));
        else if (victim instanceof ForsakenEntity) event.setAmount((float) (event.getAmount() * Config.forsakenSporeDamageTaken));
    }
    
    /*
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        
        float damage = event.getAmount();
        
        ItemStack helmet = victim.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = victim.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = victim.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = victim.getItemBySlot(EquipmentSlot.FEET);
        
        ArrayList<ItemStack> validPieces = new ArrayList<>();
        if (isValidArmor(helmet)) validPieces.add(helmet);
        if (isValidArmor(chestplate)) validPieces.add(chestplate);
        if (isValidArmor(leggings)) validPieces.add(leggings);
        if (isValidArmor(boots)) validPieces.add(boots);
        
        if (validPieces.isEmpty()) return;
        
        else damage = SporeUtils.getDamageForMutation(validPieces, event.getSource(), damage);
        
        event.setAmount(damage);
    }
     */
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (event.side.isClient()) return;
        
        Player player = event.player;
        if (player.tickCount % 100 != 0) return;
        
        SporeUtils.applyArmorMutationEffects(player);
    }
    
    @SubscribeEvent
    public static void onLivingHurt2(LivingHurtEvent event) {
        Entity entity = event.getSource().getEntity();
        if (!(entity instanceof LivingEntity attacker)) return;
        
        if (!(attacker.getMainHandItem().getItem() instanceof SporeWeaponData) && !(attacker.getOffhandItem().getItem() instanceof SporeWeaponData)) return;
        
        SporeUtils.applyWeaponMutationEffects(attacker);
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item instanceof SporeBaseArmor || item instanceof SporeSwordBase) return;
        
        String name;
        
        if (item instanceof SporeArmorData armorData) {
            SporeArmorMutations mutation = armorData.getVariant(stack);
            if (mutation == SporeArmorMutations.DEFAULT) return;
            name = mutation.getName();
        }
        else if (item instanceof SporeWeaponData weaponData) {
            SporeToolsMutations mutation = weaponData.getVariant(stack);
            if (mutation == SporeToolsMutations.DEFAULT) return;
            name = mutation.getName();
        }
        else return;
        
        List<Component> tooltip = event.getToolTip();
        
        Component line = Component.literal("Mutation: ")
                .append(Component.translatable(name))
                .withStyle(ChatFormatting.DARK_GREEN);
        
        tooltip.add(1, line);
    }
    
    private static boolean isSporeMob(Entity entity) {
        return entity.getType().is(FUNGUS_ENTITIES);
    }
   
    private static boolean isValidArmor(ItemStack armor) {
        return !(armor.getItem() instanceof SporeBaseArmor) && armor.getItem() instanceof SporeArmorData;
    }
}