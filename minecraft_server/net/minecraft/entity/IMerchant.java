package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant {
   void setCustomer(EntityPlayer p_70932_1_);

   EntityPlayer getCustomer();

   MerchantRecipeList getRecipes(EntityPlayer p_70934_1_);

   void useRecipe(MerchantRecipe p_70933_1_);

   void verifySellingItem(ItemStack p_110297_1_);

   IChatComponent getDisplayName();
}
