package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;

public abstract class WorldProvider {
   public static final float[] moonPhaseFactors = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
   protected World worldObj;
   private WorldType terrainType;
   private String generatorSettings;
   protected WorldChunkManager worldChunkMgr;
   protected boolean isHellWorld;
   protected boolean hasNoSky;
   protected final float[] lightBrightnessTable = new float[16];
   protected int dimensionId;
   private final float[] colorsSunriseSunset = new float[4];
   private static final String __OBFID = "CL_00000386";

   public final void registerWorld(World worldIn) {
      this.worldObj = worldIn;
      this.terrainType = worldIn.getWorldInfo().getTerrainType();
      this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
      this.registerWorldChunkManager();
      this.generateLightBrightnessTable();
   }

   protected void generateLightBrightnessTable() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 <= 15; ++var2) {
         float var3 = 1.0F - (float)var2 / 15.0F;
         this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
      }
   }

   protected void registerWorldChunkManager() {
      WorldType var1 = this.worldObj.getWorldInfo().getTerrainType();
      if(var1 == WorldType.FLAT) {
         FlatGeneratorInfo var2 = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
         this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(var2.getBiome(), BiomeGenBase.field_180279_ad), 0.5F);
      } else if(var1 == WorldType.DEBUG_WORLD) {
         this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.0F);
      } else {
         this.worldChunkMgr = new WorldChunkManager(this.worldObj);
      }
   }

   public IChunkProvider createChunkGenerator() {
      return (IChunkProvider)(this.terrainType == WorldType.FLAT?new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings):(this.terrainType == WorldType.DEBUG_WORLD?new ChunkProviderDebug(this.worldObj):(this.terrainType == WorldType.CUSTOMIZED?new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings):new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings))));
   }

   public boolean canCoordinateBeSpawn(int x, int z) {
      return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass;
   }

   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
      int var4 = (int)(p_76563_1_ % 24000L);
      float var5 = ((float)var4 + p_76563_3_) / 24000.0F - 0.25F;
      if(var5 < 0.0F) {
         ++var5;
      }

      if(var5 > 1.0F) {
         --var5;
      }

      float var6 = var5;
      var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
      var5 = var6 + (var5 - var6) / 3.0F;
      return var5;
   }

   public int getMoonPhase(long p_76559_1_) {
      return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
   }

   public boolean isSurfaceWorld() {
      return true;
   }

   public boolean canRespawnHere() {
      return true;
   }

   public static WorldProvider getProviderForDimension(int dimension) {
      return (WorldProvider)(dimension == -1?new WorldProviderHell():(dimension == 0?new WorldProviderSurface():(dimension == 1?new WorldProviderEnd():null)));
   }

   public BlockPos func_177496_h() {
      return null;
   }

   public int getAverageGroundLevel() {
      return this.terrainType == WorldType.FLAT?4:64;
   }

   public abstract String getDimensionName();

   public abstract String getInternalNameSuffix();

   public WorldChunkManager getWorldChunkManager() {
      return this.worldChunkMgr;
   }

   public boolean func_177500_n() {
      return this.isHellWorld;
   }

   public boolean getHasNoSky() {
      return this.hasNoSky;
   }

   public float[] getLightBrightnessTable() {
      return this.lightBrightnessTable;
   }

   public int getDimensionId() {
      return this.dimensionId;
   }

   public WorldBorder getWorldBorder() {
      return new WorldBorder();
   }
}
