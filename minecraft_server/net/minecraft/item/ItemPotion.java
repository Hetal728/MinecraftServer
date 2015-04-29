package net.minecraft.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion extends Item {
   private Map effectCache = Maps.newHashMap();
   private static final Map field_77835_b = Maps.newLinkedHashMap();
   private static final String __OBFID = "CL_00000055";

   public ItemPotion() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(CreativeTabs.tabBrewing);
   }

   public List getEffects(ItemStack p_77832_1_) {
      if(p_77832_1_.hasTagCompound() && p_77832_1_.getTagCompound().hasKey("CustomPotionEffects", 9)) {
         ArrayList var7 = Lists.newArrayList();
         NBTTagList var3 = p_77832_1_.getTagCompound().getTagList("CustomPotionEffects", 10);

         for(int var4 = 0; var4 < var3.tagCount(); ++var4) {
            NBTTagCompound var5 = var3.getCompoundTagAt(var4);
            PotionEffect var6 = PotionEffect.readCustomPotionEffectFromNBT(var5);
            if(var6 != null) {
               var7.add(var6);
            }
         }

         return var7;
      } else {
         List var2 = (List)this.effectCache.get(Integer.valueOf(p_77832_1_.getMetadata()));
         if(var2 == null) {
            var2 = PotionHelper.getPotionEffects(p_77832_1_.getMetadata(), false);
            this.effectCache.put(Integer.valueOf(p_77832_1_.getMetadata()), var2);
         }

         return var2;
      }
   }

   public List getEffects(int p_77834_1_) {
      List var2 = (List)this.effectCache.get(Integer.valueOf(p_77834_1_));
      if(var2 == null) {
         var2 = PotionHelper.getPotionEffects(p_77834_1_, false);
         this.effectCache.put(Integer.valueOf(p_77834_1_), var2);
      }

      return var2;
   }

   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
      if(!playerIn.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      if(!worldIn.isRemote) {
         List var4 = this.getEffects(stack);
         if(var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               PotionEffect var6 = (PotionEffect)var5.next();
               playerIn.addPotionEffect(new PotionEffect(var6));
            }
         }
      }

      playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      if(!playerIn.capabilities.isCreativeMode) {
         if(stack.stackSize <= 0) {
            return new ItemStack(Items.glass_bottle);
         }

         playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
      }

      return stack;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return 32;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.DRINK;
   }

   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
      if(isSplash(itemStackIn.getMetadata())) {
         if(!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
         }

         worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
         if(!worldIn.isRemote) {
            worldIn.spawnEntityInWorld(new EntityPotion(worldIn, playerIn, itemStackIn));
         }

         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
         return itemStackIn;
      } else {
         playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
         return itemStackIn;
      }
   }

   public static boolean isSplash(int p_77831_0_) {
      return (p_77831_0_ & 16384) != 0;
   }

   public String getItemStackDisplayName(ItemStack stack) {
      if(stack.getMetadata() == 0) {
         return StatCollector.translateToLocal("item.emptyPotion.name").trim();
      } else {
         String var2 = "";
         if(isSplash(stack.getMetadata())) {
            var2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
         }

         List var3 = Items.potionitem.getEffects(stack);
         String var4;
         if(var3 != null && !var3.isEmpty()) {
            var4 = ((PotionEffect)var3.get(0)).getEffectName();
            var4 = var4 + ".postfix";
            return var2 + StatCollector.translateToLocal(var4).trim();
         } else {
            var4 = PotionHelper.func_77905_c(stack.getMetadata());
            return StatCollector.translateToLocal(var4).trim() + " " + super.getItemStackDisplayName(stack);
         }
      }
   }
}
