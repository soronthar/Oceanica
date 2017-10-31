package test;

import mc.util.PerlinNoiseGen;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
/*
 * shamelessly based on https://gist.github.com/KdotJPG/b1270127455a94ac5d19#file-opensimplexnoisetest-java
 */
public class NoiseTestStuf {

        private static final int WIDTH = 512;
        private static final int HEIGHT = 512;
        private static final double FEATURE_SIZE = 16;

        public static void main(String[] args)
                throws IOException {

            PerlinNoiseGen noise = new PerlinNoiseGen(1);
            imageGeneration(noise);

        }

    private static void imageGeneration(PerlinNoiseGen noise) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                double value = noise.getValue(x / FEATURE_SIZE,0.0,y / FEATURE_SIZE);
                if (value < -0.7) {
                    image.setRGB(x,y, Color.GREEN.darker().getRGB());
                    continue;
                } else if (value<0.2) {
                    image.setRGB(x,y,Color.black.getRGB());
                    continue;
                }
//                    if (value < -0.3) {
//
//                    } else if (value<0.2) {
//                        image.setRGB(x,y,Color.black.getRGB());
//                        continue;
//                    }

//                     int rgb = 0x010101 * (255-((int)((value + 1) * 127.5)));
                int rgb = 0x010101 * (int)((value + 1) * 127.5);
//                    int rgb = 0x010101 * (int)((value) * 127.5);
//                    int rgb = 0x010101 * (255-((int)((value) * 127.5)));


                image.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(image, "png", new File("noise.png"));
    }
}
