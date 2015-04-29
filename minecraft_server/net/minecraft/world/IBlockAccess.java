package net.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IBlockAccess {
   TileEntity getTileEntity(BlockPos pos);

   IBlockState getBlockState(BlockPos pos);

   boolean isAirBlock(BlockPos pos);

   int getStrongPower(BlockPos pos, EnumFacing direction);
}
