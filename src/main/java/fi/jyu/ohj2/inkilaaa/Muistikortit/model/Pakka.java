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
    /** Pakan nimi */
    private String nimi;

    /** Vapaamuotoinen kuvaus pakan sisällöstä. */
    private String kuvaus;

    /** Pakan kortit*/
    private final ObservableList<Kortti> kortit = FXCollections.observableArrayList();

    /** Parametriton konstruktori Jacksonin JSON-latausta varten. */
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

    /** @return pakan nimi */
    public String getNimi() { return nimi; }

    /** Asettaa pakan nimen*/
    public void setNimi(String nimi) { this.nimi = nimi; }

    /** palauttaaa akan kuvaus */
    public String getKuvaus() { return kuvaus; }

    /** Asettaa pakan kuvauksen*/
    public void setKuvaus(String kuvaus) { this.kuvaus = kuvaus; }

    /** pakassa olevat kortit tarkkailtavana listana */
    public ObservableList<Kortti> getKortit() { return kortit; }

    /**
     * Korvaa pakan kaikki kortit annetulla listalla.
     * Jacksonin setter: käytetään JSON:ista luettaessa.
     * Jos parametri on null, nykyisiä kortteja ei muuteta.
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
