package mc.oceanica;

public class OceanicaStats {
    public static final OceanicaStats INSTANCE = new OceanicaStats();

    private int chunksProcessed;
    private int primarySeedCount;
    private int secondaryCount;
    private int kelpCount;

    public void addPrimarySeed() {
        this.primarySeedCount++;
    }

    public void addChunkProcessed() {
        this.chunksProcessed++;
    }

    public void addSecondaryCount() {
        this.secondaryCount++;
    }

    public void addKelpCount() {
        this.kelpCount++;
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

    public int getKelpCount() {
        return kelpCount;
    }
}
