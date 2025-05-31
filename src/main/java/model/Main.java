package model;

import exceptions.CapaciteMaxAtteinteException;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Concert concert = new Concert("C1", "Festival Jazz", LocalDateTime.now(), "Paris", 100, "Miles Davis", "Jazz");
        Participant participant = new Participant("P1", "Alice", "alice@example.com");

        try {
            concert.ajouterParticipant(participant);
            System.out.println("Participant ajouté !");
        } catch (CapaciteMaxAtteinteException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}