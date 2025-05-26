package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import service.GestionEvenements;
import model.Evenement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {
    @FXML
    private TableView<Evenement> tableauEvenements;

    private ObservableList<Evenement> donneesEvenements = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableauEvenements.setItems(donneesEvenements);
        rafraichirListeEvenements();
    }

    public void rafraichirListeEvenements() {
        donneesEvenements.clear();
        donneesEvenements.addAll(GestionEvenements.getInstance().getEvenements().values());
    }

    @FXML
    public void ouvrirFormulaireCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreerEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Créer un événement");

            // Rafraîchir après fermeture
            stage.setOnHidden(e -> rafraichirListeEvenements());
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur lors de l'ouverture du formulaire");
            e.printStackTrace();
        }
    }

    private void showAlert(String s) {
    }

    @FXML
    public void ouvrirInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inscription.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("S'inscrire à un événement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}