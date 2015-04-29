package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer implements Packet {
   protected double x;
   protected double y;
   protected double z;
   protected float yaw;
   protected float pitch;
   protected boolean field_149474_g;
   protected boolean field_149480_h;
   protected boolean rotating;
   private static final String __OBFID = "CL_00001360";

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processPlayer(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.field_149474_g = data.readUnsignedByte() != 0;
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.field_149474_g?1:0);
   }

   public double getPositionX() {
      return this.x;
   }

   public double getPositionY() {
      return this.y;
   }

   public double getPositionZ() {
      return this.z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public boolean func_149465_i() {
      return this.field_149474_g;
   }

   public boolean func_149466_j() {
      return this.field_149480_h;
   }

   public boolean getRotating() {
      return this.rotating;
   }

   public void func_149469_a(boolean p_149469_1_) {
      this.field_149480_h = p_149469_1_;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayServer)handler);
   }

   public static class C04PacketPlayerPosition extends C03PacketPlayer {
      private static final String __OBFID = "CL_00001361";

      public C04PacketPlayerPosition() {
         this.field_149480_h = true;
      }

      public void readPacketData(PacketBuffer data) throws IOException {
         this.x = data.readDouble();
         this.y = data.readDouble();
         this.z = data.readDouble();
         super.readPacketData(data);
      }

      public void writePacketData(PacketBuffer data) throws IOException {
         data.writeDouble(this.x);
         data.writeDouble(this.y);
         data.writeDouble(this.z);
         super.writePacketData(data);
      }

      public void processPacket(INetHandler handler) {
         super.processPacket((INetHandlerPlayServer)handler);
      }
   }

   public static class C05PacketPlayerLook extends C03PacketPlayer {
      private static final String __OBFID = "CL_00001363";

      public C05PacketPlayerLook() {
         this.rotating = true;
      }

      public void readPacketData(PacketBuffer data) throws IOException {
         this.yaw = data.readFloat();
         this.pitch = data.readFloat();
         super.readPacketData(data);
      }

      public void writePacketData(PacketBuffer data) throws IOException {
         data.writeFloat(this.yaw);
         data.writeFloat(this.pitch);
         super.writePacketData(data);
      }

      public void processPacket(INetHandler handler) {
         super.processPacket((INetHandlerPlayServer)handler);
      }
   }

   public static class C06PacketPlayerPosLook extends C03PacketPlayer {
      private static final String __OBFID = "CL_00001362";

      public C06PacketPlayerPosLook() {
         this.field_149480_h = true;
         this.rotating = true;
      }

      public void readPacketData(PacketBuffer data) throws IOException {
         this.x = data.readDouble();
         this.y = data.readDouble();
         this.z = data.readDouble();
         this.yaw = data.readFloat();
         this.pitch = data.readFloat();
         super.readPacketData(data);
      }

      public void writePacketData(PacketBuffer data) throws IOException {
         data.writeDouble(this.x);
         data.writeDouble(this.y);
         data.writeDouble(this.z);
         data.writeFloat(this.yaw);
         data.writeFloat(this.pitch);
         super.writePacketData(data);
      }

      public void processPacket(INetHandler handler) {
         super.processPacket((INetHandlerPlayServer)handler);
      }
   }
}
