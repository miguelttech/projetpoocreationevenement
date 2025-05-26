 
markdown
# **Réalisé par NJIKI TCHOUBIA MIGUEL 3GI 22P533**
📅 Système de Gestion d'Événements (JavaFX)

Application de gestion d'événements (conférences/concerts) avec interface graphique JavaFX, persistance des données et notifications.

## 🚀 Fonctionnalités
- **Création d'événements** (Conférences/Concerts)
- **Inscription des participants**
- **Gestion des capacités maximales**
- **Notifications** (Pattern Observer)
- **Persistance JSON/XML**
- **Interface moderne** (CSS personnalisé)

## 📦 Structure du Projet
```
projetpoo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controllers/       # Contrôleurs FXML
│   │   │   ├── model/             # Classes métier
│   │   │   ├── service/           # Logique métier
│   │   │   └── exceptions/        # Exceptions personnalisées
│   │   └── resources/
│   │       ├── fxml/              # Fichiers FXML
│   │       └── css/               # Feuilles de style
├── target/
└── README.md
```

## 🛠 Technologies
- **Java 17**
- **JavaFX 21** (UI)
- **Jackson** (Sérialisation JSON)
- **JUnit 5** (Tests)

## 🔧 Installation
1. Cloner le dépôt :
   bash
   git clone https://github.com/miguelttech/projetpoogestionevenement.git
   ```
2. Importer dans  intellij comme projet Maven/Java.
3. Exécuter App.java (Module path requis : JavaFX 21).

## 📸 Captures d'écran
| Page d'accueil | Création d'événement |
|----------------|-----------------------|
| ![Accueil](https://i.imgur.com/abc123.jpg) | ![Création](https://i.imgur.com/def456.jpg) |
 📝 UML Simplifié
 `mermaid
classDiagram
    class Evenement {
        +String id
        +String nom
        +ajouterParticipant()
    }
    Evenement <|-- Conference
    Evenement <|-- Concert
    Participant <|-- Organisateur
    GestionEvenements -- Evenement

## ✅ Tests
Exécuter les tests avec :
`bash
mvn test

Couverture visée : **70%** (Vérifie `target/site/jacoco/index.html`).

 📄 Rapport
- [Rapport PDF](/docs/Rapport.pdf) (Choix techniques, diagrammes UML)

 📅 Livraison
Date limite : **26 Mai 2024**  
Mail du prof : `w.kungne.enspy@gmail.com`

🙏 Remerciements
Projet réalisé dans le cadre du cours de POO avancée



 

 
