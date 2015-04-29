package net.minecraft.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class StatBase {
   public final String statId;
   private final IChatComponent statName;
   public boolean isIndependent;
   private final IStatType type;
   private final IScoreObjectiveCriteria field_150957_c;
   private Class field_150956_d;
   private static NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
   public static IStatType simpleStatType = new IStatType() {
      private static final String __OBFID = "CL_00001473";
   };
   private static DecimalFormat decimalFormat = new DecimalFormat("########0.00");
   public static IStatType timeStatType = new IStatType() {
      private static final String __OBFID = "CL_00001474";
   };
   public static IStatType distanceStatType = new IStatType() {
      private static final String __OBFID = "CL_00001475";
   };
   public static IStatType field_111202_k = new IStatType() {
      private static final String __OBFID = "CL_00001476";
   };
   private static final String __OBFID = "CL_00001472";

   public StatBase(String p_i45307_1_, IChatComponent p_i45307_2_, IStatType p_i45307_3_) {
      this.statId = p_i45307_1_;
      this.statName = p_i45307_2_;
      this.type = p_i45307_3_;
      this.field_150957_c = new ObjectiveStat(this);
      IScoreObjectiveCriteria.INSTANCES.put(this.field_150957_c.getName(), this.field_150957_c);
   }

   public StatBase(String p_i45308_1_, IChatComponent p_i45308_2_) {
      this(p_i45308_1_, p_i45308_2_, simpleStatType);
   }

   public StatBase initIndependentStat() {
      this.isIndependent = true;
      return this;
   }

   public StatBase registerStat() {
      if(StatList.oneShotStats.containsKey(this.statId)) {
         throw new RuntimeException("Duplicate stat id: \"" + ((StatBase)StatList.oneShotStats.get(this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
      } else {
         StatList.allStats.add(this);
         StatList.oneShotStats.put(this.statId, this);
         return this;
      }
   }

   public boolean isAchievement() {
      return false;
   }

   public IChatComponent getStatName() {
      IChatComponent var1 = this.statName.createCopy();
      var1.getChatStyle().setColor(EnumChatFormatting.GRAY);
      var1.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
      return var1;
   }

   public IChatComponent func_150955_j() {
      IChatComponent var1 = this.getStatName();
      IChatComponent var2 = (new ChatComponentText("[")).appendSibling(var1).appendText("]");
      var2.setChatStyle(var1.getChatStyle());
      return var2;
   }

   public boolean equals(Object p_equals_1_) {
      if(this == p_equals_1_) {
         return true;
      } else if(p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         StatBase var2 = (StatBase)p_equals_1_;
         return this.statId.equals(var2.statId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.statId.hashCode();
   }

   public String toString() {
      return "Stat{id=" + this.statId + ", nameId=" + this.statName + ", awardLocallyOnly=" + this.isIndependent + ", formatter=" + this.type + ", objectiveCriteria=" + this.field_150957_c + '}';
   }

   public IScoreObjectiveCriteria func_150952_k() {
      return this.field_150957_c;
   }

   public Class func_150954_l() {
      return this.field_150956_d;
   }

   public StatBase func_150953_b(Class p_150953_1_) {
      this.field_150956_d = p_150953_1_;
      return this;
   }
}
