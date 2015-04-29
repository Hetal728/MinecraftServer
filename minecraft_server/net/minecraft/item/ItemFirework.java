package net.minecraft.item;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFirework extends Item {
   private static final String __OBFID = "CL_00000031";

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(!worldIn.isRemote) {
         EntityFireworkRocket var9 = new EntityFireworkRocket(worldIn, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), stack);
         worldIn.spawnEntityInWorld(var9);
         if(!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }
}
