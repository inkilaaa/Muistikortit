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

    @Test
    void useampiKorttiLisataan() {
        Pakka pakka = new Pakka("Matematiikka", "Laskut");
        pakka.lisaaKortti(new Kortti("2+2?", "4"));
        pakka.lisaaKortti(new Kortti("3*3?", "9"));
        pakka.lisaaKortti(new Kortti("10/2?", "5"));
        assertEquals(3, pakka.getKortit().size());
    }

    @Test
    void kortinSisaltoTallentuu() {
        Pakka pakka = new Pakka("Testi", "Kuvaus");
        pakka.lisaaKortti(new Kortti("Kysymys?", "Vastaus"));
        Kortti kortti = pakka.getKortit().get(0);
        assertEquals("Kysymys?", kortti.getKysymys());
        assertEquals("Vastaus", kortti.getVastaus());
    }
}
