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

/**
 * Pakan muokkausnäkymän kontrolleri (PakanMuokkaus.fxml).
 * Tässä näkymässä käyttäjä voi muokata pakan nimeä ja kuvausta
 * sekä lisätä ja poistaa yksittäisiä kortteja.
 */
public class PakanMuokkausController implements Initializable {

    /** Taulukko. Jossa näkyvät pakan kortit. */
    @FXML private TableView<Kortti> korttiTable;

    /** Tekstikenttä uuden kortin kysymystä varten. */
    @FXML private TextField kysymysField;

    /** Tekstikenttä uuden kortin vastausta varten. */
    @FXML private TextField vastausField;

    /** Tekstikenttä pakan nimen muokkausta varten. */
    @FXML private TextField pakanNimiField;

    /** Tekstikenttä pakan kuvauksen muokkausta varten. */
    @FXML private TextField pakanKuvausField;

    /** Näkymässä muokattavana oleva pakka. */
    private Pakka valittuPakka;

    /** Koko kokoelma — tarvitaan nimen­tarkistukseen ja tallennukseen. */
    private Pakkakokoelma kokoelma;

    /**
     * Alustaa korttitaulukon sarakkeet. JavaFX kutsuu automaattisesti
     * fxml:n latauksen jälkeen.
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
