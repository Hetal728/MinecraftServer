package net.minecraft.network.rcon;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RConThreadMain extends RConThreadBase {
   private int rconPort;
   private int serverPort;
   private String hostname;
   private ServerSocket serverSocket;
   private String rconPassword;
   private Map clientThreads;
   private static final String __OBFID = "CL_00001805";

   public RConThreadMain(IServer p_i1538_1_) {
      super(p_i1538_1_, "RCON Listener");
      this.rconPort = p_i1538_1_.getIntProperty("rcon.port", 0);
      this.rconPassword = p_i1538_1_.getStringProperty("rcon.password", "");
      this.hostname = p_i1538_1_.getHostname();
      this.serverPort = p_i1538_1_.getPort();
      if(0 == this.rconPort) {
         this.rconPort = this.serverPort + 10;
         this.logInfo("Setting default rcon port to " + this.rconPort);
         p_i1538_1_.setProperty("rcon.port", Integer.valueOf(this.rconPort));
         if(0 == this.rconPassword.length()) {
            p_i1538_1_.setProperty("rcon.password", "");
         }

         p_i1538_1_.saveProperties();
      }

      if(0 == this.hostname.length()) {
         this.hostname = "0.0.0.0";
      }

      this.initClientThreadList();
      this.serverSocket = null;
   }

   private void initClientThreadList() {
      this.clientThreads = Maps.newHashMap();
   }

   private void cleanClientThreadsMap() {
      Iterator var1 = this.clientThreads.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         if(!((RConThreadClient)var2.getValue()).isRunning()) {
            var1.remove();
         }
      }
   }

   public void run() {
      this.logInfo("RCON running on " + this.hostname + ":" + this.rconPort);

      try {
         while(this.running) {
            try {
               Socket var1 = this.serverSocket.accept();
               var1.setSoTimeout(500);
               RConThreadClient var2 = new RConThreadClient(this.server, var1);
               var2.startThread();
               this.clientThreads.put(var1.getRemoteSocketAddress(), var2);
               this.cleanClientThreadsMap();
            } catch (SocketTimeoutException var7) {
               this.cleanClientThreadsMap();
            } catch (IOException var8) {
               if(this.running) {
                  this.logInfo("IO: " + var8.getMessage());
               }
            }
         }
      } finally {
         this.closeServerSocket(this.serverSocket);
      }
   }

   public void startThread() {
      if(0 == this.rconPassword.length()) {
         this.logWarning("No rcon password set in \'" + this.server.getSettingsFilename() + "\', rcon disabled!");
      } else if(0 < this.rconPort && 65535 >= this.rconPort) {
         if(!this.running) {
            try {
               this.serverSocket = new ServerSocket(this.rconPort, 0, InetAddress.getByName(this.hostname));
               this.serverSocket.setSoTimeout(500);
               super.startThread();
            } catch (IOException var2) {
               this.logWarning("Unable to initialise rcon on " + this.hostname + ":" + this.rconPort + " : " + var2.getMessage());
            }
         }
      } else {
         this.logWarning("Invalid rcon port " + this.rconPort + " found in \'" + this.server.getSettingsFilename() + "\', rcon disabled!");
      }
   }
}
