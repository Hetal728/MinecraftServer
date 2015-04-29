package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface ICommandSender {
   String getName();

   IChatComponent getDisplayName();

   void addChatMessage(IChatComponent message);

   boolean canCommandSenderUseCommand(int permissionLevel, String command);

   BlockPos getPosition();

   Vec3 getPositionVector();

   World getEntityWorld();

   Entity getCommandSenderEntity();

   boolean sendCommandFeedback();

   void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_);
}
