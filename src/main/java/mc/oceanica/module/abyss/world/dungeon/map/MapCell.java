package mc.oceanica.module.abyss.world.dungeon.map;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Created by H440 on 18/10/2017.
 */
public class MapCell {
    public int x;
    public int z;

    public MapCell(int x, int z) {
        this.x=x;
        this.z=z;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapCell mapCell = (MapCell) o;

        return x == mapCell.x && z == mapCell.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        return result;
    }
}
