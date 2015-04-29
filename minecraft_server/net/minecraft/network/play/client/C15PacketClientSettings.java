package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C15PacketClientSettings implements Packet {
   private String lang;
   private int view;
   private EntityPlayer.EnumChatVisibility chatVisibility;
   private boolean enableColors;
   private int field_179711_e;
   private static final String __OBFID = "CL_00001350";

   public void readPacketData(PacketBuffer data) throws IOException {
      this.lang = data.readStringFromBuffer(7);
      this.view = data.readByte();
      this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility(data.readByte());
      this.enableColors = data.readBoolean();
      this.field_179711_e = data.readUnsignedByte();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeString(this.lang);
      data.writeByte(this.view);
      data.writeByte(this.chatVisibility.getChatVisibility());
      data.writeBoolean(this.enableColors);
      data.writeByte(this.field_179711_e);
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processClientSettings(this);
   }

   public String getLang() {
      return this.lang;
   }

   public EntityPlayer.EnumChatVisibility getChatVisibility() {
      return this.chatVisibility;
   }

   public boolean isColorsEnabled() {
      return this.enableColors;
   }

   public int getView() {
      return this.field_179711_e;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayServer)handler);
   }
}