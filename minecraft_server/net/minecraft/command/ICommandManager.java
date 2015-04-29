package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.BlockPos;

public interface ICommandManager {
   int executeCommand(ICommandSender sender, String command);

   List getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos);

   List getPossibleCommands(ICommandSender sender);

   Map getCommands();
}
