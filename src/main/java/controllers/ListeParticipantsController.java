package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Evenement;
import model.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ListeParticipantsController {

    @FXML private Label titreEvenement;
    @FXML private Label detailsEvenement;
    @FXML private Label capaciteTotale;
    @FXML private Label nombreInscrits;
    @FXML private Label placesRestantes;
    @FXML private TableView<ParticipantAvecNumero> tableauParticipants;
    @FXML private Label messageVide;

    private Evenement evenement;
    private ObservableList<ParticipantAvecNumero> donneesParticipants = FXCollections.observableArrayList();

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        initialiserInterface();
        chargerParticipants();
    }

    private void initialiserInterface() {
        if (evenement != null) {
            // Titre et détails
            titreEvenement.setText("👥 Participants - " + evenement.getNom());

            String details = String.format("📅 %s | 📍 %s | %s",
                    evenement.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")),
                    evenement.getLieu(),
                    evenement.getDetails()
            );
            detailsEvenement.setText(details);

            // Statistiques
            int capacite = evenement.getCapaciteMax();
            int inscrits = evenement.getParticipants().size();
            int restantes = capacite - inscrits;

            capaciteTotale.setText(String.valueOf(capacite));
            nombreInscrits.setText(String.valueOf(inscrits));
            placesRestantes.setText(String.valueOf(restantes));

            // Couleur pour les places restantes
            if (restantes == 0) {
                placesRestantes.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            } else if (restantes <= 5) {
                placesRestantes.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
            } else {
                placesRestantes.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            }
        }
    }

    private void chargerParticipants() {
        donneesParticipants.clear();

        if (evenement != null && !evenement.getParticipants().isEmpty()) {
            int numero = 1;
            for (Participant participant : evenement.getParticipants()) {
                donneesParticipants.add(new ParticipantAvecNumero(numero++, participant));
            }
            tableauParticipants.setItems(donneesParticipants);
            tableauParticipants.setVisible(true);
            messageVide.setVisible(false);
        } else {
            tableauParticipants.setVisible(false);
            messageVide.setVisible(true);
        }
    }

    @FXML
    private void exporterListe() {
        if (evenement == null || evenement.getParticipants().isEmpty()) {
            showAlert("Aucune donnée", "Il n'y a aucun participant à exporter.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter la liste des participants");
        fileChooser.setInitialFileName("participants_" + evenement.getNom().replaceAll("[^a-zA-Z0-9]", "_") + ".csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );

        Stage stage = (Stage) tableauParticipants.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // En-tête CSV
                writer.write("N°,Nom,Email,ID Participant\n");

                // Données
                int numero = 1;
                for (Participant participant : evenement.getParticipants()) {
                    writer.write(String.format("%d,\"%s\",\"%s\",\"%s\"\n",
                            numero++,
                            participant.getNom(),
                            participant.getEmail(),
                            participant.getId()
                    ));
                }

                showAlert("Export réussi",
                        "La liste des participants a été exportée avec succès dans :\n" + file.getAbsolutePath(),
                        Alert.AlertType.INFORMATION);

            } catch (IOException e) {
                showAlert("Erreur d'export",
                        "Une erreur est survenue lors de l'export : " + e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void envoyerNotification() {
        if (evenement == null || evenement.getParticipants().isEmpty()) {
            showAlert("Aucun destinataire", "Il n'y a aucun participant à qui envoyer une notification.", Alert.AlertType.WARNING);
            return;
        }

        // Dialogue pour saisir le message
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Envoyer une notification");
        dialog.setHeaderText("Notification aux participants de : " + evenement.getNom());
        dialog.setContentText("Message à envoyer :");
        dialog.getEditor().setPrefColumnCount(50);

        dialog.showAndWait().ifPresent(message -> {
            if (!message.trim().isEmpty()) {
                // Simuler l'envoi de notifications
                int compteur = 0;
                for (Participant participant : evenement.getParticipants()) {
                    // Ici, vous pourriez implémenter l'envoi réel d'emails
                    System.out.println("📧 Message envoyé à " + participant.getEmail() + " : " + message);
                    compteur++;
                }

                showAlert("Notifications envoyées",
                        String.format("✅ %d notification(s) ont été envoyées avec succès !", compteur),
                        Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void fermer() {
        Stage stage = (Stage) tableauParticipants.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Classe interne pour ajouter un numéro aux participants dans le tableau
    public static class ParticipantAvecNumero {
        private final int numero;
        private final Participant participant;

        public ParticipantAvecNumero(int numero, Participant participant) {
            this.numero = numero;
            this.participant = participant;
        }

        public int getNumero() {
            return numero;
        }

        public String getNom() {
            return participant.getNom();
        }

        public String getEmail() {
            return participant.getEmail();
        }

        public String getId() {
            return participant.getId();
        }
    }
}