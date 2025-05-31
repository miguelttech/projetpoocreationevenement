package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import service.GestionEvenements;
import model.Evenement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

    @FXML
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
            stage.setTitle("➕ Créer un événement");
            stage.setResizable(false);

            // Rafraîchir après fermeture
            stage.setOnHidden(e -> rafraichirListeEvenements());
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire de création", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void ouvrirInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inscription.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("📝 S'inscrire à un événement");
            stage.setResizable(false);

            // Rafraîchir après fermeture pour mettre à jour le nombre de participants
            stage.setOnHidden(e -> rafraichirListeEvenements());
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'inscription", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void voirParticipants() {
        Evenement evenementSelectionne = tableauEvenements.getSelectionModel().getSelectedItem();

        if (evenementSelectionne == null) {
            showAlert("Sélection requise",
                    "Veuillez d'abord sélectionner un événement dans le tableau pour voir ses participants.",
                    AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListeParticipants.fxml"));
            Parent root = loader.load();

            // Passer l'événement sélectionné au contrôleur
            ListeParticipantsController controller = loader.getController();
            controller.setEvenement(evenementSelectionne);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("👥 Participants - " + evenementSelectionne.getNom());
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture de la liste des participants", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void supprimerEvenement() {
        Evenement evenementSelectionne = tableauEvenements.getSelectionModel().getSelectedItem();

        if (evenementSelectionne == null) {
            showAlert("Sélection requise",
                    "Veuillez d'abord sélectionner un événement dans le tableau pour le supprimer.",
                    AlertType.WARNING);
            return;
        }

        // Dialogue de confirmation
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmer la suppression");
        confirmation.setHeaderText("Supprimer l'événement");
        confirmation.setContentText(String.format(
                "Êtes-vous sûr de vouloir supprimer l'événement :\n\n" +
                        "📅 %s\n" +
                        "📍 %s\n" +
                        "👥 %d participant(s) inscrit(s)\n\n" +
                        "⚠️ Cette action est irréversible !",
                evenementSelectionne.getNom(),
                evenementSelectionne.getLieu(),
                evenementSelectionne.getParticipants().size()
        ));

        // Personnaliser les boutons
        ButtonType buttonTypeOui = new ButtonType("🗑️ Oui, supprimer");
        ButtonType buttonTypeNon = new ButtonType("❌ Annuler");
        confirmation.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeOui) {
            try {
                // Supprimer l'événement
               // GestionEvenements.getInstance().supprimerEvenement(evenementSelectionne.getId());

                // Rafraîchir la liste
                rafraichirListeEvenements();

                // Message de succès
                showAlert("Suppression réussie",
                        "L'événement '" + evenementSelectionne.getNom() + "' a été supprimé avec succès.",
                        AlertType.INFORMATION);

            } catch (Exception e) {
                showAlert("Erreur",
                        "Une erreur est survenue lors de la suppression : " + e.getMessage(),
                        AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}