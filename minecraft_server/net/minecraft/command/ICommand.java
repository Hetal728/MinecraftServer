package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public interface ICommand extends Comparable {
   String getCommandName();

   String getCommandUsage(ICommandSender sender);

   List getCommandAliases();

   void processCommand(ICommandSender sender, String[] args) throws CommandException;

   boolean canCommandSenderUseCommand(ICommandSender sender);

   List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos);

   boolean isUsernameIndex(String[] args, int index);
}
