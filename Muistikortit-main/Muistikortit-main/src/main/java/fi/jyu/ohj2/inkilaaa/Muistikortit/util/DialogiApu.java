package fi.jyu.ohj2.inkilaaa.Muistikortit.util;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Optional;

public class DialogiApu {

    public static boolean vahvistaPoisto(String kohteenNimi) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setContentText("Haluatko varmasti poistaa: " + kohteenNimi + "?");
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    public static void naytaVirhe(String viesti) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(viesti);
        alert.showAndWait();
    }

    public static void naytaIlmoitus(String viesti) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(viesti);
        alert.showAndWait();
    }

    public static Optional<Pair<String, String>> naytaUusiPakkaDialogi() {
        Dialog<Pair<String, String>> dialogi = new Dialog<>();
        dialogi.setTitle("Uusi pakka");

        ButtonType luoNappi = new ButtonType("Luo", ButtonBar.ButtonData.OK_DONE);
        dialogi.getDialogPane().getButtonTypes().addAll(luoNappi, ButtonType.CANCEL);

        TextField nimiKentta = new TextField();
        nimiKentta.setPromptText("Pakan nimi");
        TextField kuvausKentta = new TextField();
        kuvausKentta.setPromptText("Kuvaus");

        VBox sisalto = new VBox(10);
        sisalto.getChildren().addAll(new Label("Nimi:"), nimiKentta, new Label("Kuvaus:"), kuvausKentta);
        dialogi.getDialogPane().setContent(sisalto);

        dialogi.setResultConverter(nappi -> {
            if (nappi == luoNappi) return new Pair<>(nimiKentta.getText(), kuvausKentta.getText());
            return null;
        });
        return dialogi.showAndWait();
    }
}
