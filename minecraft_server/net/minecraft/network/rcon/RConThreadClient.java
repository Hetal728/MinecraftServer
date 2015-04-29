package net.minecraft.network.rcon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RConThreadClient extends RConThreadBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private boolean loggedIn;
   private Socket clientSocket;
   private byte[] buffer = new byte[1460];
   private String rconPassword;
   private static final String __OBFID = "CL_00001804";

   RConThreadClient(IServer p_i1537_1_, Socket socket) {
      super(p_i1537_1_, "RCON Client");
      this.clientSocket = socket;

      try {
         this.clientSocket.setSoTimeout(0);
      } catch (Exception var4) {
         this.running = false;
      }

      this.rconPassword = p_i1537_1_.getStringProperty("rcon.password", "");
      this.logInfo("Rcon connection from: " + socket.getInetAddress());
   }

   public void run() {
      while(true) {
         try {
            if(!this.running) {
               break;
            }

            BufferedInputStream var1 = new BufferedInputStream(this.clientSocket.getInputStream());
            int var2 = var1.read(this.buffer, 0, 1460);
            if(10 > var2) {
               return;
            }

            byte var3 = 0;
            int var4 = RConUtils.getBytesAsLEInt(this.buffer, 0, var2);
            if(var4 == var2 - 4) {
               int var21 = var3 + 4;
               int var5 = RConUtils.getBytesAsLEInt(this.buffer, var21, var2);
               var21 += 4;
               int var6 = RConUtils.getRemainingBytesAsLEInt(this.buffer, var21);
               var21 += 4;
               switch(var6) {
               case 2:
                  if(this.loggedIn) {
                     String var8 = RConUtils.getBytesAsString(this.buffer, var21, var2);

                     try {
                        this.sendMultipacketResponse(var5, this.server.handleRConCommand(var8));
                     } catch (Exception var16) {
                        this.sendMultipacketResponse(var5, "Error executing: " + var8 + " (" + var16.getMessage() + ")");
                     }
                     continue;
                  }

                  this.sendLoginFailedResponse();
                  continue;
               case 3:
                  String var7 = RConUtils.getBytesAsString(this.buffer, var21, var2);
                  int var10000 = var21 + var7.length();
                  if(0 != var7.length() && var7.equals(this.rconPassword)) {
                     this.loggedIn = true;
                     this.sendResponse(var5, 2, "");
                     continue;
                  }

                  this.loggedIn = false;
                  this.sendLoginFailedResponse();
                  continue;
               default:
                  this.sendMultipacketResponse(var5, String.format("Unknown request %s", new Object[]{Integer.toHexString(var6)}));
                  continue;
               }
            }
         } catch (SocketTimeoutException var17) {
            break;
         } catch (IOException var18) {
            break;
         } catch (Exception var19) {
            LOGGER.error("Exception whilst parsing RCON input", var19);
            break;
         } finally {
            this.closeSocket();
         }

         return;
      }
   }

   private void sendResponse(int p_72654_1_, int p_72654_2_, String message) throws IOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream(1248);
      DataOutputStream var5 = new DataOutputStream(var4);
      byte[] var6 = message.getBytes("UTF-8");
      var5.writeInt(Integer.reverseBytes(var6.length + 10));
      var5.writeInt(Integer.reverseBytes(p_72654_1_));
      var5.writeInt(Integer.reverseBytes(p_72654_2_));
      var5.write(var6);
      var5.write(0);
      var5.write(0);
      this.clientSocket.getOutputStream().write(var4.toByteArray());
   }

   private void sendLoginFailedResponse() throws IOException {
      this.sendResponse(-1, 2, "");
   }

   private void sendMultipacketResponse(int p_72655_1_, String p_72655_2_) throws IOException {
      int var3 = p_72655_2_.length();

      do {
         int var4 = 4096 <= var3?4096:var3;
         this.sendResponse(p_72655_1_, 0, p_72655_2_.substring(0, var4));
         p_72655_2_ = p_72655_2_.substring(var4);
         var3 = p_72655_2_.length();
      } while(0 != var3);
   }

   private void closeSocket() {
      if(null != this.clientSocket) {
         try {
            this.clientSocket.close();
         } catch (IOException var2) {
            this.logWarning("IO: " + var2.getMessage());
         }

         this.clientSocket = null;
      }
   }
}
