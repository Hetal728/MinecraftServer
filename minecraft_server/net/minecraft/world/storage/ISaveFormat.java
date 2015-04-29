package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
   ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_);

   void flushCache();

   boolean deleteWorldDirectory(String p_75802_1_);

   boolean isOldMapFormat(String p_75801_1_);

   boolean convertMapFormat(String p_75805_1_, IProgressUpdate p_75805_2_);
}
