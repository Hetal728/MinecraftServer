package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public final class ItemStack {
   public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");
   public int stackSize;
   public int animationsToGo;
   private Item item;
   private NBTTagCompound stackTagCompound;
   private int itemDamage;
   private EntityItemFrame itemFrame;
   private Block canDestroyCacheBlock;
   private boolean canDestroyCacheResult;
   private Block canPlaceOnCacheBlock;
   private boolean canPlaceOnCacheResult;
   private static final String __OBFID = "CL_00000043";

   public ItemStack(Block blockIn) {
      this(blockIn, 1);
   }

   public ItemStack(Block blockIn, int amount) {
      this(blockIn, amount, 0);
   }

   public ItemStack(Block blockIn, int amount, int meta) {
      this(Item.getItemFromBlock(blockIn), amount, meta);
   }

   public ItemStack(Item itemIn) {
      this(itemIn, 1);
   }

   public ItemStack(Item itemIn, int amount) {
      this(itemIn, amount, 0);
   }

   public ItemStack(Item itemIn, int amount, int meta) {
      this.canDestroyCacheBlock = null;
      this.canDestroyCacheResult = false;
      this.canPlaceOnCacheBlock = null;
      this.canPlaceOnCacheResult = false;
      this.item = itemIn;
      this.stackSize = amount;
      this.itemDamage = meta;
      if(this.itemDamage < 0) {
         this.itemDamage = 0;
      }
   }

   public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt) {
      ItemStack var1 = new ItemStack();
      var1.readFromNBT(nbt);
      return var1.getItem() != null?var1:null;
   }

   private ItemStack() {
      this.canDestroyCacheBlock = null;
      this.canDestroyCacheResult = false;
      this.canPlaceOnCacheBlock = null;
      this.canPlaceOnCacheResult = false;
   }

   public ItemStack splitStack(int amount) {
      ItemStack var2 = new ItemStack(this.item, amount, this.itemDamage);
      if(this.stackTagCompound != null) {
         var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
      }

      this.stackSize -= amount;
      return var2;
   }

   public Item getItem() {
      return this.item;
   }

   public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      boolean var8 = this.getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
      if(var8) {
         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }

      return var8;
   }

   public float getStrVsBlock(Block p_150997_1_) {
      return this.getItem().getStrVsBlock(this, p_150997_1_);
   }

   public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn) {
      return this.getItem().onItemRightClick(this, worldIn, playerIn);
   }

   public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn) {
      return this.getItem().onItemUseFinish(this, worldIn, playerIn);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
      nbt.setString("id", var2 == null?"minecraft:air":var2.toString());
      nbt.setByte("Count", (byte)this.stackSize);
      nbt.setShort("Damage", (short)this.itemDamage);
      if(this.stackTagCompound != null) {
         nbt.setTag("tag", this.stackTagCompound);
      }

      return nbt;
   }

   public void readFromNBT(NBTTagCompound nbt) {
      if(nbt.hasKey("id", 8)) {
         this.item = Item.getByNameOrId(nbt.getString("id"));
      } else {
         this.item = Item.getItemById(nbt.getShort("id"));
      }

      this.stackSize = nbt.getByte("Count");
      this.itemDamage = nbt.getShort("Damage");
      if(this.itemDamage < 0) {
         this.itemDamage = 0;
      }

      if(nbt.hasKey("tag", 10)) {
         this.stackTagCompound = nbt.getCompoundTag("tag");
         if(this.item != null) {
            this.item.updateItemStackNBT(this.stackTagCompound);
         }
      }
   }

   public int getMaxStackSize() {
      return this.getItem().getItemStackLimit();
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
   }

   public boolean isItemStackDamageable() {
      return this.item == null?false:(this.item.getMaxDamage() <= 0?false:!this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
   }

   public boolean getHasSubtypes() {
      return this.item.getHasSubtypes();
   }

   public boolean isItemDamaged() {
      return this.isItemStackDamageable() && this.itemDamage > 0;
   }

   public int getItemDamage() {
      return this.itemDamage;
   }

   public int getMetadata() {
      return this.itemDamage;
   }

   public void setItemDamage(int meta) {
      this.itemDamage = meta;
      if(this.itemDamage < 0) {
         this.itemDamage = 0;
      }
   }

   public int getMaxDamage() {
      return this.item.getMaxDamage();
   }

   public boolean attemptDamageItem(int amount, Random rand) {
      if(!this.isItemStackDamageable()) {
         return false;
      } else {
         if(amount > 0) {
            int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
            int var4 = 0;

            for(int var5 = 0; var3 > 0 && var5 < amount; ++var5) {
               if(EnchantmentDurability.negateDamage(this, var3, rand)) {
                  ++var4;
               }
            }

            amount -= var4;
            if(amount <= 0) {
               return false;
            }
         }

         this.itemDamage += amount;
         return this.itemDamage > this.getMaxDamage();
      }
   }

   public void damageItem(int amount, EntityLivingBase entityIn) {
      if(!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) {
         if(this.isItemStackDamageable()) {
            if(this.attemptDamageItem(amount, entityIn.getRNG())) {
               entityIn.renderBrokenItemStack(this);
               --this.stackSize;
               if(entityIn instanceof EntityPlayer) {
                  EntityPlayer var3 = (EntityPlayer)entityIn;
                  var3.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
                  if(this.stackSize == 0 && this.getItem() instanceof ItemBow) {
                     var3.destroyCurrentEquippedItem();
                  }
               }

               if(this.stackSize < 0) {
                  this.stackSize = 0;
               }

               this.itemDamage = 0;
            }
         }
      }
   }

   public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
      boolean var3 = this.item.hitEntity(this, entityIn, playerIn);
      if(var3) {
         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }
   }

   public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn) {
      boolean var5 = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
      if(var5) {
         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
      }
   }

   public boolean canHarvestBlock(Block p_150998_1_) {
      return this.item.canHarvestBlock(p_150998_1_);
   }

   public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn) {
      return this.item.itemInteractionForEntity(this, playerIn, entityIn);
   }

   public ItemStack copy() {
      ItemStack var1 = new ItemStack(this.item, this.stackSize, this.itemDamage);
      if(this.stackTagCompound != null) {
         var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
      }

      return var1;
   }

   public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
      return stackA == null && stackB == null?true:(stackA != null && stackB != null?(stackA.stackTagCompound == null && stackB.stackTagCompound != null?false:stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)):false);
   }

   public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
      return stackA == null && stackB == null?true:(stackA != null && stackB != null?stackA.isItemStackEqual(stackB):false);
   }

   private boolean isItemStackEqual(ItemStack other) {
      return this.stackSize != other.stackSize?false:(this.item != other.item?false:(this.itemDamage != other.itemDamage?false:(this.stackTagCompound == null && other.stackTagCompound != null?false:this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound))));
   }

   public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
      return stackA == null && stackB == null?true:(stackA != null && stackB != null?stackA.isItemEqual(stackB):false);
   }

   public boolean isItemEqual(ItemStack other) {
      return other != null && this.item == other.item && this.itemDamage == other.itemDamage;
   }

   public String getUnlocalizedName() {
      return this.item.getUnlocalizedName(this);
   }

   public static ItemStack copyItemStack(ItemStack stack) {
      return stack == null?null:stack.copy();
   }

   public String toString() {
      return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
   }

   public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
      if(this.animationsToGo > 0) {
         --this.animationsToGo;
      }

      this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
   }

   public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
      playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
      this.item.onCreated(this, worldIn, playerIn);
   }

   public int getMaxItemUseDuration() {
      return this.getItem().getMaxItemUseDuration(this);
   }

   public EnumAction getItemUseAction() {
      return this.getItem().getItemUseAction(this);
   }

   public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft) {
      this.getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
   }

   public boolean hasTagCompound() {
      return this.stackTagCompound != null;
   }

   public NBTTagCompound getTagCompound() {
      return this.stackTagCompound;
   }

   public NBTTagCompound getSubCompound(String key, boolean create) {
      if(this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
         return this.stackTagCompound.getCompoundTag(key);
      } else if(create) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.setTagInfo(key, var3);
         return var3;
      } else {
         return null;
      }
   }

   public NBTTagList getEnchantmentTagList() {
      return this.stackTagCompound == null?null:this.stackTagCompound.getTagList("ench", 10);
   }

   public void setTagCompound(NBTTagCompound nbt) {
      this.stackTagCompound = nbt;
   }

   public String getDisplayName() {
      String var1 = this.getItem().getItemStackDisplayName(this);
      if(this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
         NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("display");
         if(var2.hasKey("Name", 8)) {
            var1 = var2.getString("Name");
         }
      }

      return var1;
   }

   public ItemStack setStackDisplayName(String p_151001_1_) {
      if(this.stackTagCompound == null) {
         this.stackTagCompound = new NBTTagCompound();
      }

      if(!this.stackTagCompound.hasKey("display", 10)) {
         this.stackTagCompound.setTag("display", new NBTTagCompound());
      }

      this.stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
      return this;
   }

   public void clearCustomName() {
      if(this.stackTagCompound != null) {
         if(this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
            var1.removeTag("Name");
            if(var1.hasNoTags()) {
               this.stackTagCompound.removeTag("display");
               if(this.stackTagCompound.hasNoTags()) {
                  this.setTagCompound((NBTTagCompound)null);
               }
            }
         }
      }
   }

   public boolean hasDisplayName() {
      return this.stackTagCompound == null?false:(!this.stackTagCompound.hasKey("display", 10)?false:this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
   }

   public EnumRarity getRarity() {
      return this.getItem().getRarity(this);
   }

   public boolean isItemEnchantable() {
      return !this.getItem().isItemTool(this)?false:!this.isItemEnchanted();
   }

   public void addEnchantment(Enchantment ench, int level) {
      if(this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      if(!this.stackTagCompound.hasKey("ench", 9)) {
         this.stackTagCompound.setTag("ench", new NBTTagList());
      }

      NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
      NBTTagCompound var4 = new NBTTagCompound();
      var4.setShort("id", (short)ench.effectId);
      var4.setShort("lvl", (short)((byte)level));
      var3.appendTag(var4);
   }

   public boolean isItemEnchanted() {
      return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
   }

   public void setTagInfo(String key, NBTBase value) {
      if(this.stackTagCompound == null) {
         this.setTagCompound(new NBTTagCompound());
      }

      this.stackTagCompound.setTag(key, value);
   }

   public boolean canEditBlocks() {
      return this.getItem().canItemEditBlocks();
   }

   public boolean isOnItemFrame() {
      return this.itemFrame != null;
   }

   public void setItemFrame(EntityItemFrame frame) {
      this.itemFrame = frame;
   }

   public EntityItemFrame getItemFrame() {
      return this.itemFrame;
   }

   public int getRepairCost() {
      return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3)?this.stackTagCompound.getInteger("RepairCost"):0;
   }

   public void setRepairCost(int cost) {
      if(!this.hasTagCompound()) {
         this.stackTagCompound = new NBTTagCompound();
      }

      this.stackTagCompound.setInteger("RepairCost", cost);
   }

   public Multimap getAttributeModifiers() {
      Object var1;
      if(this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
         var1 = HashMultimap.create();
         NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);

         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);
            if(var5 != null && var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L) {
               ((Multimap)var1).put(var4.getString("AttributeName"), var5);
            }
         }
      } else {
         var1 = this.getItem().getItemAttributeModifiers();
      }

      return (Multimap)var1;
   }

   public void setItem(Item p_150996_1_) {
      this.item = p_150996_1_;
   }

   public IChatComponent getChatComponent() {
      ChatComponentText var1 = new ChatComponentText(this.getDisplayName());
      if(this.hasDisplayName()) {
         var1.getChatStyle().setItalic(Boolean.valueOf(true));
      }

      IChatComponent var2 = (new ChatComponentText("[")).appendSibling(var1).appendText("]");
      if(this.item != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.writeToNBT(var3);
         var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
         var2.getChatStyle().setColor(this.getRarity().rarityColor);
      }

      return var2;
   }

   public boolean canDestroy(Block blockIn) {
      if(blockIn == this.canDestroyCacheBlock) {
         return this.canDestroyCacheResult;
      } else {
         this.canDestroyCacheBlock = blockIn;
         if(this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanDestroy", 8);

            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
               if(var4 == blockIn) {
                  this.canDestroyCacheResult = true;
                  return true;
               }
            }
         }

         this.canDestroyCacheResult = false;
         return false;
      }
   }

   public boolean canPlaceOn(Block blockIn) {
      if(blockIn == this.canPlaceOnCacheBlock) {
         return this.canPlaceOnCacheResult;
      } else {
         this.canPlaceOnCacheBlock = blockIn;
         if(this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);

            for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
               Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
               if(var4 == blockIn) {
                  this.canPlaceOnCacheResult = true;
                  return true;
               }
            }
         }

         this.canPlaceOnCacheResult = false;
         return false;
      }
   }
}
