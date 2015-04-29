package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction implements Packet {
   private int id;
   private short uid;
   private boolean accepted;
   private static final String __OBFID = "CL_00001351";

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processConfirmTransaction(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.id = data.readByte();
      this.uid = data.readShort();
      this.accepted = data.readByte() != 0;
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.id);
      data.writeShort(this.uid);
      data.writeByte(this.accepted?1:0);
   }

   public int getId() {
      return this.id;
   }

   public short getUid() {
      return this.uid;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayServer)handler);
   }
}
