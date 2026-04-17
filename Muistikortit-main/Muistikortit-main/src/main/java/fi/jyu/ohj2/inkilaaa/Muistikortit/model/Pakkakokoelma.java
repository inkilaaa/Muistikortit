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

public class Pakkakokoelma {
    private final ObservableList<Pakka> pakat = FXCollections.observableArrayList();
    private final Path tiedostoPolku;
    private final ObjectMapper mapper = new ObjectMapper();

    public Pakkakokoelma() {
        this(Path.of("muistikortit.json"));
    }

    public Pakkakokoelma(Path polku) {
        this.tiedostoPolku = polku;
        pakat.addListener((ListChangeListener<Pakka>) _ -> tallenna());
    }

    public ObservableList<Pakka> getPakat() { return pakat; }

    public void tallenna() {
        try {
            mapper.writeValue(tiedostoPolku.toFile(), new ArrayList<>(pakat));
        } catch (Exception e) {
            System.err.println("Tallennus epäonnistui: " + e.getMessage());
        }
    }

    public void lataa() {
        if (Files.notExists(tiedostoPolku)) return;
        try {
            List<Pakka> ladatut = mapper.readValue(tiedostoPolku.toFile(), new TypeReference<>() {});
            pakat.setAll(ladatut);
        } catch (Exception e) {
            System.err.println("Lataus epäonnistui: " + e.getMessage());
        }
    }

    public String tarkistaNimi(String nimi, Pakka nykyinen) {
        String s = nimi != null ? nimi.trim() : "";
        if (s.isEmpty()) return "Pakalla täytyy olla nimi!";
        if (s.length() > 20) return "Pakan nimi on liian pitkä (max 20 merkkiä).";
        if (pakat.stream().anyMatch(p -> p != nykyinen && p.getNimi().equalsIgnoreCase(s)))
            return "Tämän niminen pakka on jo olemassa.";
        return null;
    }


}
