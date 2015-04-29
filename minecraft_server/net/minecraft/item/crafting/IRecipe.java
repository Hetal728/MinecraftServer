package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRecipe {
   boolean matches(InventoryCrafting p_77569_1_, World worldIn);

   ItemStack getCraftingResult(InventoryCrafting p_77572_1_);

   int getRecipeSize();

   ItemStack getRecipeOutput();

   ItemStack[] func_179532_b(InventoryCrafting p_179532_1_);
}
