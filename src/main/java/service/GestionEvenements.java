package service;

import exceptions.EvenementDejaExistantException;
import model.Concert;
import model.Conference;
import model.Evenement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GestionEvenements {
    private static GestionEvenements instance;
    private Map<String, Evenement> evenements = new HashMap<>();

    public static void setInstance(GestionEvenements instance) {
        GestionEvenements.instance = instance;
    }

    public Map<String, Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(Map<String, Evenement> evenements) {
        this.evenements = evenements;
    }

    // Constructeur privé
    private GestionEvenements() {
        try {
            ajouterEvenement(new Conference("C1", "Conférence IA", LocalDateTime.now(), "Salle A", 50, "Intelligence Artificielle"));
            ajouterEvenement(new Concert("C2", "Concert Rock", LocalDateTime.now(), "Stade", 1000, "The Rolling Stones", "Rock"));
        } catch (EvenementDejaExistantException e) {
            System.err.println("Erreur lors de l'ajout d'événements initiaux : " + e.getMessage());
        }
    }


    // Méthode statique pour obtenir l'instance
    public static GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    // Méthodes pour gérer les événements
    public void ajouterEvenement(Evenement event) throws EvenementDejaExistantException {
        if (evenements.containsKey(event.getId())) {
            throw new EvenementDejaExistantException("Événement déjà existant : " + event.getId());
        }
        evenements.put(event.getId(), event);
    }

    public Evenement rechercherEvenement(String id) {
        return evenements.get(id);
    }
}