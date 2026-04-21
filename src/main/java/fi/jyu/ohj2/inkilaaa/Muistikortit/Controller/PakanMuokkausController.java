package fi.jyu.ohj2.inkilaaa.Muistikortit.Controller;

import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Kortti;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakka;
import fi.jyu.ohj2.inkilaaa.Muistikortit.model.Pakkakokoelma;
import fi.jyu.ohj2.inkilaaa.Muistikortit.util.DialogiApu;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pakan muokkaus -kontrolleri.
 * Mahdollistaa nimen, kuvauksen ja korttien muokkauksen.
 */
public class PakanMuokkausController implements Initializable {
    /**
     * UI-komponentit:
     * - korttien taulukko
     * - uuden kortin kysymys ja vastaus
     * - pakan nimi ja kuvaus
     */
    @FXML private TableView<Kortti> korttiTable;
    @FXML private TextField kysymysField;
    @FXML private TextField vastausField;
    @FXML private TextField pakanNimiField;
    @FXML private TextField pakanKuvausField;
    @FXML private Button tallennaButton;

    /** Näkymässä muokattavana oleva pakka. */
    private Pakka valittuPakka;

    /** Muokkaustilassa oleva kortti, tai null jos lisätään uusi. */
    private Kortti muokattavaKortti = null;

    /** Koko kokoelma — tarvitaan nimen­tarkistukseen ja tallennukseen. */
    private Pakkakokoelma kokoelma;

    /**
     * Alustaa korttitaulukon sarakkeet FXML-latauksen jälkeen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<Kortti, String> kysymisSarake = new TableColumn<>("Kysymys");
        kysymisSarake.setCellValueFactory(new PropertyValueFactory<>("kysymys"));

        TableColumn<Kortti, String> vastausSarake = new TableColumn<>("Vastaus");
        vastausSarake.setCellValueFactory(new PropertyValueFactory<>("vastaus"));

        korttiTable.getColumns().addAll(kysymisSarake, vastausSarake);
    }

    /**
     * Asettaa muokattavana olevan pakan ja sitoo sen taulukkoon.
     * Kutsutaan MainControllerista, kun muokkausnäkymä avataan.
     *
     * @param pakka muokattava pakka
     */
    public void setValittuPakka(Pakka pakka) {
        this.valittuPakka = pakka;
        pakanNimiField.setText(pakka.getNimi());
        pakanKuvausField.setText(pakka.getKuvaus() != null ? pakka.getKuvaus() : "");
        korttiTable.setItems(pakka.getKortit());
    }

    /**
     * Asettaa koko kokoelman viittauksen. Tarvitaan, jotta nimen
     * duplikaattitarkistus onnistuu ja tallennus voidaan käynnistää.
     *
     * @param k sovelluksen pakkakokoelma
     */
    public void setKokoelma(Pakkakokoelma k) {
        this.kokoelma = k;
    }

    /**
     * Tallentaa pakan päivitetyn nimen ja kuvauksen.
     * Tarkistaa nimen kelvollisuuden ennen tallennusta.
     */
    @FXML
    private void handleTallennaPakanTiedot() {
        String nimi = pakanNimiField.getText().trim();
        String virhe = kokoelma.tarkistaNimi(nimi, valittuPakka);
        if (virhe != null) { DialogiApu.naytaVirhe(virhe); return; }
        valittuPakka.setNimi(nimi);
        valittuPakka.setKuvaus(pakanKuvausField.getText().trim());
        kokoelma.tallenna();
    }

    /**
     * Lisää uuden kortin pakkaan tekstikenttien sisällön perusteella.
     * Kysymys ja vastaus ovat kumpikin pakollisia. Lisäyksen jälkeen
     * kentät tyhjennetään ja fokus palaa kysymyskenttään.
     */
    @FXML
    private void handleTallennaKortti() {
        String kysymys = kysymysField.getText().trim();
        String vastaus = vastausField.getText().trim();
        boolean kysymysTyhja = kysymys.isEmpty();
        boolean vastausTyhja = vastaus.isEmpty();
        kysymysField.setStyle(kysymysTyhja ? "-fx-border-color: red; -fx-border-width: 2;" : "");
        vastausField.setStyle(vastausTyhja ? "-fx-border-color: red; -fx-border-width: 2;" : "");
        if (kysymysTyhja || vastausTyhja) return;

        if (muokattavaKortti != null) {
            muokattavaKortti.setKysymys(kysymys);
            muokattavaKortti.setVastaus(vastaus);
            korttiTable.refresh();
            muokattavaKortti = null;
            tallennaButton.setText("Lisää kortti pakkaan");
        } else {
            valittuPakka.lisaaKortti(new Kortti(kysymys, vastaus));
        }
        kokoelma.tallenna();
        kysymysField.clear();
        vastausField.clear();
        kysymysField.requestFocus();
    }

    @FXML
    private void handleMuokkaaKortti() {
        Kortti valittu = korttiTable.getSelectionModel().getSelectedItem();
        if (valittu == null) { DialogiApu.naytaVirhe("Valitse ensin muokattava kortti."); return; }
        muokattavaKortti = valittu;
        kysymysField.setText(valittu.getKysymys());
        vastausField.setText(valittu.getVastaus());
        kysymysField.setStyle("");
        vastausField.setStyle("");
        tallennaButton.setText("Tallenna muutokset");
        kysymysField.requestFocus();
    }

    /**
     * Poistaa valitun kortin pakasta vahvistuksen jälkeen.
     */
    @FXML
    private void handlePoistaKortti() {
        Kortti valittu = korttiTable.getSelectionModel().getSelectedItem();
        if (valittu != null && DialogiApu.vahvistaPoisto(valittu.getKysymys())) {
            valittuPakka.getKortit().remove(valittu);
            kokoelma.tallenna();
        }
    }

    /**
     * Palaa päänäkymään. Ennen vaihtoa varmistetaan tallennus,
     * jotta mahdolliset viimeiset muutokset menevät levyä kohti.
     */
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
