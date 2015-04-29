package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
   public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
   public static final File OLD_OPS_FILE = new File("ops.txt");
   public static final File OLD_WHITELIST_FILE = new File("white-list.txt");
   private static final String __OBFID = "CL_00001882";

   static List readFile(File inFile, Map read) throws IOException {
      List var2 = Files.readLines(inFile, Charsets.UTF_8);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var4 = var4.trim();
         if(!var4.startsWith("#") && var4.length() >= 1) {
            String[] var5 = var4.split("\\|");
            read.put(var5[0].toLowerCase(Locale.ROOT), var5);
         }
      }

      return var2;
   }

   private static void lookupNames(MinecraftServer server, Collection names, ProfileLookupCallback callback) {
      String[] var3 = (String[])Iterators.toArray(Iterators.filter(names.iterator(), new Predicate() {
         private static final String __OBFID = "CL_00001881";

         public boolean func_152733_a(String p_152733_1_) {
            return !StringUtils.isNullOrEmpty(p_152733_1_);
         }

         public boolean apply(Object p_apply_1_) {
            return this.func_152733_a((String)p_apply_1_);
         }
      }), String.class);
      if(server.isServerInOnlineMode()) {
         server.getGameProfileRepository().findProfilesByNames(var3, Agent.MINECRAFT, callback);
      } else {
         String[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            UUID var8 = EntityPlayer.getUUID(new GameProfile((UUID)null, var7));
            GameProfile var9 = new GameProfile(var8, var7);
            callback.onProfileLookupSucceeded(var9);
         }
      }
   }

   public static boolean convertUserBanlist(final MinecraftServer server) throws IOException {
      final UserListBans var1 = new UserListBans(ServerConfigurationManager.FILE_PLAYERBANS);
      if(OLD_PLAYERBAN_FILE.exists() && OLD_PLAYERBAN_FILE.isFile()) {
         if(var1.getSaveFile().exists()) {
            try {
               var1.readSavedFile();
            } catch (FileNotFoundException var6) {
               LOGGER.warn("Could not load existing file " + var1.getSaveFile().getName(), var6);
            }
         }

         try {
            final HashMap var2 = Maps.newHashMap();
            readFile(OLD_PLAYERBAN_FILE, var2);
            ProfileLookupCallback var3 = new ProfileLookupCallback() {
               private static final String __OBFID = "CL_00001910";

               public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                  server.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                  String[] var2x = (String[])var2.get(p_onProfileLookupSucceeded_1_.getName().toLowerCase(Locale.ROOT));
                  if(var2x == null) {
                     PreYggdrasilConverter.LOGGER.warn("Could not convert user banlist entry for " + p_onProfileLookupSucceeded_1_.getName());
                     throw new PreYggdrasilConverter.ConversionError("Profile not in the conversionlist", null);
                  } else {
                     Date var3 = var2x.length > 1?PreYggdrasilConverter.parseDate(var2x[1], (Date)null):null;
                     String var4 = var2x.length > 2?var2x[2]:null;
                     Date var5 = var2x.length > 3?PreYggdrasilConverter.parseDate(var2x[3], (Date)null):null;
                     String var6 = var2x.length > 4?var2x[4]:null;
                     var1.addEntry(new UserListBansEntry(p_onProfileLookupSucceeded_1_, var3, var4, var5, var6));
                  }
               }
               public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup user banlist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
                  if(!(p_onProfileLookupFailed_2_ instanceof ProfileNotFoundException)) {
                     throw new PreYggdrasilConverter.ConversionError("Could not request user " + p_onProfileLookupFailed_1_.getName() + " from backend systems", p_onProfileLookupFailed_2_, null);
                  }
               }
            };
            lookupNames(server, var2.keySet(), var3);
            var1.writeChanges();
            backupConverted(OLD_PLAYERBAN_FILE);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old user banlist to convert it!", var4);
            return false;
         } catch (PreYggdrasilConverter.ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertIpBanlist(MinecraftServer server) throws IOException {
      BanList var1 = new BanList(ServerConfigurationManager.FILE_IPBANS);
      if(OLD_IPBAN_FILE.exists() && OLD_IPBAN_FILE.isFile()) {
         if(var1.getSaveFile().exists()) {
            try {
               var1.readSavedFile();
            } catch (FileNotFoundException var11) {
               LOGGER.warn("Could not load existing file " + var1.getSaveFile().getName(), var11);
            }
         }

         try {
            HashMap var2 = Maps.newHashMap();
            readFile(OLD_IPBAN_FILE, var2);
            Iterator var3 = var2.keySet().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               String[] var5 = (String[])var2.get(var4);
               Date var6 = var5.length > 1?parseDate(var5[1], (Date)null):null;
               String var7 = var5.length > 2?var5[2]:null;
               Date var8 = var5.length > 3?parseDate(var5[3], (Date)null):null;
               String var9 = var5.length > 4?var5[4]:null;
               var1.addEntry(new IPBanEntry(var4, var6, var7, var8, var9));
            }

            var1.writeChanges();
            backupConverted(OLD_IPBAN_FILE);
            return true;
         } catch (IOException var10) {
            LOGGER.warn("Could not parse old ip banlist to convert it!", var10);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertOplist(final MinecraftServer server) throws IOException {
      final UserListOps var1 = new UserListOps(ServerConfigurationManager.FILE_OPS);
      if(OLD_OPS_FILE.exists() && OLD_OPS_FILE.isFile()) {
         if(var1.getSaveFile().exists()) {
            try {
               var1.readSavedFile();
            } catch (FileNotFoundException var6) {
               LOGGER.warn("Could not load existing file " + var1.getSaveFile().getName(), var6);
            }
         }

         try {
            List var2 = Files.readLines(OLD_OPS_FILE, Charsets.UTF_8);
            ProfileLookupCallback var3 = new ProfileLookupCallback() {
               private static final String __OBFID = "CL_00001909";

               public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                  server.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                  var1.addEntry(new UserListOpsEntry(p_onProfileLookupSucceeded_1_, server.getOpPermissionLevel()));
               }
               public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup oplist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
                  if(!(p_onProfileLookupFailed_2_ instanceof ProfileNotFoundException)) {
                     throw new PreYggdrasilConverter.ConversionError("Could not request user " + p_onProfileLookupFailed_1_.getName() + " from backend systems", p_onProfileLookupFailed_2_, null);
                  }
               }
            };
            lookupNames(server, var2, var3);
            var1.writeChanges();
            backupConverted(OLD_OPS_FILE);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old oplist to convert it!", var4);
            return false;
         } catch (PreYggdrasilConverter.ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean convertWhitelist(final MinecraftServer server) throws IOException {
      final UserListWhitelist var1 = new UserListWhitelist(ServerConfigurationManager.FILE_WHITELIST);
      if(OLD_WHITELIST_FILE.exists() && OLD_WHITELIST_FILE.isFile()) {
         if(var1.getSaveFile().exists()) {
            try {
               var1.readSavedFile();
            } catch (FileNotFoundException var6) {
               LOGGER.warn("Could not load existing file " + var1.getSaveFile().getName(), var6);
            }
         }

         try {
            List var2 = Files.readLines(OLD_WHITELIST_FILE, Charsets.UTF_8);
            ProfileLookupCallback var3 = new ProfileLookupCallback() {
               private static final String __OBFID = "CL_00001908";

               public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                  server.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                  var1.addEntry(new UserListWhitelistEntry(p_onProfileLookupSucceeded_1_));
               }
               public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
                  if(!(p_onProfileLookupFailed_2_ instanceof ProfileNotFoundException)) {
                     throw new PreYggdrasilConverter.ConversionError("Could not request user " + p_onProfileLookupFailed_1_.getName() + " from backend systems", p_onProfileLookupFailed_2_, null);
                  }
               }
            };
            lookupNames(server, var2, var3);
            var1.writeChanges();
            backupConverted(OLD_WHITELIST_FILE);
            return true;
         } catch (IOException var4) {
            LOGGER.warn("Could not read old whitelist to convert it!", var4);
            return false;
         } catch (PreYggdrasilConverter.ConversionError var5) {
            LOGGER.error("Conversion failed, please try again later", var5);
            return false;
         }
      } else {
         return true;
      }
   }

   public static String func_152719_a(String p_152719_0_) {
      if(!StringUtils.isNullOrEmpty(p_152719_0_) && p_152719_0_.length() <= 16) {
         final MinecraftServer var1 = MinecraftServer.getServer();
         GameProfile var2 = var1.getPlayerProfileCache().getGameProfileForUsername(p_152719_0_);
         if(var2 != null && var2.getId() != null) {
            return var2.getId().toString();
         } else if(!var1.isSinglePlayer() && var1.isServerInOnlineMode()) {
            final ArrayList var3 = Lists.newArrayList();
            ProfileLookupCallback var4 = new ProfileLookupCallback() {
               private static final String __OBFID = "CL_00001880";

               public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                  var1.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                  var3.add(p_onProfileLookupSucceeded_1_);
               }
               public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
               }
            };
            lookupNames(var1, Lists.newArrayList(new String[]{p_152719_0_}), var4);
            return var3.size() > 0 && ((GameProfile)var3.get(0)).getId() != null?((GameProfile)var3.get(0)).getId().toString():"";
         } else {
            return EntityPlayer.getUUID(new GameProfile((UUID)null, p_152719_0_)).toString();
         }
      } else {
         return p_152719_0_;
      }
   }

   public static boolean convertSaveFiles(final DedicatedServer server, PropertyManager p_152723_1_) {
      final File var2 = getPlayersDirectory(p_152723_1_);
      final File var3 = new File(var2.getParentFile(), "playerdata");
      final File var4 = new File(var2.getParentFile(), "unknownplayers");
      if(var2.exists() && var2.isDirectory()) {
         File[] var5 = var2.listFiles();
         ArrayList var6 = Lists.newArrayList();
         File[] var7 = var5;
         int var8 = var5.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            File var10 = var7[var9];
            String var11 = var10.getName();
            if(var11.toLowerCase(Locale.ROOT).endsWith(".dat")) {
               String var12 = var11.substring(0, var11.length() - ".dat".length());
               if(var12.length() > 0) {
                  var6.add(var12);
               }
            }
         }

         try {
            final String[] var14 = (String[])var6.toArray(new String[var6.size()]);
            ProfileLookupCallback var15 = new ProfileLookupCallback() {
               private static final String __OBFID = "CL_00001907";

               public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                  server.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                  UUID var2x = p_onProfileLookupSucceeded_1_.getId();
                  if(var2x == null) {
                     throw new PreYggdrasilConverter.ConversionError("Missing UUID for user profile " + p_onProfileLookupSucceeded_1_.getName(), null);
                  } else {
                     this.func_152743_a(var3, this.func_152744_a(p_onProfileLookupSucceeded_1_), var2x.toString());
                  }
               }
               public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                  PreYggdrasilConverter.LOGGER.warn("Could not lookup user uuid for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
                  if(p_onProfileLookupFailed_2_ instanceof ProfileNotFoundException) {
                     String var3x = this.func_152744_a(p_onProfileLookupFailed_1_);
                     this.func_152743_a(var4, var3x, var3x);
                  } else {
                     throw new PreYggdrasilConverter.ConversionError("Could not request user " + p_onProfileLookupFailed_1_.getName() + " from backend systems", p_onProfileLookupFailed_2_, null);
                  }
               }
               private void func_152743_a(File p_152743_1_, String p_152743_2_, String p_152743_3_) {
                  File var4x = new File(var2, p_152743_2_ + ".dat");
                  File var5 = new File(p_152743_1_, p_152743_3_ + ".dat");
                  PreYggdrasilConverter.mkdir(p_152743_1_);
                  if(!var4x.renameTo(var5)) {
                     throw new PreYggdrasilConverter.ConversionError("Could not convert file for " + p_152743_2_, null);
                  }
               }
               private String func_152744_a(GameProfile p_152744_1_) {
                  String var2x = null;

                  for(int var3x = 0; var3x < var14.length; ++var3x) {
                     if(var14[var3x] != null && var14[var3x].equalsIgnoreCase(p_152744_1_.getName())) {
                        var2x = var14[var3x];
                        break;
                     }
                  }

                  if(var2x == null) {
                     throw new PreYggdrasilConverter.ConversionError("Could not find the filename for " + p_152744_1_.getName() + " anymore", null);
                  } else {
                     return var2x;
                  }
               }
            };
            lookupNames(server, Lists.newArrayList(var14), var15);
            return true;
         } catch (PreYggdrasilConverter.ConversionError var13) {
            LOGGER.error("Conversion failed, please try again later", var13);
            return false;
         }
      } else {
         return true;
      }
   }

   private static void mkdir(File dir) {
      if(dir.exists()) {
         if(!dir.isDirectory()) {
            throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + dir.getName() + " in world save directory.", null);
         }
      } else if(!dir.mkdirs()) {
         throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + dir.getName() + " in world save directory.", null);
      }
   }

   public static boolean tryConvert(PropertyManager properties) {
      boolean var1 = hasUnconvertableFiles(properties);
      var1 = var1 && hasUnconvertablePlayerFiles(properties);
      return var1;
   }

   private static boolean hasUnconvertableFiles(PropertyManager properties) {
      boolean var1 = false;
      if(OLD_PLAYERBAN_FILE.exists() && OLD_PLAYERBAN_FILE.isFile()) {
         var1 = true;
      }

      boolean var2 = false;
      if(OLD_IPBAN_FILE.exists() && OLD_IPBAN_FILE.isFile()) {
         var2 = true;
      }

      boolean var3 = false;
      if(OLD_OPS_FILE.exists() && OLD_OPS_FILE.isFile()) {
         var3 = true;
      }

      boolean var4 = false;
      if(OLD_WHITELIST_FILE.exists() && OLD_WHITELIST_FILE.isFile()) {
         var4 = true;
      }

      if(!var1 && !var2 && !var3 && !var4) {
         return true;
      } else {
         LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
         LOGGER.warn("** please remove the following files and restart the server:");
         if(var1) {
            LOGGER.warn("* " + OLD_PLAYERBAN_FILE.getName());
         }

         if(var2) {
            LOGGER.warn("* " + OLD_IPBAN_FILE.getName());
         }

         if(var3) {
            LOGGER.warn("* " + OLD_OPS_FILE.getName());
         }

         if(var4) {
            LOGGER.warn("* " + OLD_WHITELIST_FILE.getName());
         }

         return false;
      }
   }

   private static boolean hasUnconvertablePlayerFiles(PropertyManager properties) {
      File var1 = getPlayersDirectory(properties);
      if(var1.exists() && var1.isDirectory() && (var1.list().length > 0 || !var1.delete())) {
         LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
         LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
         LOGGER.warn("** please restart the server and if the problem persists, remove the directory \'{}\'", new Object[]{var1.getPath()});
         return false;
      } else {
         return true;
      }
   }

   private static File getPlayersDirectory(PropertyManager properties) {
      String var1 = properties.getStringProperty("level-name", "world");
      File var2 = new File(var1);
      return new File(var2, "players");
   }

   private static void backupConverted(File convertedFile) {
      File var1 = new File(convertedFile.getName() + ".converted");
      convertedFile.renameTo(var1);
   }

   private static Date parseDate(String input, Date defaultValue) {
      Date var2;
      try {
         var2 = BanEntry.dateFormat.parse(input);
      } catch (ParseException var4) {
         var2 = defaultValue;
      }

      return var2;
   }

   static class ConversionError extends RuntimeException {
      private static final String __OBFID = "CL_00001905";

      private ConversionError(String message, Throwable cause) {
         super(message, cause);
      }

      private ConversionError(String message) {
         super(message);
      }

      ConversionError(String p_i1208_1_, Object p_i1208_2_) {
         this(p_i1208_1_);
      }

      ConversionError(String p_i46367_1_, Throwable p_i46367_2_, Object p_i46367_3_) {
         this(p_i46367_1_, p_i46367_2_);
      }
   }
}
