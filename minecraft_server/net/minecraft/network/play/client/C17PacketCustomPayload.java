package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C17PacketCustomPayload implements Packet {
   private String channel;
   private PacketBuffer data;
   private static final String __OBFID = "CL_00001356";

   public void readPacketData(PacketBuffer data) throws IOException {
      this.channel = data.readStringFromBuffer(20);
      int var2 = data.readableBytes();
      if(var2 >= 0 && var2 <= 32767) {
         this.data = new PacketBuffer(data.readBytes(var2));
      } else {
         throw new IOException("Payload may not be larger than 32767 bytes");
      }
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeString(this.channel);
      data.writeBytes((ByteBuf)this.data);
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processVanilla250Packet(this);
   }

   public String getChannelName() {
      return this.channel;
   }

   public PacketBuffer getBufferData() {
      return this.data;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayServer)handler);
   }
}
