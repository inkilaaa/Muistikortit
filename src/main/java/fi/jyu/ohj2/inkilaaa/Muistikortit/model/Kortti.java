package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

/**
 * Yksittäinen muistikortti, jossa on kysymys ja siihen kuuluva vastaus.
 */
public class Kortti {
    /** Kortin kysymyspuoli ja Kortin vastauspuoli. */
    private String kysymys;
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
