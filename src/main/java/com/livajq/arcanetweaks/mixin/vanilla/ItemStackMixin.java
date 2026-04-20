package com.livajq.arcanetweaks.mixin.vanilla;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.livajq.arcanetweaks.util.SporeUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    
    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void spore$injectMutationAttributes(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        Item item = stack.getItem();
        
        if (item instanceof ArmorItem armor) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = SporeUtils.applySporeArmorAttributes(slot, armor, stack, cir.getReturnValue());
            cir.setReturnValue(builder.build());
        }
        
        if (item instanceof SwordItem sword) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = SporeUtils.applySporeWeaponAttributes(slot, sword, stack, cir.getReturnValue());
            cir.setReturnValue(builder.build());
        }
    }
}