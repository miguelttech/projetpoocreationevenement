package model;

import exceptions.CapaciteMaxAtteinteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Evenement {
    protected String id;
    protected String nom;
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;
    protected List<Participant> participants = new ArrayList<>();

    public String getNom() {
        return nom;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLieu() {
        return lieu;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    // Constructeur
    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
    }
    public String getId() {
        return id;
    }

    public String getType() {
        return this instanceof Conference ? "Conférence" : "Concert";
    }

    public String getDetails() {
        if (this instanceof Conference) {
            return "Thème: " + ((Conference)this).getTheme();
        } else if (this instanceof Concert) {
            return "Artiste: " + ((Concert)this).getArtiste();
        }
        return "";
    }

    // Méthodes
    public void ajouterParticipant(Participant participant) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement : " + nom);
        }
        participants.add(participant);
    }

    public void annuler() {
        System.out.println("Événement annulé : " + nom);
    }

    public abstract void afficherDetails(); // À implémenter dans les classes enfants
}