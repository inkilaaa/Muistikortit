package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

/**
 * Yksittäinen muistikortti, jossa on kysymys ja siihen kuuluva vastaus.
 * Luokka on yksinkertainen POJO (Plain Old Java Object), jota käytetään
 * kortin tietojen säilyttämiseen. Jackson-kirjasto (JSON-serialisointi)
 * tarvitsee parametrittoman konstruktorin ja setterit/gettterit.
 */
public class Kortti {
    /** Kortin kysymyspuoli. */
    private String kysymys;

    /** Kortin vastauspuoli. */
    private String vastaus;

    public Kortti() {}

    /**
     * Luo uuden kortin annetulla kysymyksellä ja vastauksella.
     * @param kysymys kortin kysymysteksti
     * @param vastaus kortin vastausteksti
     */
    public Kortti(String kysymys, String vastaus) {
        this.kysymys = kysymys;
        this.vastaus = vastaus;
    }

    /** @return kortin kysymysteksti */
    public String getKysymys() { return kysymys; }

    /** Asettaa kortin kysymyksen. @param kysymys uusi kysymysteksti */
    public void setKysymys(String kysymys) { this.kysymys = kysymys; }

    /** @return kortin vastausteksti */
    public String getVastaus() { return vastaus; }

    /** Asettaa kortin vastauksen. @param vastaus uusi vastausteksti */
    public void setVastaus(String vastaus) { this.vastaus = vastaus; }
}
