package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DedicatedPlayerList extends ServerConfigurationManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String __OBFID = "CL_00001783";

   public DedicatedPlayerList(DedicatedServer server) {
      super(server);
      this.setViewDistance(server.getIntProperty("view-distance", 10));
      this.maxPlayers = server.getIntProperty("max-players", 20);
      this.setWhiteListEnabled(server.getBooleanProperty("white-list", false));
      if(!server.isSinglePlayer()) {
         this.getBannedPlayers().setLanServer(true);
         this.getBannedIPs().setLanServer(true);
      }

      this.loadUserBansList();
      this.saveUserBanList();
      this.loadIpBanList();
      this.saveIpBanList();
      this.loadOpsList();
      this.readWhiteList();
      this.saveOpsList();
      if(!this.getWhitelistedPlayers().getSaveFile().exists()) {
         this.saveWhiteList();
      }
   }

   public void setWhiteListEnabled(boolean whitelistEnabled) {
      super.setWhiteListEnabled(whitelistEnabled);
      this.getServerInstance().setProperty("white-list", Boolean.valueOf(whitelistEnabled));
      this.getServerInstance().saveProperties();
   }

   public void addOp(GameProfile profile) {
      super.addOp(profile);
      this.saveOpsList();
   }

   public void removeOp(GameProfile profile) {
      super.removeOp(profile);
      this.saveOpsList();
   }

   public void removePlayerFromWhitelist(GameProfile profile) {
      super.removePlayerFromWhitelist(profile);
      this.saveWhiteList();
   }

   public void addWhitelistedPlayer(GameProfile profile) {
      super.addWhitelistedPlayer(profile);
      this.saveWhiteList();
   }

   public void loadWhiteList() {
      this.readWhiteList();
   }

   private void saveIpBanList() {
      try {
         this.getBannedIPs().writeChanges();
      } catch (IOException var2) {
         LOGGER.warn("Failed to save ip banlist: ", var2);
      }
   }

   private void saveUserBanList() {
      try {
         this.getBannedPlayers().writeChanges();
      } catch (IOException var2) {
         LOGGER.warn("Failed to save user banlist: ", var2);
      }
   }

   private void loadIpBanList() {
      try {
         this.getBannedIPs().readSavedFile();
      } catch (IOException var2) {
         LOGGER.warn("Failed to load ip banlist: ", var2);
      }
   }

   private void loadUserBansList() {
      try {
         this.getBannedPlayers().readSavedFile();
      } catch (IOException var2) {
         LOGGER.warn("Failed to load user banlist: ", var2);
      }
   }

   private void loadOpsList() {
      try {
         this.getOppedPlayers().readSavedFile();
      } catch (Exception var2) {
         LOGGER.warn("Failed to load operators list: ", var2);
      }
   }

   private void saveOpsList() {
      try {
         this.getOppedPlayers().writeChanges();
      } catch (Exception var2) {
         LOGGER.warn("Failed to save operators list: ", var2);
      }
   }

   private void readWhiteList() {
      try {
         this.getWhitelistedPlayers().readSavedFile();
      } catch (Exception var2) {
         LOGGER.warn("Failed to load white-list: ", var2);
      }
   }

   private void saveWhiteList() {
      try {
         this.getWhitelistedPlayers().writeChanges();
      } catch (Exception var2) {
         LOGGER.warn("Failed to save white-list: ", var2);
      }
   }

   public boolean canJoin(GameProfile profile) {
      return !this.isWhiteListEnabled() || this.canSendCommands(profile) || this.getWhitelistedPlayers().func_152705_a(profile);
   }

   public DedicatedServer getServerInstance() {
      return (DedicatedServer)super.getServerInstance();
   }
}
