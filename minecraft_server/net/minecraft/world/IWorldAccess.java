package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public interface IWorldAccess {
   void markBlockForUpdate(BlockPos pos);

   void notifyLightSet(BlockPos pos);

   void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2);

   void playSound(String soundName, double x, double y, double z, float volume, float pitch);

   void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch);

   void func_180442_a(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int ... var15);

   void onEntityAdded(Entity entityIn);

   void onEntityRemoved(Entity entityIn);

   void func_174961_a(String p_174961_1_, BlockPos p_174961_2_);

   void func_180440_a(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_);

   void func_180439_a(EntityPlayer p_180439_1_, int p_180439_2_, BlockPos p_180439_3_, int p_180439_4_);

   void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress);
}
