package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Kokoelma muistikorttipakkoja — sovelluksen päämalli (MVC:n M-osa).
 * Vastaa pakkojen säilyttämisestä, lataamisesta ja tallentamisesta
 * JSON-tiedostoon. Tarkistaa myös pakan nimen kelvollisuuden.
 *
 * Lista käyttää ObservableList-tyyppiä ja siihen on kytketty
 * ListChangeListener, joka kutsuu tallenna()-metodia aina
 * kun lista muuttuu (pakka lisätään tai poistetaan).
 */
public class Pakkakokoelma {
    /** Kokoelman pakat tarkkailtavassa listassa. */
    private final ObservableList<Pakka> pakat = FXCollections.observableArrayList();

    /** JSON-tiedoston polku, johon kokoelma tallennetaan. */
    private final Path tiedostoPolku;

    /** Jackson-kirjaston ObjectMapper serialisointia/deserialisointia varten. */
    private final ObjectMapper mapper = new ObjectMapper();

    /** Oletuskonstruktori, joka käyttää projektin juuren muistikortit.json-tiedostoa. */
    public Pakkakokoelma() {
        this(Path.of("muistikortit.json"));
    }

    /**
     * Luo kokoelman, joka tallentuu annettuun polkuun.
     * Käytetään myös yksikkötesteissä, jotta testit voivat kirjoittaa
     * väliaikaiseen hakemistoon oikean datatiedoston sijaan.
     *
     * @param polku tiedostopolku, johon JSON tallennetaan
     */
    public Pakkakokoelma(Path polku) {
        this.tiedostoPolku = polku;
        // Rekisteröidään listener, joka tallentaa kokoelman automaattisesti
        // aina kun pakkojen listaan tehdään muutoksia.
        pakat.addListener((ListChangeListener<Pakka>) _ -> tallenna());
    }

    /** @return kokoelman pakat tarkkailtavana listana */
    public ObservableList<Pakka> getPakat() { return pakat; }

    /**
     * Kirjoittaa kokoelman pakat JSON-tiedostoon.
     * Tämä kutsutaan automaattisesti, kun pakkojen määrä muuttuu,
     * mutta sitä voi kutsua myös suoraan esim. kortin tietojen
     * muokkaamisen jälkeen.
     */
    public void tallenna() {
        try {
            mapper.writeValue(tiedostoPolku.toFile(), new ArrayList<>(pakat));
        } catch (Exception e) {
            System.err.println("Tallennus epäonnistui: " + e.getMessage());
        }
    }

    /**
     * Lukee pakat JSON-tiedostosta kokoelmaan.
     * Jos tiedostoa ei ole vielä olemassa (esim. ensimmäinen käynnistys),
     * metodi ei tee mitään. Virhetilanteessa tulostaa virheilmoituksen.
     */
    public void lataa() {
        if (Files.notExists(tiedostoPolku)) return;
        try {
            List<Pakka> ladatut = mapper.readValue(tiedostoPolku.toFile(), new TypeReference<>() {});
            pakat.setAll(ladatut);
        } catch (Exception e) {
            System.err.println("Lataus epäonnistui: " + e.getMessage());
        }
    }

    /**
     * Tarkistaa, onko pakan nimi kelvollinen.
     * Säännöt:
     *  - nimi ei saa olla tyhjä (tai pelkkää välilyöntiä),
     *  - enintään 20 merkkiä,
     *  - ei duplikaatti (kirjainkoosta riippumaton vertailu).
     *
     * Parametri {@code nykyinen} kertoo, mitä pakkaa ollaan muokkaamassa,
     * jotta nimen voi säilyttää samana omaa pakkaa muokattaessa ilman
     * duplikaattivirhettä.
     *
     * @param nimi     tarkistettava nimi
     * @param nykyinen muokattava pakka (tai null jos luodaan uusi)
     * @return virheilmoitus merkkijonona, tai null jos nimi on ok
     */
    public String tarkistaNimi(String nimi, Pakka nykyinen) {
        String s = nimi != null ? nimi.trim() : "";
        if (s.isEmpty()) return "Pakalla täytyy olla nimi!";
        if (s.length() > 20) return "Pakan nimi on liian pitkä (max 20 merkkiä).";
        if (pakat.stream().anyMatch(p -> p != nykyinen && p.getNimi().equalsIgnoreCase(s)))
            return "Tämän niminen pakka on jo olemassa.";
        return null;
    }


}
