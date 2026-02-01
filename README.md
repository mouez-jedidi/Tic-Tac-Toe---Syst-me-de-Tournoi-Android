# 🎮 Tic-Tac-Toe Android - Système de Tournoi avec Persistance

[![Android](https://img.shields.io/badge/Android-5.0+-green)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-8-orange)](https://www.java.com/)
[![MVC](https://img.shields.io/badge/Architecture-MVC-blue)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![Material Design](https://img.shields.io/badge/UI-Material%20Design-purple)](https://material.io/)

## 📋 Table des Matières
- [À Propos](#-à-propos)
- [Cahier des Charges](#-cahier-des-charges)
- [Architecture MVC](#-architecture-mvc)
- [Fonctionnalités Principales](#-fonctionnalités-principales)
- [Implémentation Technique](#-implémentation-technique)
- [Interface & UX](#-interface--ux)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Tests & Validation](#-tests--validation)
- [Perspectives](#-perspectives)
- [Contributeur](#-contributeur)

## 🎯 À Propos

**Tic-Tac-Toe Android** est une application mobile native complète développée dans le cadre du module de développement mobile à la **Faculté des Sciences de Sfax**. Le projet implémente un système de tournoi sophistiqué avec persistance des données et interface responsive.

### Objectif Pédagogique
Présenter une application Android complète avec :
- ✅ Architecture MVC bien structurée
- ✅ Persistance des données sans base de données
- ✅ Interface responsive adaptée à tous les écrans
- ✅ Gestion complète du cycle de vie Android

## 📝 Cahier des Charges

### Spécifications Fonctionnelles
| Fonctionnalité | Description |
|----------------|-------------|
| **Jeu Classique** | Grille 3×3 avec règles standard du Tic-Tac-Toe |
| **Système de Tournoi** | Choix de 5, 10 ou 15 parties consécutives |
| **Choix du Symbole** | Joueur peut choisir X ou O |
| **Scores en Temps Réel** | Suivi des victoires X, O et matchs nuls |
| **Persistance** | Sauvegarde et récupération du dernier tournoi |
| **Responsive Design** | Adaptation automatique à toutes les tailles d'écran |

### Spécifications Techniques
| Critère | Valeur |
|---------|--------|
| **IDE** | Android Studio 2021+ |
| **Langage** | Java 8 |
| **API Minimum** | 21 (Android 5.0 Lollipop) |
| **Orientation** | Portrait uniquement |
| **Stockage** | Fichier interne (sérialisation Java) |
| **Base de Données** | ❌ Non utilisée (volontairement) |

## 🏗️ Architecture MVC

Le projet suit strictement le pattern **Model-View-Controller** pour une séparation claire des responsabilités.

```
┌─────────────────────────────────────────────────┐
│                    VIEW                         │
│  ┌──────────────────────────────────────────┐  │
│  │ activity_main.xml - Écran d'accueil      │  │
│  │ activity_game.xml - Écran du jeu         │  │
│  │ Drawables - Ressources visuelles         │  │
│  └──────────────────────────────────────────┘  │
│              ↕ (Interaction via Intent)         │
│                  CONTROLLER                     │
│  ┌──────────────────────────────────────────┐  │
│  │ MainActivity.java                        │  │
│  │ - Configuration du tournoi               │  │
│  │ - Navigation vers GameActivity           │  │
│  │                                          │  │
│  │ GameActivity.java                        │  │
│  │ - Gestion des événements (clics)        │  │
│  │ - Mise à jour de l'interface             │  │
│  │ - Appel au modèle                        │  │
│  └──────────────────────────────────────────┘  │
│              ↕ (Opérations CRUD)                │
│                    MODEL                        │
│  ┌──────────────────────────────────────────┐  │
│  │ TournoiData.java (Serializable)          │  │
│  │ - scoreX, scoreO, partiesNulles          │  │
│  │ - totalParties, vainqueur                │  │
│  │ - Méthodes de sérialisation/désérial.    │  │
│  │                                          │  │
│  │ Algorithmes du Jeu                       │  │
│  │ - Vérification victoire O(1)             │  │
│  │ - Détection match nul                    │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

### Diagramme de Classes

```java
AppCompatActivity
   ├── MainActivity
   │    └── Navigation → Intent → GameActivity
   │
   └── GameActivity
        └── utilise → TournoiData (Serializable)
             ├── lecture données (deserialize)
             └── écriture données (serialize)
```

**Principe SOLID appliqué** : Séparation des responsabilités (SRP - Single Responsibility Principle)

## 🎮 Fonctionnalités Principales

### 1️⃣ Écran d'Accueil (MainActivity)
- 🎮 **Choix du symbole** : RadioButtons pour sélectionner X ou O
- 🏆 **Sélection du nombre de parties** : Spinner avec options 5/10/15
- 📖 **Bouton "Principe du jeu"** : Dialogue explicatif des règles
- 📊 **Bouton "Dernier Tournoi"** : Récupération et affichage des scores sauvegardés
- ▶️ **Bouton "JOUER"** : Lance le tournoi configuré

### 2️⃣ Écran de Jeu (GameActivity)
- 📍 **Numéro de partie** : Affichage "Partie X/Y"
- 📊 **Scores en temps réel** : X, O, Nulles (mise à jour automatique)
- 🎯 **Indicateur du tour** : Affichage coloré du joueur actuel
- 🎲 **Grille interactive 3×3** : Boutons dynamiques et responsifs
- 🔄 **Alternance automatique** : Changement de joueur après chaque coup

### 3️⃣ Fin de Tournoi
- 🏆 **Dialogue de résultats** : Récapitulatif complet du tournoi
- 💾 **Sauvegarde optionnelle** : Choix de persister les données
- 🏠 **Retour à l'accueil** : Navigation fluide

## 🛠️ Implémentation Technique

### Persistance - Sérialisation Java

**Avantages** : Simple, natif Java, sans dépendances externes, rapide

#### Classe Modèle Sérialisable

```java
public class TournoiData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributs encapsulés
    private int scoreX;
    private int scoreO;
    private int partiesNulles;
    private int totalParties;
    private String vainqueur;
    
    // Constructeur
    public TournoiData(int scoreX, int scoreO, int partiesNulles, 
                       int totalParties, String vainqueur) {
        this.scoreX = scoreX;
        this.scoreO = scoreO;
        this.partiesNulles = partiesNulles;
        this.totalParties = totalParties;
        this.vainqueur = vainqueur;
    }
    
    // Getters (Encapsulation)
    public int getScoreX() { return scoreX; }
    public int getScoreO() { return scoreO; }
    // ... autres getters
}
```

#### Sauvegarde des Données

```java
private void sauvegarderTournoi() {
    TournoiData data = new TournoiData(
        scoreX, scoreO, scoreNul, totalParties, vainqueur
    );
    
    try {
        FileOutputStream fos = openFileOutput("tournoi.dat", MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(data); // Sérialisation
        oos.close();
        fos.close();
        Toast.makeText(this, "Tournoi sauvegardé ✓", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
    }
}
```

#### Récupération des Données

```java
private void chargerDernierTournoi() {
    try {
        FileInputStream fis = openFileInput("tournoi.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        TournoiData data = (TournoiData) ois.readObject(); // Désérialisation
        ois.close();
        fis.close();
        
        // Affichage des résultats
        afficherResultats(data);
    } catch (FileNotFoundException e) {
        Toast.makeText(this, "Aucun tournoi enregistré", Toast.LENGTH_SHORT).show();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
```

### Algorithme de Victoire - Complexité O(1)

**Performance** : Temps constant, maximum 8 opérations

```java
private boolean verifierVictoire() {
    // 8 combinaisons possibles : 3 lignes + 3 colonnes + 2 diagonales
    
    // 1. Vérification des 3 lignes
    for (int i = 0; i < 3; i++) {
        if (!grille[i][0].equals("") && 
            grille[i][0].equals(grille[i][1]) && 
            grille[i][1].equals(grille[i][2])) {
            return true;
        }
    }
    
    // 2. Vérification des 3 colonnes
    for (int j = 0; j < 3; j++) {
        if (!grille[0][j].equals("") && 
            grille[0][j].equals(grille[1][j]) && 
            grille[1][j].equals(grille[2][j])) {
            return true;
        }
    }
    
    // 3. Vérification des 2 diagonales
    if (!grille[1][1].equals("") && 
        grille[0][0].equals(grille[1][1]) && 
        grille[1][1].equals(grille[2][2])) {
        return true; // Diagonale principale
    }
    
    if (!grille[1][1].equals("") && 
        grille[0][2].equals(grille[1][1]) && 
        grille[1][1].equals(grille[2][0])) {
        return true; // Diagonale secondaire
    }
    
    return false; // Aucune victoire
}
```

### Détection de Match Nul

```java
private boolean verifierMatchNul() {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (grille[i][j].equals("")) {
                return false; // Case vide trouvée
            }
        }
    }
    return true; // Grille complète sans victoire
}
```

## 📱 Interface & UX

### Grille Adaptative Multi-Résolutions

**Problème** : Dimensions fixes → mauvais affichage sur différents écrans  
**Solution** : Calcul dynamique en temps réel basé sur les métriques de l'écran

```java
private void creerGrilleAdaptative() {
    // 1. Récupération des métriques de l'écran
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int screenWidth = metrics.widthPixels;
    int screenHeight = metrics.heightPixels;
    
    // 2. Calcul optimal (85% de la plus petite dimension)
    int availableSize = Math.min(screenWidth, screenHeight);
    int gridSize = (int) (availableSize * 0.85);
    int buttonSize = (gridSize - 60) / 3; // 3×3 + marges
    
    // 3. Création dynamique des boutons
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            Button bouton = new Button(this);
            
            // Taille de texte proportionnelle
            bouton.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonSize * 0.4f);
            
            // Configuration des LayoutParams
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = buttonSize;
            params.height = buttonSize;
            params.setMargins(10, 10, 10, 10);
            
            bouton.setLayoutParams(params);
            grilleDynamique.addView(bouton);
        }
    }
}
```

**Résultat** : Interface parfaite sur smartphones (5"), phablettes (6") et tablettes (10")

### Différenciation Visuelle X-O

**Principe** : X et O doivent être immédiatement identifiables  
**Implémentation** : Couleurs distinctes + Emojis

```java
private void cliquerCase(int ligne, int colonne) {
    grille[ligne][colonne] = tourActuel;
    
    if (tourActuel.equals("X")) {
        boutons[ligne][colonne].setText("❌");
        boutons[ligne][colonne].setTextColor(
            Color.parseColor("#FF6B6B") // ROUGE pour X
        );
    } else {
        boutons[ligne][colonne].setText("⭕");
        boutons[ligne][colonne].setTextColor(
            Color.parseColor("#4ECDC4") // BLEU pour O
        );
    }
    
    boutons[ligne][colonne].setEnabled(false); // Désactivation
}
```

### Material Design

- **Cartes** : Elevation, ombres portées
- **Couleurs vives** : Palette cohérente et moderne
- **Feedback visuel** : États pressés, ripple effect
- **Typographie** : Roboto, hiérarchie claire

## 🚀 Installation

### Prérequis
```
Android Studio 2021 ou supérieur
JDK 8 ou supérieur
SDK Android API 21+ (Android 5.0)
Émulateur Android ou appareil physique
```

### Étapes d'Installation

1️⃣ **Cloner le repository**
```bash
git clone https://github.com/votre-username/tictactoe-android.git
cd tictactoe-android
```

2️⃣ **Ouvrir dans Android Studio**
```
File → Open → Sélectionner le dossier du projet
```

3️⃣ **Synchroniser Gradle**
```
Android Studio synchronisera automatiquement les dépendances
```

4️⃣ **Configurer l'émulateur ou l'appareil**
```
Tools → AVD Manager → Create Virtual Device
ou
Connecter un appareil physique avec USB Debugging activé
```

5️⃣ **Lancer l'application**
```
Run → Run 'app' (Shift + F10)
ou cliquer sur le bouton ▶️ dans la barre d'outils
```

## 📖 Utilisation

### Démarrage d'un Tournoi

1. **Choisir le symbole** : Sélectionner X ou O via les RadioButtons
2. **Définir le nombre de parties** : Choisir 5, 10 ou 15 dans le Spinner
3. **Lire les règles** (optionnel) : Cliquer sur "Principe du jeu"
4. **Lancer** : Appuyer sur le bouton "JOUER"

### Pendant le Jeu

1. **Jouer** : Toucher une case vide pour placer votre symbole
2. **Suivre les scores** : Vérifier les compteurs X/O/Nul en haut
3. **Identifier le tour** : L'indicateur coloré montre qui doit jouer
4. **Finir la partie** : Victoire ou nul → passage à la partie suivante

### Fin du Tournoi

1. **Consulter les résultats** : Dialogue avec statistiques complètes
2. **Sauvegarder** (optionnel) : Choisir de persister le tournoi
3. **Recommencer** : Retour à l'écran d'accueil

### Récupération d'un Tournoi

1. **Écran d'accueil** : Cliquer sur "Dernier Tournoi"
2. **Visualisation** : Dialogue avec tous les scores du dernier tournoi sauvegardé

## 🔧 Gestion du Cycle de Vie Android

### Problème : Rotation d'Écran
Lors d'une rotation, Android détruit et recrée l'Activity → perte de données

### Solution : Bundle (Sauvegarde Temporaire)

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
    
    // Restauration si rotation
    if (savedInstanceState != null) {
        scoreX = savedInstanceState.getInt("scoreX", 0);
        scoreO = savedInstanceState.getInt("scoreO", 0);
        partieActuelle = savedInstanceState.getInt("partie", 1);
        tourActuel = savedInstanceState.getString("tour", "X");
        // ... autres restaurations
    }
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("scoreX", scoreX);
    outState.putInt("scoreO", scoreO);
    outState.putInt("partie", partieActuelle);
    outState.putString("tour", tourActuel);
    // ... autres sauvegardes
}
```

**Résultat** : Conservation de l'état lors de changements de configuration (rotation, split-screen, etc.)

## 🧪 Tests & Validation

### Stratégie de Test

#### ✅ Tests Fonctionnels
| Test | Résultat |
|------|----------|
| Détection victoire (lignes) | ✅ Passé |
| Détection victoire (colonnes) | ✅ Passé |
| Détection victoire (diagonales) | ✅ Passé |
| Détection match nul | ✅ Passé |
| Alternance des joueurs | ✅ Passé |
| Calcul des scores | ✅ Passé |
| Passage entre parties | ✅ Passé |

#### ✅ Tests de Persistance
| Test | Résultat |
|------|----------|
| Sauvegarde des données | ✅ Passé |
| Récupération correcte | ✅ Passé |
| Gestion fichier inexistant | ✅ Passé |
| Intégrité après sérialisation | ✅ Passé |

#### ✅ Tests d'Interface
| Test | Résultat |
|------|----------|
| Responsive sur smartphone 5" | ✅ Passé |
| Responsive sur phablette 6" | ✅ Passé |
| Responsive sur tablette 10" | ✅ Passé |
| Navigation Intent | ✅ Passé |
| Rotation d'écran | ✅ Passé |

**Résultats Globaux** : ✅ **100% des tests passés**

## 🔮 Perspectives d'Amélioration

### Phase 1 : Architecture (Court Terme)
- [ ] Migration vers **MVVM** (ViewModel + LiveData)
- [ ] Migration **Room Database** pour multi-tournois
- [ ] Injection de dépendances avec **Dagger/Hilt**

### Phase 2 : Fonctionnalités (Moyen Terme)
- [ ] **Mode contre IA** (Algorithme Minimax)
- [ ] **Multijoueur en ligne** (Firebase Realtime Database)
- [ ] **Historique complet** des tournois
- [ ] **Statistiques avancées** (graphiques, tendances)

### Phase 3 : UX (Moyen Terme)
- [ ] **Animations avancées** (Lottie)
- [ ] **Personnalisation** (thèmes sombre/clair, sons)
- [ ] **Leaderboard global** (classement des joueurs)
- [ ] **Achievements** (système de badges)

### Phase 4 : Analytics & Monitoring (Long Terme)
- [ ] **Firebase Analytics** (comportement utilisateur)
- [ ] **Crashlytics** pour monitoring des crashs
- [ ] **Performance Monitoring** (temps de réponse)

## 📸 Captures d'Écran

### Points Clés Visuels
- ✅ Design moderne avec **Material Design**
- ✅ Couleurs vives et différenciées (Rouge/Bleu)
- ✅ Interface épurée et intuitive
- ✅ Feedback visuel immédiat
- ✅ Adaptation parfaite à tous les écrans

## 🎓 Compétences Acquises

### Développement Android
- ✅ Activities, Intents, Layouts XML
- ✅ Gestion du cycle de vie (onCreate, onSaveInstanceState, etc.)
- ✅ Ressources et adaptation multi-résolutions
- ✅ Material Design Guidelines

### Programmation Orientée Objet (Java)
- ✅ Encapsulation, héritage, polymorphisme
- ✅ Sérialisation d'objets
- ✅ Gestion des exceptions
- ✅ Collections et structures de données

### Design Patterns
- ✅ **MVC** (Model-View-Controller)
- ✅ **Observer** (listeners d'événements)
- ✅ **Singleton** (gestion de l'état)

### UI/UX
- ✅ Responsive Design adaptatif
- ✅ Principes de Material Design
- ✅ Accessibilité et ergonomie
- ✅ Feedback utilisateur

### Algorithmes & Structures de Données
- ✅ Matrices 2D (grille de jeu)
- ✅ Optimisation (complexité O(1))
- ✅ Conditions et boucles efficaces

### Tests & Débogage
- ✅ Tests fonctionnels manuels
- ✅ Utilisation de Logcat
- ✅ Gestion des erreurs et exceptions
- ✅ Validation sur différents appareils

## 📊 Métriques du Projet

| Métrique | Valeur |
|----------|--------|
| **Lignes de code** | ~800 LOC (Java + XML) |
| **Nombre de classes** | 3 principales (MainActivity, GameActivity, TournoiData) |
| **Nombre d'activités** | 2 (Main, Game) |
| **Taille APK** | ~2 MB |
| **API Minimum** | 21 (Android 5.0) |
| **API Cible** | 33 (Android 13) |
| **Temps de développement** | ~3 semaines |

## 👤 Contributeur

**Moez JEDIDI**  
Étudiant en LSI 3 - Faculté des Sciences de Sfax  
📧 [mouez.jedidi@gmail.com](mailto:mouez.jedidi@gmail.com)

### Encadrement
**Mme Amira TALHA** - Enseignante encadrante

## 📄 Licence

Ce projet est un **projet académique** développé dans le cadre de la formation LSI 3 à la Faculté des Sciences de Sfax.

## 🙏 Remerciements

- **Mme Amira TALHA** pour son encadrement et ses conseils
- **Android Developers** pour la documentation complète
- **Material Design Team** pour les guidelines UI/UX
- **Faculté des Sciences de Sfax** pour l'infrastructure et les ressources

---

<div align="center">

**⭐ Projet Académique - LSI 3 - 2025/2026**

**Développé avec ❤️ et Java**

[🐛 Signaler un bug](https://github.com/votre-username/tictactoe-android/issues) · [✨ Demander une fonctionnalité](https://github.com/votre-username/tictactoe-android/issues)

</div>
