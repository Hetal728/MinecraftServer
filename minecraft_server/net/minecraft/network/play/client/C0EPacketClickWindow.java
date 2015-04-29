package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow implements Packet {
   private int windowId;
   private int slotId;
   private int usedButton;
   private short actionNumber;
   private ItemStack clickedItem;
   private int mode;
   private static final String __OBFID = "CL_00001353";

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processClickWindow(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.windowId = data.readByte();
      this.slotId = data.readShort();
      this.usedButton = data.readByte();
      this.actionNumber = data.readShort();
      this.mode = data.readByte();
      this.clickedItem = data.readItemStackFromBuffer();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.windowId);
      data.writeShort(this.slotId);
      data.writeByte(this.usedButton);
      data.writeShort(this.actionNumber);
      data.writeByte(this.mode);
      data.writeItemStackToBuffer(this.clickedItem);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getSlotId() {
      return this.slotId;
   }

   public int getUsedButton() {
      return this.usedButton;
   }

   public short getActionNumber() {
      return this.actionNumber;
   }

   public ItemStack getClickedItem() {
      return this.clickedItem;
   }

   public int getMode() {
      return this.mode;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayServer)handler);
   }
}
