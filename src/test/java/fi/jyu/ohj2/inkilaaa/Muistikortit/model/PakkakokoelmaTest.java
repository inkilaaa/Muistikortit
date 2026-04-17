package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PakkakokoelmaTest {

    @Test
    void tarkistaNimiAntaaVirheenTyhjalleNimelle(@TempDir Path tempDir) {
        Pakkakokoelma kokoelma = new Pakkakokoelma(tempDir.resolve("testi.json"));
        String virhe = kokoelma.tarkistaNimi("", null);
        assertNotNull(virhe);
    }
}
