package mc.util;

import net.minecraft.util.math.Vec3i;

/**
 * Created by H440 on 22/10/2017.
 */
public class Utils {

    public static Vec3i addVec3i(Vec3i v, Vec3i u) {
        return new Vec3i(v.getX() + u.getX(), v.getY() + u.getY(), v.getZ() + u.getZ());
    }

}
