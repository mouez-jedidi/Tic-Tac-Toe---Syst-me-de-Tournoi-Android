package com.moez.jeuxo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class GameActivity extends AppCompatActivity {

    private TextView tvPartieNumero, tvScoreX, tvScoreO, tvScoreNul, tvTourActuel;
    private GridLayout gridLayout;
    private SquareButton[][] boutons = new SquareButton[3][3];

    private String symboleJoueur;
    private int nombrePartiesTotal;
    private int partieActuelle = 1;
    private int scoreX = 0;
    private int scoreO = 0;
    private int scoreNul = 0;

    private String tourActuel = "X";
    private String[][] grille = new String[3][3];
    private boolean jeuActif = true;

    private static final String FICHIER_TOURNOI = "dernierTournoi.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        symboleJoueur = getIntent().getStringExtra("symboleJoueur");
        nombrePartiesTotal = getIntent().getIntExtra("nombreParties", 5);

        initializeViews();

        // Post pour s'assurer que GridLayout est déjà mesuré
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                creerGrilleResponsive();
            }
        });

        mettreAJourAffichage();
    }

    private void initializeViews() {
        tvPartieNumero = findViewById(R.id.tvPartieNumero);
        tvScoreX = findViewById(R.id.tvScoreX);
        tvScoreO = findViewById(R.id.tvScoreO);
        tvScoreNul = findViewById(R.id.tvScoreNul);
        tvTourActuel = findViewById(R.id.tvTourActuel);
        gridLayout = findViewById(R.id.gridLayout);
    }

    private void creerGrilleResponsive() {
        gridLayout.removeAllViews();

        int gridWidth = gridLayout.getWidth();
        int gridHeight = gridLayout.getHeight();
        int gridSize = Math.min(gridWidth, gridHeight);
        int buttonSize = gridSize / 3;

        int marginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5,
                getResources().getDisplayMetrics()
        );

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SquareButton bouton = new SquareButton(this);
                bouton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize * 0.15f);
                bouton.setTextColor(Color.parseColor("#2C3E50"));
                bouton.setBackgroundResource(R.drawable.grid_button_modern);
                bouton.setAllCaps(false);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(marginPx, marginPx, marginPx, marginPx);
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                bouton.setLayoutParams(params);

                final int ligne = i;
                final int colonne = j;

                bouton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cliquerCase(ligne, colonne);
                    }
                });

                boutons[i][j] = bouton;
                grille[i][j] = "";
                gridLayout.addView(bouton);
            }
        }
    }

    private void cliquerCase(int ligne, int colonne) {
        if (!jeuActif || !grille[ligne][colonne].equals("")) return;

        grille[ligne][colonne] = tourActuel;

        // Mettre le symbole et la couleur
        if (tourActuel.equals("X")) {
            boutons[ligne][colonne].setText("❌");
            boutons[ligne][colonne].setTextColor(Color.parseColor("#FF6B6B"));
        } else {
            boutons[ligne][colonne].setText("⭕");
            boutons[ligne][colonne].setTextColor(Color.parseColor("#4ECDC4"));
        }

        if (verifierVictoire()) {
            jeuActif = false;
            if (tourActuel.equals("X")) scoreX++;
            else scoreO++;
            afficherResultatPartie("🎉 Victoire de " + tourActuel + " !");
        } else if (grilleComplete()) {
            jeuActif = false;
            scoreNul++;
            afficherResultatPartie("🤝 Partie Nulle !");
        } else {
            tourActuel = tourActuel.equals("X") ? "O" : "X";
            mettreAJourAffichage();
        }
    }

    private boolean verifierVictoire() {
        for (int i = 0; i < 3; i++)
            if (!grille[i][0].equals("") &&
                    grille[i][0].equals(grille[i][1]) &&
                    grille[i][1].equals(grille[i][2])) return true;

        for (int j = 0; j < 3; j++)
            if (!grille[0][j].equals("") &&
                    grille[0][j].equals(grille[1][j]) &&
                    grille[1][j].equals(grille[2][j])) return true;

        if (!grille[0][0].equals("") &&
                grille[0][0].equals(grille[1][1]) &&
                grille[1][1].equals(grille[2][2])) return true;

        if (!grille[0][2].equals("") &&
                grille[0][2].equals(grille[1][1]) &&
                grille[1][1].equals(grille[2][0])) return true;

        return false;
    }

    private boolean grilleComplete() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grille[i][j].equals("")) return false;
        return true;
    }

    private void afficherResultatPartie(String message) {
        mettreAJourAffichage();

        new Handler().postDelayed(() -> {
            if (partieActuelle < nombrePartiesTotal) {
                partieActuelle++;
                reinitialiserPartie();
            } else {
                afficherResultatTournoi();
            }
        }, 1500);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void reinitialiserPartie() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                grille[i][j] = "";
                boutons[i][j].setText("");
            }
        tourActuel = "X";
        jeuActif = true;
        mettreAJourAffichage();
    }

    private void mettreAJourAffichage() {
        tvPartieNumero.setText("Partie " + partieActuelle + " / " + nombrePartiesTotal);
        tvScoreX.setText(String.valueOf(scoreX));
        tvScoreO.setText(String.valueOf(scoreO));
        tvScoreNul.setText(String.valueOf(scoreNul));

        if (tourActuel.equals("X")) {
            tvTourActuel.setText("Tour de ❌");
            tvTourActuel.setTextColor(Color.parseColor("#FF6B6B"));
        } else {
            tvTourActuel.setText("Tour de ⭕");
            tvTourActuel.setTextColor(Color.parseColor("#4ECDC4"));
        }
    }

    private void afficherResultatTournoi() {
        String vainqueur;
        String titre;

        if (scoreX > scoreO) {
            vainqueur = "Joueur X";
            titre = "🏆 Victoire du Joueur X !";
        } else if (scoreO > scoreX) {
            vainqueur = "Joueur O";
            titre = "🏆 Victoire du Joueur O !";
        } else {
            vainqueur = "Égalité";
            titre = "🤝 Égalité Parfaite !";
        }

        String message = "🎉 Tournoi Terminé !\n\n" +
                "❌ Joueur X : " + scoreX + " victoires\n" +
                "⭕ Joueur O : " + scoreO + " victoires\n" +
                "🤝 Parties nulles : " + scoreNul + "\n\n" +
                "👑 Champion : " + vainqueur;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("💾 Sauvegarder", (dialog, which) -> {
            sauvegarderTournoi(vainqueur);
            finish();
        });

        builder.setNegativeButton("🏠 Accueil", (dialog, which) -> finish());

        builder.create().show();
    }

    private void sauvegarderTournoi(String vainqueur) {
        try {
            TournoiData data = new TournoiData(scoreX, scoreO, scoreNul, nombrePartiesTotal, vainqueur);
            FileOutputStream fos = openFileOutput(FICHIER_TOURNOI, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();

            Toast.makeText(this, "✅ Tournoi sauvegardé avec succès !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "❌ Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
