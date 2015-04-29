package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S32PacketConfirmTransaction implements Packet {
   private int field_148894_a;
   private short field_148892_b;
   private boolean field_148893_c;
   private static final String __OBFID = "CL_00001291";

   public S32PacketConfirmTransaction() {}

   public S32PacketConfirmTransaction(int p_i45182_1_, short p_i45182_2_, boolean p_i45182_3_) {
      this.field_148894_a = p_i45182_1_;
      this.field_148892_b = p_i45182_2_;
      this.field_148893_c = p_i45182_3_;
   }

   public void func_180730_a(INetHandlerPlayClient p_180730_1_) {
      p_180730_1_.handleConfirmTransaction(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.field_148894_a = data.readUnsignedByte();
      this.field_148892_b = data.readShort();
      this.field_148893_c = data.readBoolean();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.field_148894_a);
      data.writeShort(this.field_148892_b);
      data.writeBoolean(this.field_148893_c);
   }

   public void processPacket(INetHandler handler) {
      this.func_180730_a((INetHandlerPlayClient)handler);
   }
}
