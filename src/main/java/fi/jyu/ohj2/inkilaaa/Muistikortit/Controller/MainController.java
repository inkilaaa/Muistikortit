package fi.jyu.ohj2.inkilaaa.Muistikortit.Controller;

import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakka;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakkakokoelma;
import fi.jyu.ohj2.inkilaaa.Muistikortit.util.DialogiApu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

    /** Taulukko. Näkyvät kaikki pakat. Sidotaan fxml:ään fx:id:llä. */
    @FXML private TableView<Pakka> pakkaTable;

    /** Sovelluksen pakkakokoelma, joka ladataan ja tallennetaan levyltä. */
    private final Pakkakokoelma kokoelma = new Pakkakokoelma();

    /**
     * Alustus, jonka JavaFX kutsuu automaattisesti fxml:n latauksen jälkeen.
     * Rakentaa taulukon sarakkeet, lataa pakat tiedostosta ja asettaa
     * tuplaklikkaus-kuuntelijan, joka avaa pakan muokkausnäkymän.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**Sarake: pakan nimi*/
        TableColumn<Pakka, String> nimiSarake = new TableColumn<>("Pakan nimi");
        nimiSarake.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        /**Sarake: pakan kuvaus*/
        TableColumn<Pakka, String> kuvausSarake = new TableColumn<>("Kuvaus");
        kuvausSarake.setCellValueFactory(new PropertyValueFactory<>("kuvaus"));

        pakkaTable.getColumns().addAll(nimiSarake, kuvausSarake);
        /**Sarake: Ladataan kokoelma tiedostosta ja kytketään taulukkoon*/
        kokoelma.lataa();
        pakkaTable.setItems(kokoelma.getPakat());


        /**Tuplaklikkaus rivillä avaa muokkausnäkymän suoraan*/
        pakkaTable.setRowFactory(_ -> {
            TableRow<Pakka> rivi = new TableRow<>();
            rivi.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !rivi.isEmpty())
                    avaaMuokkaus(rivi.getItem());
            });
            return rivi;
        });
    }

    /**
     * Käsittelee "Uusi pakka" -painikkeen painalluksen.
     * Avaa dialogin, johon käyttäjä syöttää pakan nimen ja kuvauksen,
     * tarkistaa nimen ja lisää pakan kokoelmaan.
     */
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

    /**
     * Käsittelee "Harjoittele" -painikkeen.
     * Tarkistaa, että pakka on valittu ja siinä on kortteja, ja
     * avaa harjoittelunäkymän (Kysymys.fxml).
     */
    @FXML
    private void handleHarjoittele() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu == null) return;
        if (valittu.getKortit().isEmpty()) {
            DialogiApu.naytaVirhe("Pakka on tyhjä, lisää kortteja ennen harjoittelua.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/Kysymys.fxml"));
            Stage stage = (Stage) pakkaTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            // Annetaan harjoittelukontrollerille tiedot valitusta pakasta
            KysymysController ctrl = loader.getController();
            ctrl.setPakka(valittu);
        } catch (Exception e) {
            DialogiApu.naytaVirhe("Näkymän avaus epäonnistui.");
        }
    }

    /**
     * Käsittelee "Muokkaa" -painikkeen. Avaa valitun pakan muokkausnäkymän.
     * Jos pakkaa ei ole valittu, näyttää virheilmoituksen.
     */
    @FXML
    private void handleMuokkaa() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu != null) avaaMuokkaus(valittu);
        else DialogiApu.naytaVirhe("Valitse ensin muokattava pakka.");
    }

    /**
     * Käsittelee "Poista" -painikkeen. Pyytää vahvistuksen ja
     * poistaa valitun pakan kokoelmasta. Poistaminen laukaisee
     * automaattisen tallennuksen.
     */
    @FXML
    private void handlePoista() {
        Pakka valittu = pakkaTable.getSelectionModel().getSelectedItem();
        if (valittu == null) { DialogiApu.naytaVirhe("Valinta puuttuu."); return; }
        if (DialogiApu.vahvistaPoisto(valittu.getNimi()))
            kokoelma.getPakat().remove(valittu);
    }

    /**
     * Avaa pakan muokkausnäkymän (PakanMuokkaus.fxml) annetulle pakalle.
     * Käytetään sekä "Muokkaa" -napista että tuplaklikkauksesta.
     *
     * @param pakka muokattavaksi avattava pakka
     */
    private void avaaMuokkaus(Pakka pakka) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/jyu/ohj2/inkilaaa/Muistikortit/PakanMuokkaus.fxml"));
            Stage stage = (Stage) pakkaTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            // Välitetään muokkauskontrollerille valittu pakka ja kokoelma
            PakanMuokkausController ctrl = loader.getController();
            ctrl.setValittuPakka(pakka);
            ctrl.setKokoelma(kokoelma);
        } catch (Exception e) {
            DialogiApu.naytaVirhe("Näkymän avaus epäonnistui.");
        }
    }
}
