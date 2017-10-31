package mc.structgen;


import org.testng.annotations.Test;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TestStructure {
    @Test()
    public void test() {
        InputStream partsFile = this.getClass().getResourceAsStream("/assets/oceanica/dungeon/parts.json");
//        assertNotNull(partsFile);
//        StructureRegistry registry=new StructureRegistry();
//
//        registry.load(partsFile);
//
//        assertEquals(registry.partsCount(),0);

    }
}
