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
}
