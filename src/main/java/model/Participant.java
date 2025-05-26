package model;

public class Participant {
    private String id;
    private String nom;
    private String email;

    public Participant(String id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    // Getters
    public String getEmail() { return email; }
    public String getNom() { return nom; }
}