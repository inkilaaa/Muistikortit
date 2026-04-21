package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KorttiTest {

    @Test
    void konstruktoriAsettaaKysymyksenJaVastauksen() {
        Kortti kortti = new Kortti("Suomen pääkaupunki?", "Helsinki");
        assertEquals("Suomen pääkaupunki?", kortti.getKysymys());
        assertEquals("Helsinki", kortti.getVastaus());
    }

    @Test
    void setKysymysPaivittaaArvon() {
        Kortti kortti = new Kortti("vanha", "vastaus");
        kortti.setKysymys("uusi kysymys");
        assertEquals("uusi kysymys", kortti.getKysymys());
    }

    @Test
    void setVastausPaivittaaArvon() {
        Kortti kortti = new Kortti("kysymys", "vanha");
        kortti.setVastaus("uusi vastaus");
        assertEquals("uusi vastaus", kortti.getVastaus());
    }
}
