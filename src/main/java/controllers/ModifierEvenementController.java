package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Concert;
import model.Conference;
import model.Evenement;
import service.GestionEvenements;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifierEvenementController {
    @FXML private TextField nomField;
    @FXML private DatePicker datePicker;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private ComboBox<String> typeComboBox;

    // Champs dynamiques
    @FXML private VBox conferenceFields;
    @FXML private VBox concertFields;
    @FXML private TextField themeField;
    @FXML private TextField artisteField;
    @FXML private TextField genreField;

    private Evenement evenementAModifier;

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

    public void setEvenement(Evenement evenement) {
        this.evenementAModifier = evenement;
        remplirFormulaire();
    }

    private void remplirFormulaire() {
        if (evenementAModifier != null) {
            nomField.setText(evenementAModifier.getNom());
            datePicker.setValue(evenementAModifier.getDate().toLocalDate());
            lieuField.setText(evenementAModifier.getLieu());
            capaciteField.setText(String.valueOf(evenementAModifier.getCapaciteMax()));

            if (evenementAModifier instanceof Conference) {
                typeComboBox.setValue("Conférence");
                themeField.setText(((Conference) evenementAModifier).getTheme());
                conferenceFields.setVisible(true);
                conferenceFields.setManaged(true);
                concertFields.setVisible(false);
                concertFields.setManaged(false);
            } else if (evenementAModifier instanceof Concert) {
                typeComboBox.setValue("Concert");
                artisteField.setText(((Concert) evenementAModifier).getArtiste());
                genreField.setText(((Concert) evenementAModifier).getGenreMusical());
                concertFields.setVisible(true);
                concertFields.setManaged(true);
                conferenceFields.setVisible(false);
                conferenceFields.setManaged(false);
            }
        }
    }

    @FXML
    private void validerModification() {
        try {
            // Validation des champs
            if (nomField.getText().isEmpty() || datePicker.getValue() == null
                    || lieuField.getText().isEmpty() || capaciteField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            int nouvelleCapacite = Integer.parseInt(capaciteField.getText());

            // Vérifier que la nouvelle capacité n'est pas inférieure au nombre de participants actuels
            if (nouvelleCapacite < evenementAModifier.getParticipants().size()) {
                showAlert("Erreur",
                        String.format("La capacité ne peut pas être inférieure au nombre de participants actuels (%d).",
                                evenementAModifier.getParticipants().size()));
                return;
            }

            // Mise à jour des données de base
            String nom = nomField.getText();
            LocalDateTime date = datePicker.getValue().atStartOfDay();
            String lieu = lieuField.getText();
            String type = typeComboBox.getValue();

            // Supprimer l'ancien événement de la gestion
            GestionEvenements.getInstance().getEvenements().remove(evenementAModifier.getId());

            // Créer le nouvel événement modifié
            Evenement eventModifie;
            if ("Conférence".equals(type)) {
                String theme = themeField.getText();
                if (theme.isEmpty()) {
                    showAlert("Erreur", "Veuillez saisir un thème pour la conférence.");
                    return;
                }
                eventModifie = new Conference(evenementAModifier.getId(), nom, date, lieu, nouvelleCapacite, theme);
            } else {
                String artiste = artisteField.getText();
                String genre = genreField.getText();
                if (artiste.isEmpty() || genre.isEmpty()) {
                    showAlert("Erreur", "Veuillez saisir l'artiste et le genre musical.");
                    return;
                }
                eventModifie = new Concert(evenementAModifier.getId(), nom, date, lieu, nouvelleCapacite, artiste, genre);
            }

            // Réajouter les participants existants
            for (var participant : evenementAModifier.getParticipants()) {
                try {
                    eventModifie.ajouterParticipant(participant);
                } catch (Exception e) {
                    // Normalement cela ne devrait pas arriver car on a vérifié la capacité
                    System.err.println("Erreur lors du réajout des participants : " + e.getMessage());
                }
            }

            // Remettre l'événement modifié dans la gestion
            GestionEvenements.getInstance().getEvenements().put(eventModifie.getId(), eventModifie);

            showAlert("Succès", "Événement modifié avec succès !");

            // Fermer la fenêtre
            ((Stage) nomField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        // Fermer la fenêtre
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Succès") ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}