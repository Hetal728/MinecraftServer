package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
   int getSizeInventory();

   ItemStack getStackInSlot(int slotIn);

   ItemStack decrStackSize(int index, int count);

   ItemStack getStackInSlotOnClosing(int index);

   void setInventorySlotContents(int index, ItemStack stack);

   int getInventoryStackLimit();

   void markDirty();

   boolean isUseableByPlayer(EntityPlayer playerIn);

   void openInventory(EntityPlayer playerIn);

   void closeInventory(EntityPlayer playerIn);

   boolean isItemValidForSlot(int index, ItemStack stack);

   int getField(int id);

   void setField(int id, int value);

   int getFieldCount();

   void clearInventory();
}
