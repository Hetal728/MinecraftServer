package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockEnchantmentTable extends BlockContainer {
   private static final String __OBFID = "CL_00000235";

   protected BlockEnchantmentTable() {
      super(Material.rock);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
      this.setLightOpacity(0);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getRenderType() {
      return 3;
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileEntityEnchantmentTable();
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(worldIn.isRemote) {
         return true;
      } else {
         TileEntity var9 = worldIn.getTileEntity(pos);
         if(var9 instanceof TileEntityEnchantmentTable) {
            playerIn.displayGui((TileEntityEnchantmentTable)var9);
         }

         return true;
      }
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
      if(stack.hasDisplayName()) {
         TileEntity var6 = worldIn.getTileEntity(pos);
         if(var6 instanceof TileEntityEnchantmentTable) {
            ((TileEntityEnchantmentTable)var6).func_145920_a(stack.getDisplayName());
         }
      }
   }
}
