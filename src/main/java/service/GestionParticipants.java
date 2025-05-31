package service;

import model.Participant;
import java.util.*;

public class GestionParticipants {
    private static GestionParticipants instance;
    private Map<String, Participant> participants = new HashMap<>();

    // Constructeur privé pour Singleton
    private GestionParticipants() {}

    public static GestionParticipants getInstance() {
        if (instance == null) {
            instance = new GestionParticipants();
        }
        return instance;
    }

    public void ajouterParticipant(Participant participant) {
        participants.put(participant.getId(), participant);
    }

    public Participant rechercherParticipant(String id) {
        return participants.get(id);
    }

    public Collection<Participant> getTousLesParticipants() {
        return participants.values();
    }

    public boolean participantExiste(String email) {
        return participants.values().stream()
                .anyMatch(p -> p.getEmail().equals(email));
    }

    public Participant getParticipantParEmail(String email) {
        return participants.values().stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}