package net.minecraft.world;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

public class WorldProviderEnd extends WorldProvider {
   private static final String __OBFID = "CL_00000389";

   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
      this.dimensionId = 1;
      this.hasNoSky = true;
   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
   }

   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
      return 0.0F;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public boolean canCoordinateBeSpawn(int x, int z) {
      return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
   }

   public BlockPos func_177496_h() {
      return new BlockPos(100, 50, 0);
   }

   public int getAverageGroundLevel() {
      return 50;
   }

   public String getDimensionName() {
      return "The End";
   }

   public String getInternalNameSuffix() {
      return "_end";
   }
}
