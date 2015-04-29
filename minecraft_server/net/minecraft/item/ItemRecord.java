package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRecord extends Item {
   private static final Map field_150928_b = Maps.newHashMap();
   public final String recordName;
   private static final String __OBFID = "CL_00000057";

   protected ItemRecord(String p_i45350_1_) {
      this.recordName = p_i45350_1_;
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.tabMisc);
      field_150928_b.put("records." + p_i45350_1_, this);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      IBlockState var9 = worldIn.getBlockState(pos);
      if(var9.getBlock() == Blocks.jukebox && !((Boolean)var9.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) {
         if(worldIn.isRemote) {
            return true;
         } else {
            ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, var9, stack);
            worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1005, pos, Item.getIdFromItem(this));
            --stack.stackSize;
            return true;
         }
      } else {
         return false;
      }
   }

   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.RARE;
   }
}
