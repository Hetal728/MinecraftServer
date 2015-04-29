package net.minecraft.network.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RConOutputStream {
   private ByteArrayOutputStream byteArrayOutput;
   private DataOutputStream output;
   private static final String __OBFID = "CL_00001798";

   public RConOutputStream(int size) {
      this.byteArrayOutput = new ByteArrayOutputStream(size);
      this.output = new DataOutputStream(this.byteArrayOutput);
   }

   public void writeByteArray(byte[] data) throws IOException {
      this.output.write(data, 0, data.length);
   }

   public void writeString(String data) throws IOException {
      this.output.writeBytes(data);
      this.output.write(0);
   }

   public void writeInt(int data) throws IOException {
      this.output.write(data);
   }

   public void writeShort(short data) throws IOException {
      this.output.writeShort(Short.reverseBytes(data));
   }

   public byte[] toByteArray() {
      return this.byteArrayOutput.toByteArray();
   }

   public void reset() {
      this.byteArrayOutput.reset();
   }
}
