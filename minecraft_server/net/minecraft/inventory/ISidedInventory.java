package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ISidedInventory extends IInventory {
   int[] getSlotsForFace(EnumFacing side);

   boolean canInsertItem(int slotIn, ItemStack itemStackIn, EnumFacing direction);

   boolean canExtractItem(int slotId, ItemStack stack, EnumFacing direction);
}
