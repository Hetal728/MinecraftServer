package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState {
   Collection getPropertyNames();

   Comparable getValue(IProperty property);

   IBlockState withProperty(IProperty property, Comparable value);

   IBlockState cycleProperty(IProperty property);

   ImmutableMap getProperties();

   Block getBlock();
}
