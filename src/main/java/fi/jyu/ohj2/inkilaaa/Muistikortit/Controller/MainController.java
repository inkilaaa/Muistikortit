package fi.jyu.ohj2.inkilaaa.Muistikortit.Controller;

import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakka;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakkakokoelma;
import fi.jyu.ohj2.inkilaaa.Muistikortit.util.DialogiApu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Päänäkymän kontrolleri (main.fxml).
 * Vastaa pakkojen listauksesta taulukossa
 */
public class MainController implements Initializable {

    /** Taulukko. Näkyvät kaikki pakat.*/
    @FXML
    private TableView<Pakka> pakkaTable;

    @FXML
    private Button uusiPakkaButton;

    @FXML
    private Button muokkaaButton;

    /** Sovelluksen pakkakokoelma, joka ladataan ja tallennetaan levyltä. */
    private final Pakkakokoelma kokoelma = new Pakkakokoelma();

    /**Rakentaa taulukon, lataa pakat ja avaa muokkasnäkymän*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<Pakka, String> nimiSarake = new TableColumn<>("Pakan nimi");
        nimiSarake.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        TableColumn<Pakka, String> kuvausSarake = new TableColumn<>("Kuvaus");
        kuvausSarake.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));

        pakkaTable.getColumns().addAll(nimiSarake, kuvausSarake);kokoelma.lataa();
        pakkaTable.setItems(kokoelma.getPakat());

        kokoelma.getPakat().addListener((javafx.collections.ListChangeListener<Pakka>) _ -> paivitaUusiPakkaStyle());
        paivitaUusiPakkaStyle();

        pakkaTable.getSelectionModel().selectedItemProperty().addListener((_, _, valittu) -> paivitaMuokkaaStyle(valittu));

        pakkaTable.setRowFactory(_ -> {
            TableRow<Pakka> rivi = new TableRow<>();
            rivi.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !rivi.isEmpty())
                    avaaMuokkaus(rivi.getItem());
            });
            return rivi;
        });
    }

    private void paivitaMuokkaaStyle(Pakka valittu) {
        if (valittu != null && valittu.getKortit().isEmpty()) {
            muokkaaButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            muokkaaButton.setStyle("");
        }
    }

    private void paivitaUusiPakkaStyle() {
        if (kokoelma.getPakat().isEmpty()) {
            uusiPakkaButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            uusiPakkaButton.setStyle("");
        }
    }

    /** Käsittelee uuden pakan, laittaa kuvaukset ja lisää paikantaulukkoon*/
    @FXML
    private void handleUusiPakka() {
        Optional<Pair<String, String>> tulos = DialogiApu.naytaUusiPakkaDialogi();
        tulos.ifPresent(pari -> {
            String nimi = pari.getKey().trim();
            String virhe = kokoelma.tarkistaNimi(nimi, null);
            if (virhe != null) { DialogiApu.naytaVirhe(virhe); return; }
            Pakka uusi = new Pakka(nimi, pari.getValue().trim());
            kokoelma.getPakat().add(uusi);
            pakkaTable.getSelectionModel().select(uusi);
        });
    }

    /**Harjoittelu painike, Tarkistaa pakan ja avaa*/
    @FXML
    private void handleHarjoittele() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu == null) {
            if (kokoelma.getPakat().isEmpty())
                DialogiApu.naytaVirhe("Luo uusi pakka ennen harjoittelua.");
            else
                DialogiApu.naytaVirhe("Valitse pakka ennen harjoittelua.");
            return;
        }
        if (valittu.getKortit().isEmpty()) {
            DialogiApu.naytaVirhe("Pakka on tyhjä, lisää kortteja ennen harjoittelua.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/Kysymys.fxml"));
            Stage stage = (Stage) pakkaTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            KysymysController ctrl = loader.getController();
            ctrl.setPakka(valittu);
        } catch (Exception e) {
            DialogiApu.naytaVirhe("Näkymän avaus epäonnistui.");
        }
    }

    /**Käsittelee Muokkaa painikkeen.*/
    @FXML
    private void handleMuokkaa() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu != null) avaaMuokkaus(valittu);
        else DialogiApu.naytaVirhe("Valitse ensin muokattava pakka.");
    }

    /** Käsittelee Poista painikkeen.*/
    @FXML
    private void handlePoista() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu == null) { DialogiApu.naytaVirhe("Valinta puuttuu."); return; }
        if (DialogiApu.vahvistaPoisto(valittu.getNimi()))
            kokoelma.getPakat().remove(valittu);
    }

    /**
     * Avaa PakanMuokkaus.fxml.
     *
     * @param pakka muokattavaksi avattava pakka
     */
    private void avaaMuokkaus(Pakka pakka) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/PakanMuokkaus.fxml"));
            Stage stage = (Stage) pakkaTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            PakanMuokkausController ctrl = loader.getController();
            ctrl.setValittuPakka(pakka);
            ctrl.setKokoelma(kokoelma);
        } catch (Exception e) {
            DialogiApu.naytaVirhe("Näkymän avaus epäonnistui.");
        }
    }
}
