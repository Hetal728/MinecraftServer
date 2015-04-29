package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface ICrafting {
   void updateCraftingInventory(Container p_71110_1_, List p_71110_2_);

   void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_);

   void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_);

   void func_175173_a(Container p_175173_1_, IInventory p_175173_2_);
}
