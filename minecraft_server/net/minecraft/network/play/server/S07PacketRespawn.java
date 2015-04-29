package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S07PacketRespawn implements Packet {
   private int field_149088_a;
   private EnumDifficulty field_149086_b;
   private WorldSettings.GameType field_149087_c;
   private WorldType field_149085_d;
   private static final String __OBFID = "CL_00001322";

   public S07PacketRespawn() {}

   public S07PacketRespawn(int p_i45213_1_, EnumDifficulty p_i45213_2_, WorldType p_i45213_3_, WorldSettings.GameType p_i45213_4_) {
      this.field_149088_a = p_i45213_1_;
      this.field_149086_b = p_i45213_2_;
      this.field_149087_c = p_i45213_4_;
      this.field_149085_d = p_i45213_3_;
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleRespawn(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.field_149088_a = data.readInt();
      this.field_149086_b = EnumDifficulty.getDifficultyEnum(data.readUnsignedByte());
      this.field_149087_c = WorldSettings.GameType.getByID(data.readUnsignedByte());
      this.field_149085_d = WorldType.parseWorldType(data.readStringFromBuffer(16));
      if(this.field_149085_d == null) {
         this.field_149085_d = WorldType.DEFAULT;
      }
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeInt(this.field_149088_a);
      data.writeByte(this.field_149086_b.getDifficultyId());
      data.writeByte(this.field_149087_c.getID());
      data.writeString(this.field_149085_d.getWorldTypeName());
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayClient)handler);
   }
}
