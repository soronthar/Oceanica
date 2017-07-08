package mc.oceanica;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class OceanicaStats {
    public static final OceanicaStats INSTANCE=new OceanicaStats();

    private int chunksProcessed;
    private int primarySeedCount;
    private int secondaryCount;
    private double acumSecondaryDensity;

    public void addPrimarySeed() {
        this.primarySeedCount++;
    }

    public void addChunkProcessed() {
        this.chunksProcessed++;
    }

    public void addSecondaryCount() {
        this.secondaryCount++;
    }

    public void addSecondaryDensity(double density) {
        this.acumSecondaryDensity+=density;
    }

    public int getChunksProcessed() {
        return chunksProcessed;
    }

    public int getPrimarySeedCount() {
        return primarySeedCount;
    }

    public int getSecondaryCount() {
        return secondaryCount;
    }

    public double getAcumSecondaryDensity() {
        return acumSecondaryDensity;
    }

    public double avgSecondaryDensirt() {
        return acumSecondaryDensity/(double) secondaryCount;
    }

}
