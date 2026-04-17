package fi.jyu.ohj2.inkilaaa.Muistikortit.Controller;

import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Kortti;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakka;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakkakokoelma;
import fi.jyu.ohj2.inkilaaa.Muistikortit.util.DialogiApu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PakanMuokkausController implements Initializable {

    @FXML private TableView<Kortti> korttiTable;
    @FXML private TextField kysymysField;
    @FXML private TextField vastausField;
    @FXML private TextField pakanNimiField;
    @FXML private TextField pakanKuvausField;

    private Pakka valittuPakka;
    private Pakkakokoelma kokoelma;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<Kortti, String> kysymisSarake = new TableColumn<>("Kysymys");
        kysymisSarake.setCellValueFactory(new PropertyValueFactory<>("kysymys"));

        TableColumn<Kortti, String> vastausSarake = new TableColumn<>("Vastaus");
        vastausSarake.setCellValueFactory(new PropertyValueFactory<>("vastaus"));

        korttiTable.getColumns().addAll(kysymisSarake, vastausSarake);
    }

    public void setValittuPakka(Pakka pakka) {
        this.valittuPakka = pakka;
        pakanNimiField.setText(pakka.getNimi());
        pakanKuvausField.setText(pakka.getKuvaus() != null ? pakka.getKuvaus() : "");
        korttiTable.setItems(pakka.getKortit());
    }

    public void setKokoelma(Pakkakokoelma k) {
        this.kokoelma = k;
    }

    @FXML
    private void handleTallennaPakanTiedot() {
        String nimi = pakanNimiField.getText().trim();
        String virhe = kokoelma.tarkistaNimi(nimi, valittuPakka);
        if (virhe != null) { DialogiApu.naytaVirhe(virhe); return; }
        valittuPakka.setNimi(nimi);
        valittuPakka.setKuvaus(pakanKuvausField.getText().trim());
        kokoelma.tallenna();
    }

    @FXML
    private void handleTallennaKortti() {
        String kysymys = kysymysField.getText().trim();
        String vastaus = vastausField.getText().trim();
        if (kysymys.isEmpty() || vastaus.isEmpty()) {
            DialogiApu.naytaVirhe("Täytä sekä kysymys että vastaus.");
            return;
        }
        valittuPakka.lisaaKortti(new Kortti(kysymys, vastaus));
        kokoelma.tallenna();
        kysymysField.clear();
        vastausField.clear();
        kysymysField.requestFocus();
    }

    @FXML
    private void handlePoistaKortti() {
        Kortti valittu = korttiTable.getSelectionModel().getSelectedItem();
        if (valittu != null && DialogiApu.vahvistaPoisto(valittu.getKysymys())) {
            valittuPakka.getKortit().remove(valittu);
            kokoelma.tallenna();
        }
    }

    @FXML
    private void handleTakaisin() {
        kokoelma.tallenna();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/main.fxml"));
            Stage stage = (Stage) korttiTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            System.err.println("Takaisin epäonnistui: " + e.getMessage());
        }
    }
}
