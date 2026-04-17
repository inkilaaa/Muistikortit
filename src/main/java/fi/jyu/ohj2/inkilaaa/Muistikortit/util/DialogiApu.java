package fi.jyu.ohj2.inkilaaa.Muistikortit.util;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Apuluokka JavaFX-dialogien näyttämiseen.
 * Keskittää dialogien luomisen yhteen paikkaan, jotta eri
 * kontrollerit voivat kutsua niitä lyhyesti ilman toistuvaa
 * koodia.
 */
public class DialogiApu {

    /**
     * Näyttää vahvistusdialogin ennen poistoa ja palauttaa käyttäjän valinnan.
     *
     * @param kohteenNimi poistettavan kohteen nimi (näytetään dialogissa)
     * @return true jos käyttäjä painoi OK, false muutoin
     */
    public static boolean vahvistaPoisto(String kohteenNimi) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setContentText("Haluatko varmasti poistaa: " + kohteenNimi + "?");
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    /**
     * Näyttää virheilmoituksen punaisessa dialogissa.
     *
     * @param viesti käyttäjälle näytettävä virheilmoitus
     */
    public static void naytaVirhe(String viesti) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(viesti);
        alert.showAndWait();
    }

    /**
     * Näyttää neutraalin info-dialogin.
     *
     * @param viesti käyttäjälle näytettävä ilmoitus
     */
    public static void naytaIlmoitus(String viesti) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(viesti);
        alert.showAndWait();
    }

    /**
     * Näyttää dialogin, jossa kysytään uuden pakan nimi ja kuvaus.
     * Palauttaa Optional-paketoituna parin (nimi, kuvaus) jos käyttäjä
     * painoi "Luo", tai tyhjän Optionalin jos dialogi peruutettiin.
     *
     * @return Optional, jossa Pair&lt;nimi, kuvaus&gt;, tai tyhjä jos peruutettu
     */
    public static Optional<Pair<String, String>> naytaUusiPakkaDialogi() {
        Dialog<Pair<String, String>> dialogi = new Dialog<>();
        dialogi.setTitle("Uusi pakka");

        // Lisätään omat "Luo" ja "Peruuta" -napit
        ButtonType luoNappi = new ButtonType("Luo", ButtonBar.ButtonData.OK_DONE);
        dialogi.getDialogPane().getButtonTypes().addAll(luoNappi, ButtonType.CANCEL);

        // Syötekentät pakan tiedoille
        TextField nimiKentta = new TextField();
        nimiKentta.setPromptText("Pakan nimi");
        TextField kuvausKentta = new TextField();
        kuvausKentta.setPromptText("Kuvaus");

        // Pystylaatikko, johon kentät ladotaan labeleiden kanssa
        VBox sisalto = new VBox(10);
        sisalto.getChildren().addAll(new Label("Nimi:"), nimiKentta, new Label("Kuvaus:"), kuvausKentta);
        dialogi.getDialogPane().setContent(sisalto);

        // Muunnetaan painikevalinta tulokseksi (pariksi) tai nulliksi
        dialogi.setResultConverter(nappi -> {
            if (nappi == luoNappi) return new Pair<>(nimiKentta.getText(), kuvausKentta.getText());
            return null;
        });
        return dialogi.showAndWait();
    }
}
