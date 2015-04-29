package net.minecraft.server.dedicated;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHangWatchdog implements Runnable {
   private static final Logger LOGGER = LogManager.getLogger();
   private final DedicatedServer server;
   private final long maxTickTime;
   private static final String __OBFID = "CL_00002634";

   public ServerHangWatchdog(DedicatedServer server) {
      this.server = server;
      this.maxTickTime = server.getMaxTickTime();
   }

   public void run() {
      while(this.server.isServerRunning()) {
         long var1 = this.server.getCurrentTime();
         long var3 = MinecraftServer.getCurrentTimeMillis();
         long var5 = var3 - var1;
         if(var5 > this.maxTickTime) {
            LOGGER.fatal("A single server tick took " + String.format("%.2f", new Object[]{Float.valueOf((float)var5 / 1000.0F)}) + " seconds (should be max " + String.format("%.2f", new Object[]{Float.valueOf(0.05F)}) + ")");
            LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
            ThreadMXBean var7 = ManagementFactory.getThreadMXBean();
            ThreadInfo[] var8 = var7.dumpAllThreads(true, true);
            StringBuilder var9 = new StringBuilder();
            Error var10 = new Error();
            ThreadInfo[] var11 = var8;
            int var12 = var8.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               ThreadInfo var14 = var11[var13];
               if(var14.getThreadId() == this.server.getServerThread().getId()) {
                  var10.setStackTrace(var14.getStackTrace());
               }

               var9.append(var14);
               var9.append("\n");
            }

            CrashReport var16 = new CrashReport("Watching Server", var10);
            this.server.addServerInfoToCrashReport(var16);
            CrashReportCategory var17 = var16.makeCategory("Thread Dump");
            var17.addCrashSection("Threads", var9);
            File var18 = new File(new File(this.server.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
            if(var16.saveToFile(var18)) {
               LOGGER.error("This crash report has been saved to: " + var18.getAbsolutePath());
            } else {
               LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.func_180248_a();
         }

         try {
            Thread.sleep(var1 + this.maxTickTime - var3);
         } catch (InterruptedException var15) {
            ;
         }
      }
   }

   private void func_180248_a() {
      try {
         Timer var1 = new Timer();
         var1.schedule(new TimerTask() {
            private static final String __OBFID = "CL_00002633";

            public void run() {
               Runtime.getRuntime().halt(1);
            }
         }, 10000L);
         System.exit(1);
      } catch (Throwable var2) {
         Runtime.getRuntime().halt(1);
      }
   }
}
