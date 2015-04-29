package net.minecraft.server.gui;

import com.mojang.util.QueueLogAppender;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftServerGui extends JComponent {
   private static final Font serverGuiFont = new Font("Monospaced", 0, 12);
   private static final Logger LOGGER = LogManager.getLogger();
   private DedicatedServer server;
   private static final String __OBFID = "CL_00001789";

   public static void createServerGui(final DedicatedServer serverIn) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var3) {
         ;
      }

      MinecraftServerGui var1 = new MinecraftServerGui(serverIn);
      JFrame var2 = new JFrame("Minecraft server");
      var2.add(var1);
      var2.pack();
      var2.setLocationRelativeTo((Component)null);
      var2.setVisible(true);
      var2.addWindowListener(new WindowAdapter() {
         private static final String __OBFID = "CL_00001791";

         public void windowClosing(WindowEvent p_windowClosing_1_) {
            serverIn.initiateShutdown();

            while(!serverIn.isServerStopped()) {
               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var3) {
                  var3.printStackTrace();
               }
            }

            System.exit(0);
         }
      });
   }

   public MinecraftServerGui(DedicatedServer serverIn) {
      this.server = serverIn;
      this.setPreferredSize(new Dimension(854, 480));
      this.setLayout(new BorderLayout());

      try {
         this.add(this.getLogComponent(), "Center");
         this.add(this.getStatsComponent(), "West");
      } catch (Exception var3) {
         LOGGER.error("Couldn\'t build server GUI", var3);
      }
   }

   private JComponent getStatsComponent() {
      JPanel var1 = new JPanel(new BorderLayout());
      var1.add(new StatsComponent(this.server), "North");
      var1.add(this.getPlayerListComponent(), "Center");
      var1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
      return var1;
   }

   private JComponent getPlayerListComponent() {
      PlayerListComponent var1 = new PlayerListComponent(this.server);
      JScrollPane var2 = new JScrollPane(var1, 22, 30);
      var2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
      return var2;
   }

   private JComponent getLogComponent() {
      JPanel var1 = new JPanel(new BorderLayout());
      final JTextArea var2 = new JTextArea();
      final JScrollPane var3 = new JScrollPane(var2, 22, 30);
      var2.setEditable(false);
      var2.setFont(serverGuiFont);
      final JTextField var4 = new JTextField();
      var4.addActionListener(new ActionListener() {
         private static final String __OBFID = "CL_00001790";

         public void actionPerformed(ActionEvent p_actionPerformed_1_) {
            String var2 = var4.getText().trim();
            if(var2.length() > 0) {
               MinecraftServerGui.this.server.addPendingCommand(var2, MinecraftServer.getServer());
            }

            var4.setText("");
         }
      });
      var2.addFocusListener(new FocusAdapter() {
         private static final String __OBFID = "CL_00001794";

         public void focusGained(FocusEvent p_focusGained_1_) {}
      });
      var1.add(var3, "Center");
      var1.add(var4, "South");
      var1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
      Thread var5 = new Thread(new Runnable() {
         private static final String __OBFID = "CL_00001793";

         public void run() {
            String var1;
            while((var1 = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
               MinecraftServerGui.this.func_164247_a(var2, var3, var1);
            }
         }
      });
      var5.setDaemon(true);
      var5.start();
      return var1;
   }

   public void func_164247_a(final JTextArea p_164247_1_, final JScrollPane p_164247_2_, final String p_164247_3_) {
      if(!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(new Runnable() {
            private static final String __OBFID = "CL_00001792";

            public void run() {
               MinecraftServerGui.this.func_164247_a(p_164247_1_, p_164247_2_, p_164247_3_);
            }
         });
      } else {
         Document var4 = p_164247_1_.getDocument();
         JScrollBar var5 = p_164247_2_.getVerticalScrollBar();
         boolean var6 = false;
         if(p_164247_2_.getViewport().getView() == p_164247_1_) {
            var6 = (double)var5.getValue() + var5.getSize().getHeight() + (double)(serverGuiFont.getSize() * 4) > (double)var5.getMaximum();
         }

         try {
            var4.insertString(var4.getLength(), p_164247_3_, (AttributeSet)null);
         } catch (BadLocationException var8) {
            ;
         }

         if(var6) {
            var5.setValue(Integer.MAX_VALUE);
         }
      }
   }
}
