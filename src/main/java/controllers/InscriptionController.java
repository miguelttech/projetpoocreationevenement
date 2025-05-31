package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import model.Participant;
import service.GestionEvenements;
import model.Evenement;
import exceptions.CapaciteMaxAtteinteException;
import javafx.collections.FXCollections;

public class InscriptionController {
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Evenement> evenementCombo;

    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les événements disponibles
        evenementCombo.setItems(FXCollections.observableArrayList(
                GestionEvenements.getInstance().getEvenements().values()
        ));

        // Personnaliser l'affichage des événements dans la ComboBox
        evenementCombo.setCellFactory(param -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " - " + item.getLieu() + " (" +
                            item.getParticipants().size() + "/" + item.getCapaciteMax() + ")");
                }
            }
        });

        // Personnaliser l'affichage de l'élément sélectionné
        evenementCombo.setButtonCell(new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Sélectionner un événement");
                } else {
                    setText(item.getNom() + " - " + item.getLieu());
                }
            }
        });
    }

    @FXML
    private void validerInscription() {
        try {
            // Validation des champs
            if (nomField.getText().trim().isEmpty()) {
                showAlert("Erreur", "Le nom est obligatoire !");
                nomField.requestFocus();
                return;
            }

            if (emailField.getText().trim().isEmpty()) {
                showAlert("Erreur", "L'email est obligatoire !");
                emailField.requestFocus();
                return;
            }

            if (!isValidEmail(emailField.getText().trim())) {
                showAlert("Erreur", "Format d'email invalide !");
                emailField.requestFocus();
                return;
            }

            if (evenementCombo.getValue() == null) {
                showAlert("Erreur", "Veuillez sélectionner un événement !");
                evenementCombo.requestFocus();
                return;
            }

            // Création du participant
            Participant participant = new Participant(
                    "P" + System.currentTimeMillis(), // ID auto-généré
                    nomField.getText().trim(),
                    emailField.getText().trim()
            );

            // Inscription à l'événement
            Evenement evenement = evenementCombo.getValue();

            // Vérifier si le participant n'est pas déjà inscrit
            boolean dejaInscrit = evenement.getParticipants().stream()
                    .anyMatch(p -> p.getEmail().equalsIgnoreCase(participant.getEmail()));

            if (dejaInscrit) {
                showAlert("Erreur", "Vous êtes déjà inscrit à cet événement !");
                return;
            }

            evenement.ajouterParticipant(participant);

            // Confirmation
            showAlert("Succès",
                    "Inscription réussie !\n" +
                            "Événement : " + evenement.getNom() + "\n" +
                            "Participant : " + participant.getNom() + "\n" +
                            "Places restantes : " + (evenement.getCapaciteMax() - evenement.getParticipants().size())
            );

            // Réinitialiser le formulaire
            nomField.clear();
            emailField.clear();
            evenementCombo.getSelectionModel().clearSelection();

            // Rafraîchir la ComboBox pour mettre à jour le nombre de participants
            evenementCombo.setItems(FXCollections.observableArrayList(
                    GestionEvenements.getInstance().getEvenements().values()
            ));

        } catch (CapaciteMaxAtteinteException e) {
            showAlert("Erreur", e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void annuler() {
        // Fermer la fenêtre
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}