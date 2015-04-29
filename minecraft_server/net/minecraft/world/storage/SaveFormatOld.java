package net.minecraft.world.storage;

import java.io.File;
import java.io.FileInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat {
   private static final Logger logger = LogManager.getLogger();
   protected final File savesDirectory;
   private static final String __OBFID = "CL_00000586";

   public SaveFormatOld(File p_i2147_1_) {
      if(!p_i2147_1_.exists()) {
         p_i2147_1_.mkdirs();
      }

      this.savesDirectory = p_i2147_1_;
   }

   public void flushCache() {}

   public WorldInfo getWorldInfo(String p_75803_1_) {
      File var2 = new File(this.savesDirectory, p_75803_1_);
      if(!var2.exists()) {
         return null;
      } else {
         File var3 = new File(var2, "level.dat");
         NBTTagCompound var4;
         NBTTagCompound var5;
         if(var3.exists()) {
            try {
               var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               var5 = var4.getCompoundTag("Data");
               return new WorldInfo(var5);
            } catch (Exception var7) {
               logger.error("Exception reading " + var3, var7);
            }
         }

         var3 = new File(var2, "level.dat_old");
         if(var3.exists()) {
            try {
               var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               var5 = var4.getCompoundTag("Data");
               return new WorldInfo(var5);
            } catch (Exception var6) {
               logger.error("Exception reading " + var3, var6);
            }
         }

         return null;
      }
   }

   public boolean deleteWorldDirectory(String p_75802_1_) {
      File var2 = new File(this.savesDirectory, p_75802_1_);
      if(!var2.exists()) {
         return true;
      } else {
         logger.info("Deleting level " + p_75802_1_);

         for(int var3 = 1; var3 <= 5; ++var3) {
            logger.info("Attempt " + var3 + "...");
            if(deleteFiles(var2.listFiles())) {
               break;
            }

            logger.warn("Unsuccessful in deleting contents.");
            if(var3 < 5) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var5) {
                  ;
               }
            }
         }

         return var2.delete();
      }
   }

   protected static boolean deleteFiles(File[] p_75807_0_) {
      for(int var1 = 0; var1 < p_75807_0_.length; ++var1) {
         File var2 = p_75807_0_[var1];
         logger.debug("Deleting " + var2);
         if(var2.isDirectory() && !deleteFiles(var2.listFiles())) {
            logger.warn("Couldn\'t delete directory " + var2);
            return false;
         }

         if(!var2.delete()) {
            logger.warn("Couldn\'t delete file " + var2);
            return false;
         }
      }

      return true;
   }

   public ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_) {
      return new SaveHandler(this.savesDirectory, p_75804_1_, p_75804_2_);
   }

   public boolean isOldMapFormat(String p_75801_1_) {
      return false;
   }

   public boolean convertMapFormat(String p_75805_1_, IProgressUpdate p_75805_2_) {
      return false;
   }
}
