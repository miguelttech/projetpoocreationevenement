package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Participant;
import service.GestionEvenements;
import model.Evenement;

public class InscriptionController {
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Evenement> evenementCombo;

    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les événements disponibles
        evenementCombo.getItems().addAll(GestionEvenements.getInstance().getEvenements().values());
    }

    @FXML
    private void validerInscription() {
        try {
            // Validation des champs
            if (nomField.getText().isEmpty() || emailField.getText().isEmpty() || evenementCombo.getValue() == null) {
                showAlert("Erreur", "Tous les champs sont obligatoires !");
                return;
            }

            // Création du participant
            Participant participant = new Participant(
                    "P" + System.currentTimeMillis(), // ID auto-généré
                    nomField.getText(),
                    emailField.getText()
            );

            // Inscription à l'événement
            Evenement evenement = evenementCombo.getValue();
            evenement.ajouterParticipant(participant);

            // Confirmation
            showAlert("Succès", "Inscription réussie à l'événement : " + evenement.getNom());

            // Réinitialiser le formulaire
            nomField.clear();
            emailField.clear();
            evenementCombo.getSelectionModel().clearSelection();

        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}