package mc.oceanica.command;

import mc.oceanica.OceanicaStats;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class StatCommand extends CommandBase {
    private final OceanicaStats stats;

    public StatCommand(OceanicaStats stats) {
        this.stats=stats;
    }

    @Override
    public String getName() {
        return "chunkstat";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "chunkstat";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString("Processed Chunks: " + stats.getChunksProcessed()));
        sender.sendMessage(new TextComponentString("Primary Seed Count: " + stats.getPrimarySeedCount()));
        sender.sendMessage(new TextComponentString("Effective Primary Seed Density: " + ((double)stats.getPrimarySeedCount() / (double)stats.getChunksProcessed())));
        sender.sendMessage(new TextComponentString("Average Secondary Density: " + stats.avgSecondaryDensirt()));
    }
}
