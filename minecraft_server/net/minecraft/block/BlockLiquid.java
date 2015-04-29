package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block {
   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
   private static final String __OBFID = "CL_00000265";

   protected BlockLiquid(Material p_i45413_1_) {
      super(p_i45413_1_);
      this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setTickRandomly(true);
   }

   public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
      return this.blockMaterial != Material.lava;
   }

   public static float getLiquidHeightPercent(int p_149801_0_) {
      if(p_149801_0_ >= 8) {
         p_149801_0_ = 0;
      }

      return (float)(p_149801_0_ + 1) / 9.0F;
   }

   protected int func_176362_e(IBlockAccess p_176362_1_, BlockPos p_176362_2_) {
      return p_176362_1_.getBlockState(p_176362_2_).getBlock().getMaterial() == this.blockMaterial?((Integer)p_176362_1_.getBlockState(p_176362_2_).getValue(LEVEL)).intValue():-1;
   }

   protected int func_176366_f(IBlockAccess p_176366_1_, BlockPos p_176366_2_) {
      int var3 = this.func_176362_e(p_176366_1_, p_176366_2_);
      return var3 >= 8?0:var3;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
      return p_176209_2_ && ((Integer)state.getValue(LEVEL)).intValue() == 0;
   }

   public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      Material var4 = worldIn.getBlockState(pos).getBlock().getMaterial();
      return var4 == this.blockMaterial?false:(side == EnumFacing.UP?true:(var4 == Material.ice?false:super.isBlockSolid(worldIn, pos, side)));
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public int getRenderType() {
      return 1;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   protected Vec3 func_180687_h(IBlockAccess p_180687_1_, BlockPos p_180687_2_) {
      Vec3 var3 = new Vec3(0.0D, 0.0D, 0.0D);
      int var4 = this.func_176366_f(p_180687_1_, p_180687_2_);
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      EnumFacing var6;
      BlockPos var7;
      while(var5.hasNext()) {
         var6 = (EnumFacing)var5.next();
         var7 = p_180687_2_.offset(var6);
         int var8 = this.func_176366_f(p_180687_1_, var7);
         int var9;
         if(var8 < 0) {
            if(!p_180687_1_.getBlockState(var7).getBlock().getMaterial().blocksMovement()) {
               var8 = this.func_176366_f(p_180687_1_, var7.offsetDown());
               if(var8 >= 0) {
                  var9 = var8 - (var4 - 8);
                  var3 = var3.addVector((double)((var7.getX() - p_180687_2_.getX()) * var9), (double)((var7.getY() - p_180687_2_.getY()) * var9), (double)((var7.getZ() - p_180687_2_.getZ()) * var9));
               }
            }
         } else if(var8 >= 0) {
            var9 = var8 - var4;
            var3 = var3.addVector((double)((var7.getX() - p_180687_2_.getX()) * var9), (double)((var7.getY() - p_180687_2_.getY()) * var9), (double)((var7.getZ() - p_180687_2_.getZ()) * var9));
         }
      }

      if(((Integer)p_180687_1_.getBlockState(p_180687_2_).getValue(LEVEL)).intValue() >= 8) {
         var5 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var5.hasNext()) {
            var6 = (EnumFacing)var5.next();
            var7 = p_180687_2_.offset(var6);
            if(this.isBlockSolid(p_180687_1_, var7, var6) || this.isBlockSolid(p_180687_1_, var7.offsetUp(), var6)) {
               var3 = var3.normalize().addVector(0.0D, -6.0D, 0.0D);
               break;
            }
         }
      }

      return var3.normalize();
   }

   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
      return motion.add(this.func_180687_h(worldIn, pos));
   }

   public int tickRate(World worldIn) {
      return this.blockMaterial == Material.water?5:(this.blockMaterial == Material.lava?(worldIn.provider.getHasNoSky()?10:30):0);
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      this.func_176365_e(worldIn, pos, state);
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      this.func_176365_e(worldIn, pos, state);
   }

   public boolean func_176365_e(World worldIn, BlockPos p_176365_2_, IBlockState p_176365_3_) {
      if(this.blockMaterial == Material.lava) {
         boolean var4 = false;
         EnumFacing[] var5 = EnumFacing.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            EnumFacing var8 = var5[var7];
            if(var8 != EnumFacing.DOWN && worldIn.getBlockState(p_176365_2_.offset(var8)).getBlock().getMaterial() == Material.water) {
               var4 = true;
               break;
            }
         }

         if(var4) {
            Integer var9 = (Integer)p_176365_3_.getValue(LEVEL);
            if(var9.intValue() == 0) {
               worldIn.setBlockState(p_176365_2_, Blocks.obsidian.getDefaultState());
               this.func_180688_d(worldIn, p_176365_2_);
               return true;
            }

            if(var9.intValue() <= 4) {
               worldIn.setBlockState(p_176365_2_, Blocks.cobblestone.getDefaultState());
               this.func_180688_d(worldIn, p_176365_2_);
               return true;
            }
         }
      }

      return false;
   }

   protected void func_180688_d(World worldIn, BlockPos p_180688_2_) {
      double var3 = (double)p_180688_2_.getX();
      double var5 = (double)p_180688_2_.getY();
      double var7 = (double)p_180688_2_.getZ();
      worldIn.playSoundEffect(var3 + 0.5D, var5 + 0.5D, var7 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

      for(int var9 = 0; var9 < 8; ++var9) {
         worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var3 + Math.random(), var5 + 1.2D, var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
      }
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((Integer)state.getValue(LEVEL)).intValue();
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{LEVEL});
   }

   public static BlockDynamicLiquid getDynamicLiquidForMaterial(Material p_176361_0_) {
      if(p_176361_0_ == Material.water) {
         return Blocks.flowing_water;
      } else if(p_176361_0_ == Material.lava) {
         return Blocks.flowing_lava;
      } else {
         throw new IllegalArgumentException("Invalid material");
      }
   }

   public static BlockStaticLiquid getStaticLiquidForMaterial(Material p_176363_0_) {
      if(p_176363_0_ == Material.water) {
         return Blocks.water;
      } else if(p_176363_0_ == Material.lava) {
         return Blocks.lava;
      } else {
         throw new IllegalArgumentException("Invalid material");
      }
   }
}
