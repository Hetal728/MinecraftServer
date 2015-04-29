package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockLeavesBase extends Block {
   protected boolean field_150121_P;
   private static final String __OBFID = "CL_00000326";

   protected BlockLeavesBase(Material p_i45433_1_, boolean p_i45433_2_) {
      super(p_i45433_1_);
      this.field_150121_P = p_i45433_2_;
   }

   public boolean isOpaqueCube() {
      return false;
   }
}
