package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Muistikorttipakkojen malli (MVC).
 * Hoitaa latauksen, tallennuksen ja validoinnin JSON-tiedostoon.
 * Tallentaa automaattisesti muutoksissa.
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
     * Luo kokoelman annetulle polulle.
     * Mahdollistaa myös testauksen erillisellä tiedostolla.
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
     * Tallentaa pakat JSON-tiedostoon.
     * Kutsutaan automaattisesti muutoksissa tai tarvittaessa manuaalisesti.
     */
    public void tallenna() {
        try {
            mapper.writeValue(tiedostoPolku.toFile(), new ArrayList<>(pakat));
        } catch (Exception e) {
            System.err.println("Tallennus epäonnistui: " + e.getMessage());
        }
    }

    /**
     * Lataa pakat JSON-tiedostosta.
     * Jos tiedostoa ei ole, ei tehdä mitään. Virheistä ilmoitetaan.
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
     * Tarkistaa pakan nimen kelpoisuuden.
     * Ei tyhjä, max 20 merkkiä, ei duplikaatti.
     * Nykyinen pakka sallii saman nimen muokkauksessa.
     *
     * @param nimi tarkistettava nimi
     * @param nykyinen muokattava pakka
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
