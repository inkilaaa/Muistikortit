package fi.jyu.ohj2.inkilaaa.Muistikortit.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class Pakka {
    private String nimi;
    private String kuvaus;
    private final ObservableList<Kortti> kortit = FXCollections.observableArrayList();

    public Pakka() {}

    public Pakka(String nimi, String kuvaus) {
        this.nimi = nimi;
        this.kuvaus = kuvaus;
    }

    public String getNimi() { return nimi; }
    public void setNimi(String nimi) { this.nimi = nimi; }
    public String getKuvaus() { return kuvaus; }
    public void setKuvaus(String kuvaus) { this.kuvaus = kuvaus; }
    public ObservableList<Kortti> getKortit() { return kortit; }

    public void setKortit(List<Kortti> uudet) {
        if (uudet != null) this.kortit.setAll(uudet);
    }

    public void lisaaKortti(Kortti kortti) {
        this.kortit.add(kortti);
    }
}
