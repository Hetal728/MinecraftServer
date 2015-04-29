package net.minecraft.network;

import java.io.IOException;

public interface Packet {
   void readPacketData(PacketBuffer data) throws IOException;

   void writePacketData(PacketBuffer data) throws IOException;

   void processPacket(INetHandler handler);
}
