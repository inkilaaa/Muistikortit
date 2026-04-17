package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PakkaTest {

    @Test
    void lisaaKorttiKasvattaaKortistonKokoa() {
        Pakka pakka = new Pakka("Historia", "Suomen historia");
        pakka.lisaaKortti(new Kortti("Milloin Suomi itsenäistyi?", "1917"));
        assertEquals(1, pakka.getKortit().size());
    }
}
