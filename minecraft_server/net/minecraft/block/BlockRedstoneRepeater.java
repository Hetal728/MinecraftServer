package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {
   public static final PropertyBool field_176411_a = PropertyBool.create("locked");
   public static final PropertyInteger field_176410_b = PropertyInteger.create("delay", 1, 4);
   private static final String __OBFID = "CL_00000301";

   protected BlockRedstoneRepeater(boolean p_i45424_1_) {
      super(p_i45424_1_);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, EnumFacing.NORTH).withProperty(field_176410_b, Integer.valueOf(1)).withProperty(field_176411_a, Boolean.valueOf(false)));
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.withProperty(field_176411_a, Boolean.valueOf(this.func_176405_b(worldIn, pos, state)));
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(!playerIn.capabilities.allowEdit) {
         return false;
      } else {
         worldIn.setBlockState(pos, state.cycleProperty(field_176410_b), 3);
         return true;
      }
   }

   protected int func_176403_d(IBlockState p_176403_1_) {
      return ((Integer)p_176403_1_.getValue(field_176410_b)).intValue() * 2;
   }

   protected IBlockState func_180674_e(IBlockState p_180674_1_) {
      Integer var2 = (Integer)p_180674_1_.getValue(field_176410_b);
      Boolean var3 = (Boolean)p_180674_1_.getValue(field_176411_a);
      EnumFacing var4 = (EnumFacing)p_180674_1_.getValue(AGE);
      return Blocks.powered_repeater.getDefaultState().withProperty(AGE, var4).withProperty(field_176410_b, var2).withProperty(field_176411_a, var3);
   }

   protected IBlockState func_180675_k(IBlockState p_180675_1_) {
      Integer var2 = (Integer)p_180675_1_.getValue(field_176410_b);
      Boolean var3 = (Boolean)p_180675_1_.getValue(field_176411_a);
      EnumFacing var4 = (EnumFacing)p_180675_1_.getValue(AGE);
      return Blocks.unpowered_repeater.getDefaultState().withProperty(AGE, var4).withProperty(field_176410_b, var2).withProperty(field_176411_a, var3);
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.repeater;
   }

   public boolean func_176405_b(IBlockAccess p_176405_1_, BlockPos p_176405_2_, IBlockState p_176405_3_) {
      return this.func_176407_c(p_176405_1_, p_176405_2_, p_176405_3_) > 0;
   }

   protected boolean func_149908_a(Block p_149908_1_) {
      return isRedstoneRepeaterBlockID(p_149908_1_);
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      super.breakBlock(worldIn, pos, state);
      this.func_176400_h(worldIn, pos, state);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(AGE, EnumFacing.getHorizontal(meta)).withProperty(field_176411_a, Boolean.valueOf(false)).withProperty(field_176410_b, Integer.valueOf(1 + (meta >> 2)));
   }

   public int getMetaFromState(IBlockState state) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)state.getValue(AGE)).getHorizontalIndex();
      var3 |= ((Integer)state.getValue(field_176410_b)).intValue() - 1 << 2;
      return var3;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{AGE, field_176410_b, field_176411_a});
   }
}
