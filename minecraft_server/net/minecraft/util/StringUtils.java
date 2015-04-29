package net.minecraft.util;

import java.util.regex.Pattern;

public class StringUtils {
   private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
   private static final String __OBFID = "CL_00001501";

   public static boolean isNullOrEmpty(String p_151246_0_) {
      return org.apache.commons.lang3.StringUtils.isEmpty(p_151246_0_);
   }
}
