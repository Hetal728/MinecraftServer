package net.minecraft.creativetab;

import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class CreativeTabs {
   public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
   public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks") {
      private static final String __OBFID = "CL_00000006";
   };
   public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations") {
      private static final String __OBFID = "CL_00000010";
   };
   public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone") {
      private static final String __OBFID = "CL_00000011";
   };
   public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation") {
      private static final String __OBFID = "CL_00000012";
   };
   public static final CreativeTabs tabMisc = (new CreativeTabs(4, "misc") {
      private static final String __OBFID = "CL_00000014";
   }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ALL});
   public static final CreativeTabs tabAllSearch = (new CreativeTabs(5, "search") {
      private static final String __OBFID = "CL_00000015";
   }).setBackgroundImageName("item_search.png");
   public static final CreativeTabs tabFood = new CreativeTabs(6, "food") {
      private static final String __OBFID = "CL_00000016";
   };
   public static final CreativeTabs tabTools = (new CreativeTabs(7, "tools") {
      private static final String __OBFID = "CL_00000017";
   }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
   public static final CreativeTabs tabCombat = (new CreativeTabs(8, "combat") {
      private static final String __OBFID = "CL_00000018";
   }).setRelevantEnchantmentTypes(new EnumEnchantmentType[]{EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON});
   public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing") {
      private static final String __OBFID = "CL_00000007";
   };
   public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials") {
      private static final String __OBFID = "CL_00000008";
   };
   public static final CreativeTabs tabInventory = (new CreativeTabs(11, "inventory") {
      private static final String __OBFID = "CL_00000009";
   }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
   private final int tabIndex;
   private final String tabLabel;
   private String theTexture = "items.png";
   private boolean hasScrollbar = true;
   private boolean drawTitle = true;
   private EnumEnchantmentType[] enchantmentTypes;
   private static final String __OBFID = "CL_00000005";

   public CreativeTabs(int index, String label) {
      this.tabIndex = index;
      this.tabLabel = label;
      creativeTabArray[index] = this;
   }

   public CreativeTabs setBackgroundImageName(String texture) {
      this.theTexture = texture;
      return this;
   }

   public CreativeTabs setNoTitle() {
      this.drawTitle = false;
      return this;
   }

   public CreativeTabs setNoScrollbar() {
      this.hasScrollbar = false;
      return this;
   }

   public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType ... types) {
      this.enchantmentTypes = types;
      return this;
   }
}
