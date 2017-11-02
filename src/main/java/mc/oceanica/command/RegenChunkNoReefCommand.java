package mc.oceanica.command;

import mc.oceanica.Oceanica;
import mc.oceanica.OceanicaConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.ArrayList;
import java.util.List;

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
