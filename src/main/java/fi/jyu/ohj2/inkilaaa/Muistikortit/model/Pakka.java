package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * Yksi muistikorttipakka, joka sisältää nimen, kuvauksen ja kokoelman kortteja.
 * Pakka kuuluu Pakkakokoelma-luokan alaisuuteen. Kortit tallennetaan
 * ObservableListiin, jotta JavaFX-käyttöliittymä päivittyy automaattisesti
 * kun kortteja lisätään tai poistetaan.
 */
public class Pakka {
    /** Pakan nimi, kuvaus ja kortit */
    private String nimi;
    private String kuvaus;
    private final ObservableList<Kortti> kortit = FXCollections.observableArrayList();

    /** Jacksonin JSON-latausta varten. */
    public Pakka() {}

    /**
     * Luo uuden pakan annetulla nimellä ja kuvauksella.
     * @param nimi   pakan nimi
     * @param kuvaus pakan kuvaus (voi olla tyhjä)
     */
    public Pakka(String nimi, String kuvaus) {
        this.nimi = nimi;
        this.kuvaus = kuvaus;
    }

    /** @return pakan nimi, asettaa pakan nimen, palauttaa kuvauksen ja pakan lista */
    public String getNimi() { return nimi; }
    public void setNimi(String nimi) { this.nimi = nimi; }
    public String getKuvaus() { return kuvaus; }
    public void setKuvaus(String kuvaus) { this.kuvaus = kuvaus; }
    public ObservableList<Kortti> getKortit() { return kortit; }

    /**
     * Korvaa pakan kaikki kortit annetulla listalla.
     * @param uudet uudet kortit
     */
    public void setKortit(List<Kortti> uudet) {
        if (uudet != null) this.kortit.setAll(uudet);
    }

    /**
     * Lisää yhden kortin pakan loppuun.
     * @param kortti lisättävä kortti
     */
    public void lisaaKortti(Kortti kortti) {
        this.kortit.add(kortti);
    }
}
