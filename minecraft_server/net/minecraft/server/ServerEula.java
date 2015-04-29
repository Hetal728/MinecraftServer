package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerEula {
   private static final Logger LOG = LogManager.getLogger();
   private final File eulaFile;
   private final boolean acceptedEULA;
   private static final String __OBFID = "CL_00001911";

   public ServerEula(File eulaFile) {
      this.eulaFile = eulaFile;
      this.acceptedEULA = this.loadEULAFile(eulaFile);
   }

   private boolean loadEULAFile(File inFile) {
      FileInputStream var2 = null;
      boolean var3 = false;

      try {
         Properties var4 = new Properties();
         var2 = new FileInputStream(inFile);
         var4.load(var2);
         var3 = Boolean.parseBoolean(var4.getProperty("eula", "false"));
      } catch (Exception var8) {
         LOG.warn("Failed to load " + inFile);
         this.createEULAFile();
      } finally {
         IOUtils.closeQuietly(var2);
      }

      return var3;
   }

   public boolean hasAcceptedEULA() {
      return this.acceptedEULA;
   }

   public void createEULAFile() {
      FileOutputStream var1 = null;

      try {
         Properties var2 = new Properties();
         var1 = new FileOutputStream(this.eulaFile);
         var2.setProperty("eula", "false");
         var2.store(var1, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
      } catch (Exception var6) {
         LOG.warn("Failed to save " + this.eulaFile, var6);
      } finally {
         IOUtils.closeQuietly(var1);
      }
   }
}
