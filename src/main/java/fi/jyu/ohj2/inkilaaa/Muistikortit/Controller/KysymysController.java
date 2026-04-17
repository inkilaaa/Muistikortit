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

public class KysymysController {

    @FXML private Label kysymysLabel;
    @FXML private Label kuvausLabel;
    @FXML private Button seuraavaButton;
    @FXML private Button kaannaButton;
    @FXML private Button edellinenButton;
    @FXML private Button lopetaButton;

    private List<Kortti> kortit;
    private int indeksi = 0;

    public void setPakka(Pakka pakka) {
        this.kortit = new ArrayList<>(pakka.getKortit());
        Collections.shuffle(this.kortit);
        indeksi = 0;
        naytaKortti();
    }

    private void naytaKortti() {
        if (indeksi < kortit.size()) {
            Kortti k = kortit.get(indeksi);
            kysymysLabel.setText(k.getKysymys());
            kuvausLabel.setText(k.getVastaus());
            kuvausLabel.setVisible(false);
            kaannaButton.setText("Näytä vastaus");
            edellinenButton.setDisable(indeksi == 0);
            seuraavaButton.setDisable(indeksi == kortit.size() - 1);
        } else {
            DialogiApu.naytaIlmoitus("Harjoitus päättyi! Kävit läpi " + kortit.size() + " korttia.");
            palaaPaavalikkoon();
        }
    }

    @FXML
    private void handleSeuraavaKortti() {
        indeksi++;
        naytaKortti();
    }

    @FXML
    private void handleEdellinenKortti() {
        if (indeksi > 0) { indeksi--; naytaKortti(); }
    }

    @FXML
    private void handleKaannaKortti() {
        boolean nayta = !kuvausLabel.isVisible();
        kuvausLabel.setVisible(nayta);
        kaannaButton.setText(nayta ? "Piilota vastaus" : "Näytä vastaus");
    }

    @FXML
    private void handleLopeta() {
        palaaPaavalikkoon();
    }

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
