package net.minecraft.world.chunk;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;

public interface IChunkProvider {
   boolean chunkExists(int p_73149_1_, int p_73149_2_);

   Chunk provideChunk(int p_73154_1_, int p_73154_2_);

   Chunk func_177459_a(BlockPos p_177459_1_);

   void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_);

   boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_);

   boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_);

   boolean unloadQueuedChunks();

   boolean canSave();

   String makeString();

   List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_);

   BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_);

   int getLoadedChunkCount();

   void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_);

   void saveExtraData();
}
