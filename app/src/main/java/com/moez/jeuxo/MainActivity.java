package com.moez.jeuxo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupSymbole;
    private RadioButton radioX, radioO;
    private Spinner spinnerParties;
    private Button btnJouer, btnPrincipe, btnRetrouverScores;

    private static final String FICHIER_TOURNOI = "dernierTournoi.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupSpinner();
        setupListeners();
    }

    private void initializeViews() {
        radioGroupSymbole = findViewById(R.id.radioGroupSymbole);
        radioX = findViewById(R.id.radioX);
        radioO = findViewById(R.id.radioO);
        spinnerParties = findViewById(R.id.spinnerParties);
        btnJouer = findViewById(R.id.btnJouer);
        btnPrincipe = findViewById(R.id.btnPrincipe);
        btnRetrouverScores = findViewById(R.id.btnRetrouverScores);
    }

    private void setupSpinner() {
        String[] options = {"5 parties", "10 parties", "15 parties"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParties.setAdapter(adapter);
    }

    private void setupListeners() {
        btnJouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerJeu();
            }
        });

        btnPrincipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherPrincipe();
            }
        });

        btnRetrouverScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherDernierTournoi();
            }
        });
    }

    private void lancerJeu() {
        // Récupérer le symbole choisi
        String symboleJoueur = radioX.isChecked() ? "X" : "O";

        // Récupérer le nombre de parties
        String selection = spinnerParties.getSelectedItem().toString();
        int nombreParties = Integer.parseInt(selection.split(" ")[0]);

        // Lancer l'activité de jeu
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("symboleJoueur", symboleJoueur);
        intent.putExtra("nombreParties", nombreParties);
        startActivity(intent);
    }

    private void afficherPrincipe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📖 Principe du Jeu");
        builder.setMessage(R.string.principe_texte);
        builder.setPositiveButton("Compris !", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void afficherDernierTournoi() {
        try {
            FileInputStream fis = openFileInput(FICHIER_TOURNOI);
            ObjectInputStream ois = new ObjectInputStream(fis);
            TournoiData data = (TournoiData) ois.readObject();
            ois.close();
            fis.close();

            String message = "🏆 Résultats du Dernier Tournoi\n\n" +
                    "❌ Joueur X : " + data.getScoreX() + " victoires\n" +
                    "⭕ Joueur O : " + data.getScoreO() + " victoires\n" +
                    "🤝 Parties nulles : " + data.getPartiesNulles() + "\n" +
                    "📊 Total parties : " + data.getTotalParties() + "\n\n" +
                    "🎉 Vainqueur : " + data.getVainqueur();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("📊 Dernier Tournoi");
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            builder.create().show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.aucun_tournoi), Toast.LENGTH_SHORT).show();
        }
    }
}