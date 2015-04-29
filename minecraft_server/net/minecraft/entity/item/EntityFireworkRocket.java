package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity {
   private int fireworkAge;
   private int lifetime;
   private static final String __OBFID = "CL_00001718";

   public EntityFireworkRocket(World worldIn) {
      super(worldIn);
      this.setSize(0.25F, 0.25F);
   }

   protected void entityInit() {
      this.dataWatcher.addObjectByDataType(8, 5);
   }

   public EntityFireworkRocket(World worldIn, double p_i1763_2_, double p_i1763_4_, double p_i1763_6_, ItemStack p_i1763_8_) {
      super(worldIn);
      this.fireworkAge = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);
      int var9 = 1;
      if(p_i1763_8_ != null && p_i1763_8_.hasTagCompound()) {
         this.dataWatcher.updateObject(8, p_i1763_8_);
         NBTTagCompound var10 = p_i1763_8_.getTagCompound();
         NBTTagCompound var11 = var10.getCompoundTag("Fireworks");
         if(var11 != null) {
            var9 += var11.getByte("Flight");
         }
      }

      this.motionX = this.rand.nextGaussian() * 0.001D;
      this.motionZ = this.rand.nextGaussian() * 0.001D;
      this.motionY = 0.05D;
      this.lifetime = 10 * var9 + this.rand.nextInt(6) + this.rand.nextInt(7);
   }

   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      this.motionX *= 1.15D;
      this.motionZ *= 1.15D;
      this.motionY += 0.04D;
      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

      for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
         ;
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      if(this.fireworkAge == 0 && !this.isSlient()) {
         this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
      }

      ++this.fireworkAge;
      if(this.worldObj.isRemote && this.fireworkAge % 2 < 2) {
         this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
      }

      if(!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
         this.worldObj.setEntityState(this, (byte)17);
         this.setDead();
      }
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      tagCompound.setInteger("Life", this.fireworkAge);
      tagCompound.setInteger("LifeTime", this.lifetime);
      ItemStack var2 = this.dataWatcher.getWatchableObjectItemStack(8);
      if(var2 != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         var2.writeToNBT(var3);
         tagCompound.setTag("FireworksItem", var3);
      }
   }

   public void readEntityFromNBT(NBTTagCompound tagCompund) {
      this.fireworkAge = tagCompund.getInteger("Life");
      this.lifetime = tagCompund.getInteger("LifeTime");
      NBTTagCompound var2 = tagCompund.getCompoundTag("FireworksItem");
      if(var2 != null) {
         ItemStack var3 = ItemStack.loadItemStackFromNBT(var2);
         if(var3 != null) {
            this.dataWatcher.updateObject(8, var3);
         }
      }
   }

   public float getBrightness(float p_70013_1_) {
      return super.getBrightness(p_70013_1_);
   }

   public boolean canAttackWithItem() {
      return false;
   }
}
