package test;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.NoiseGeneratorImproved;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by H440 on 23/09/2017.
 */
public class NoiseTestStuf {


    public static void main(String[] args) {
        Random random = new Random(15633225);
//        NoiseGeneratorImproved ngi=new NoiseGeneratorImproved(random);
//        NoiseGeneratorOctaves ngo = new NoiseGeneratorOctaves(random,5);
        NoiseGeneratorPerlin ngp = new NoiseGeneratorPerlin(random,1);

//        maxMin(ngp);
        doChunkDensitySimulation(ngp);
//        doChunkGroupSimulation(ngp);

    }

    private static void maxMin(NoiseGeneratorPerlin ngp) {
        int xSize = 10000;
        int zSize = 10000;
        int primaryScale=1;


        double min=0;
        double max=0;
        for(int x=0;x<xSize;x++) {
            for (int z=0;z<zSize;z++) {
                double value = ngp.getValue(x , z);
                min=Math.min(min,value);
                max=Math.max(max,value);
            }
        }
        System.out.println("min = " + min);
        System.out.println("max = " + max);
    }

    private static void doChunk(NoiseGeneratorPerlin ngp) {
        int xSize = 16;
        int zSize = 16;
        double density=0;

        for(int x=0;x<xSize;x++) {
            for (int z=0;z<zSize;z++) {
                double value = ngp.getValue(x , z );
                String s = Math.abs(value) < density? "X ": "o ";
                System.out.print(s);
            }
            System.out.println();
        }
    }

    private static void doChunkGroupSimulation(NoiseGeneratorPerlin ngp) {
        int xSize = 8;
        int zSize = 8;
        double seedDensity=0.012;
        double secondarySeedDensity=0.15;
        double secondaryDensity=0.2;

        for(int x=0;x<xSize;x++) {
            for (int z=0;z<zSize;z++) {
                String s=".";
                double value = ngp.getValue(x , z );
                if (Math.abs(value) < seedDensity) {
                    s="P";
                } else if (Math.abs(value) < secondarySeedDensity) {
                    s="S";
                } else if (Math.abs(value) < secondaryDensity) {
                    s="s";
                }
                System.out.print(s);
            }
            System.out.println();
        }
    }

    private static void doChunkDensitySimulation(NoiseGeneratorPerlin ngp) {
        int xSize = 16;
        int zSize = 16;
        int totalCount = xSize * zSize;

        for(double density=0;density<=1;density+=.05) {
            int count=0;
            for(int x=0;x<xSize;x++) {
                for (int z=0;z<zSize;z++) {
                    double value = ngp.getValue(x , z );
                    if (Math.abs(value) < density) count++;
                }
            }
            System.out.printf("Density %f count %d/%d (%d%%)\n",density,count, totalCount, Math.round(count*100/totalCount));
        }

    }
}
