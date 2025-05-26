package model;

import java.time.LocalDateTime;
import java.util.List;

public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<String> getIntervenants() {
        return intervenants;
    }

    public void setIntervenants(List<String> intervenants) {
        this.intervenants = intervenants;
    }

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
    }

    @Override
    public void afficherDetails() {
        System.out.println("Conférence - Thème : " + theme + ", Lieu : " + lieu);
    }
}