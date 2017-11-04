package mc.oceanica.command;

import mc.oceanica.OceanicaConfig;
import mc.structgen.command.RegenChunkCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class RegenChunkNoReefCommand extends RegenChunkCommand {
    @Override
    public String getName() {
        return "regennr";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "regennr";
    }


    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] strings) throws CommandException {
        boolean originalGenerateReef = OceanicaConfig.generateReef;
        OceanicaConfig.generateReef = false;

        super.execute(minecraftServer, sender, strings);
        OceanicaConfig.generateReef = originalGenerateReef;
    }

}
