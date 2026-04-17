package fi.jyu.ohj2.inkilaaa.Muistikortit.Controller;

import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Kortti;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakka;
import fi.jyu.ohj2.inkilaaa.Muistikortit.util.DialogiApu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Harjoittelunäkymän kontrolleri (Kysymys.fxml).
 * Käy läpi valitun pakan kortit satunnaisessa järjestyksessä ja
 * mahdollistaa kortin "kääntämisen" vastauksen näyttämiseksi.
 */
public class KysymysController {

    /** Label. Näytetään kortin kysymys. */
    @FXML private Label kysymysLabel;

    /** Label. Näytetään kortin vastaus (piilotetaan oletuksena). */
    @FXML private Label kuvausLabel;

    /** Painike. Siirrytään seuraavaan korttiin. */
    @FXML private Button seuraavaButton;

    /** Painike. Näytetään/piilotetaan vastaus. */
    @FXML private Button kaannaButton;

    /** Painike. Palataan edelliseen korttiin. */
    @FXML private Button edellinenButton;

    /** Painike. Harjoittelu lopetetaan kesken. */
    @FXML private Button lopetaButton;

    /** Sekoitettu lista käsiteltävistä korteista. */
    private List<Kortti> kortit;

    /** Tällä hetkellä näytettävän kortin indeksi listassa. */
    private int indeksi = 0;

    /**
     * Asettaa harjoiteltavan pakan. Kortit kopioidaan uuteen listaan
     * ja sekoitetaan satunnaiseen järjestykseen, jotta alkuperäinen
     * pakka säilyy muuttumattomana.
     *
     * @param pakka harjoiteltava pakka
     */
    public void setPakka(Pakka pakka) {
        this.kortit = new ArrayList<>(pakka.getKortit());
        Collections.shuffle(this.kortit);
        indeksi = 0;
        naytaKortti();
    }

    /**
     * Päivittää näkymän näyttämään nykyisen kortin tiedot.
     * Jos viimeinen kortti on käyty läpi, ilmoittaa harjoituksen
     * päättymisestä ja palaa päänäkymään.
     */
    private void naytaKortti() {
        if (indeksi < kortit.size()) {
            Kortti k = kortit.get(indeksi);
            kysymysLabel.setText(k.getKysymys());
            kuvausLabel.setText(k.getVastaus());
            kuvausLabel.setVisible(false); // vastaus piiloon aluksi
            kaannaButton.setText("Näytä vastaus");
            // Pois käytöstä, jos ensimmäinen/viimeinen kortti
            edellinenButton.setDisable(indeksi == 0);
            seuraavaButton.setDisable(indeksi == kortit.size() - 1);
        } else {
            DialogiApu.naytaIlmoitus("Harjoitus päättyi! Kävit läpi " + kortit.size() + " korttia.");
            palaaPaavalikkoon();
        }
    }

    /** Siirtyy seuraavaan korttiin. */
    @FXML
    private void handleSeuraavaKortti() {
        indeksi++;
        naytaKortti();
    }

    /** Siirtyy edelliseen korttiin, jos sellainen on. */
    @FXML
    private void handleEdellinenKortti() {
        if (indeksi > 0) { indeksi--; naytaKortti(); }
    }

    /** Näyttää tai piilottaa vastauksen (kortin kääntäminen). */
    @FXML
    private void handleKaannaKortti() {
        boolean nayta = !kuvausLabel.isVisible();
        kuvausLabel.setVisible(nayta);
        kaannaButton.setText(nayta ? "Piilota vastaus" : "Näytä vastaus");
    }

    /** Keskeyttää harjoittelun ja palaa päänäkymään. */
    @FXML
    private void handleLopeta() {
        palaaPaavalikkoon();
    }

    /**
     * Lataa päänäkymän (main.fxml) nykyisen ikkunan tilalle.
     * Apumetodi, jota käytetään sekä lopetus-napista että
     * automaattisesti harjoituksen päättyessä.
     */
    private void palaaPaavalikkoon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/main.fxml"));
            Stage stage = (Stage) kysymysLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            System.err.println("Takaisin epäonnistui: " + e.getMessage());
        }
    }
}
