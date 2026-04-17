package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

public class Kortti {
    private String kysymys;
    private String vastaus;

    public Kortti() {}

    public Kortti(String kysymys, String vastaus) {
        this.kysymys = kysymys;
        this.vastaus = vastaus;
    }

    public String getKysymys() { return kysymys; }
    public void setKysymys(String kysymys) { this.kysymys = kysymys; }
    public String getVastaus() { return vastaus; }
    public void setVastaus(String vastaus) { this.vastaus = vastaus; }
}
