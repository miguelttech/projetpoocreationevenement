package controllers;

import exceptions.EvenementDejaExistantException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.Concert;
import model.Conference;
import model.Evenement;
import service.GestionEvenements;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CreerEvenementController {
    @FXML private TextField nomField;
    @FXML private DatePicker datePicker;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private ComboBox<String> typeComboBox;

    // Champs dynamiques
    @FXML private VBox conferenceFields;
    @FXML private VBox concertFields;
    @FXML private TextField themeField;
    @FXML private TextField intervenantsField;
    @FXML private TextField artisteField;
    @FXML private TextField genreField;

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("Conférence", "Concert");

        typeComboBox.setOnAction(event -> {
            String selected = typeComboBox.getValue();
            conferenceFields.setVisible("Conférence".equals(selected));
            conferenceFields.setManaged("Conférence".equals(selected));
            concertFields.setVisible("Concert".equals(selected));
            concertFields.setManaged("Concert".equals(selected));
        });
    }

    @FXML
    private void validerFormulaire() {
        try {
            // Validation des champs
            if (nomField.getText().isEmpty() || datePicker.getValue() == null
                    || lieuField.getText().isEmpty() || capaciteField.getText().isEmpty()) {
                showAlert("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Création de l'événement
            String nom = nomField.getText();
            LocalDateTime date = datePicker.getValue().atStartOfDay();
            String lieu = lieuField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            String type = typeComboBox.getValue();

            Evenement event;
            if ("Conférence".equals(type)) {
                String theme = themeField.getText();
                if (theme.isEmpty()) {
                    showAlert("Veuillez saisir un thème pour la conférence.");
                    return;
                }
                event = new Conference(generateId(), nom, date, lieu, capacite, theme);
            } else {
                String artiste = artisteField.getText();
                String genre = genreField.getText();
                if (artiste.isEmpty() || genre.isEmpty()) {
                    showAlert("Veuillez saisir l'artiste et le genre musical.");
                    return;
                }
                event = new Concert(generateId(), nom, date, lieu, capacite, artiste, genre);
            }

            // Ajout à la gestion
            GestionEvenements.getInstance().ajouterEvenement(event);
            showAlert("Événement créé avec succès !");

            // Fermer la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("La capacité doit être un nombre valide.");
        } catch (EvenementDejaExistantException e) {
            showAlert("Erreur : " + e.getMessage());
        } catch (Exception e) {
            showAlert("Une erreur inattendue est survenue.");
            e.printStackTrace();
        }
    }

    private String generateId() {
        return "EV" + System.currentTimeMillis();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

