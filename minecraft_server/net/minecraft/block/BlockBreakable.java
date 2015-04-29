package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockBreakable extends Block {
   private boolean ignoreSimilarity;
   private static final String __OBFID = "CL_00000254";

   protected BlockBreakable(Material p_i45712_1_, boolean p_i45712_2_) {
      super(p_i45712_1_);
      this.ignoreSimilarity = p_i45712_2_;
   }

   public boolean isOpaqueCube() {
      return false;
   }
}
