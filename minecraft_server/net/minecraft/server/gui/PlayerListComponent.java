package net.minecraft.server.gui;

import java.util.Vector;
import javax.swing.JList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerListComponent extends JList implements IUpdatePlayerListBox {
   private MinecraftServer server;
   private int ticks;
   private static final String __OBFID = "CL_00001795";

   public PlayerListComponent(MinecraftServer server) {
      this.server = server;
      server.registerTickable(this);
   }

   public void update() {
      if(this.ticks++ % 20 == 0) {
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < this.server.getConfigurationManager().playerEntityList.size(); ++var2) {
            var1.add(((EntityPlayerMP)this.server.getConfigurationManager().playerEntityList.get(var2)).getName());
         }

         this.setListData(var1);
      }
   }
}
