package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {
   protected int chunkX;
   protected int chunkZ;
   protected Chunk[][] chunkArray;
   protected boolean hasExtendedLevels;
   protected World worldObj;
   private static final String __OBFID = "CL_00000155";

   public ChunkCache(World worldIn, BlockPos p_i45746_2_, BlockPos p_i45746_3_, int p_i45746_4_) {
      this.worldObj = worldIn;
      this.chunkX = p_i45746_2_.getX() - p_i45746_4_ >> 4;
      this.chunkZ = p_i45746_2_.getZ() - p_i45746_4_ >> 4;
      int var5 = p_i45746_3_.getX() + p_i45746_4_ >> 4;
      int var6 = p_i45746_3_.getZ() + p_i45746_4_ >> 4;
      this.chunkArray = new Chunk[var5 - this.chunkX + 1][var6 - this.chunkZ + 1];
      this.hasExtendedLevels = true;

      int var7;
      int var8;
      for(var7 = this.chunkX; var7 <= var5; ++var7) {
         for(var8 = this.chunkZ; var8 <= var6; ++var8) {
            this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ] = worldIn.getChunkFromChunkCoords(var7, var8);
         }
      }

      for(var7 = p_i45746_2_.getX() >> 4; var7 <= p_i45746_3_.getX() >> 4; ++var7) {
         for(var8 = p_i45746_2_.getZ() >> 4; var8 <= p_i45746_3_.getZ() >> 4; ++var8) {
            Chunk var9 = this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ];
            if(var9 != null && !var9.getAreLevelsEmpty(p_i45746_2_.getY(), p_i45746_3_.getY())) {
               this.hasExtendedLevels = false;
            }
         }
      }
   }

   public TileEntity getTileEntity(BlockPos pos) {
      int var2 = (pos.getX() >> 4) - this.chunkX;
      int var3 = (pos.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[var2][var3].func_177424_a(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
   }

   public IBlockState getBlockState(BlockPos pos) {
      if(pos.getY() >= 0 && pos.getY() < 256) {
         int var2 = (pos.getX() >> 4) - this.chunkX;
         int var3 = (pos.getZ() >> 4) - this.chunkZ;
         if(var2 >= 0 && var2 < this.chunkArray.length && var3 >= 0 && var3 < this.chunkArray[var2].length) {
            Chunk var4 = this.chunkArray[var2][var3];
            if(var4 != null) {
               return var4.getBlockState(pos);
            }
         }
      }

      return Blocks.air.getDefaultState();
   }

   public boolean isAirBlock(BlockPos pos) {
      return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
   }

   public int getStrongPower(BlockPos pos, EnumFacing direction) {
      IBlockState var3 = this.getBlockState(pos);
      return var3.getBlock().isProvidingStrongPower(this, pos, var3, direction);
   }
}
