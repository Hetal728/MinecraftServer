package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlock extends Item {
   protected final Block block;
   private static final String __OBFID = "CL_00001772";

   public ItemBlock(Block block) {
      this.block = block;
   }

   public ItemBlock setUnlocalizedName(String unlocalizedName) {
      super.setUnlocalizedName(unlocalizedName);
      return this;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      IBlockState var9 = worldIn.getBlockState(pos);
      Block var10 = var9.getBlock();
      if(var10 == Blocks.snow_layer && ((Integer)var9.getValue(BlockSnow.LAYERS_PROP)).intValue() < 1) {
         side = EnumFacing.UP;
      } else if(!var10.isReplaceable(worldIn, pos)) {
         pos = pos.offset(side);
      }

      if(stack.stackSize == 0) {
         return false;
      } else if(!playerIn.func_175151_a(pos, side, stack)) {
         return false;
      } else if(pos.getY() == 255 && this.block.getMaterial().isSolid()) {
         return false;
      } else if(worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack)) {
         int var11 = this.getMetadata(stack.getMetadata());
         IBlockState var12 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, var11, playerIn);
         if(worldIn.setBlockState(pos, var12, 3)) {
            var12 = worldIn.getBlockState(pos);
            if(var12.getBlock() == this.block) {
               setTileEntityNBT(worldIn, pos, stack);
               this.block.onBlockPlacedBy(worldIn, pos, var12, playerIn, stack);
            }

            worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
            --stack.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean setTileEntityNBT(World worldIn, BlockPos p_179224_1_, ItemStack p_179224_2_) {
      if(p_179224_2_.hasTagCompound() && p_179224_2_.getTagCompound().hasKey("BlockEntityTag", 10)) {
         TileEntity var3 = worldIn.getTileEntity(p_179224_1_);
         if(var3 != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            NBTTagCompound var5 = (NBTTagCompound)var4.copy();
            var3.writeToNBT(var4);
            NBTTagCompound var6 = (NBTTagCompound)p_179224_2_.getTagCompound().getTag("BlockEntityTag");
            var4.merge(var6);
            var4.setInteger("x", p_179224_1_.getX());
            var4.setInteger("y", p_179224_1_.getY());
            var4.setInteger("z", p_179224_1_.getZ());
            if(!var4.equals(var5)) {
               var3.readFromNBT(var4);
               var3.markDirty();
               return true;
            }
         }
      }

      return false;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return this.block.getUnlocalizedName();
   }

   public String getUnlocalizedName() {
      return this.block.getUnlocalizedName();
   }

   public Block getBlock() {
      return this.block;
   }
}
