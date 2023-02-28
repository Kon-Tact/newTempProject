package com.libgdx.roguelike.preGame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.libgdx.roguelike.R;

import java.util.HashMap;
import java.util.Map;

public class ConnexionPage extends AppCompatActivity {

    private Button validerBtn;
    private EditText etPseudo;
    private String pseudoPlayer;
    private FirebaseFirestore mFirestore;
    Map<String, Object> pseudoHashMap = new HashMap<>();
    DocumentReference PseudoDocRef;

    public void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        PseudoDocRef = mFirestore.collection("Pseudo").document("pseudo");
    }

    public void initUi() {
        validerBtn = findViewById(R.id.validerBtn);
        etPseudo = findViewById(R.id.etPseudo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_page);

        initUi();
        initFirestore();

        validerBtn.setOnClickListener(v -> {
            if(etPseudo != null) {
                pseudoPlayer = etPseudo.getText().toString();
                Toast.makeText(ConnexionPage.this, "Vous avez été enregistré sous le pseudo "
                        + pseudoPlayer + ".", Toast.LENGTH_SHORT).show();

                //Ecriture en base
                pseudoHashMap.put("Pseudo " + pseudoPlayer, pseudoPlayer);
                PseudoDocRef.set(pseudoHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Writed to base" + pseudoPlayer);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Write failed");
                            }
                        });

                //Changement d'activité en donnant le pseudo pour l'activité suivante
                Intent itConnexionToModeSelection = new Intent(this, ModeSelection.class);
                itConnexionToModeSelection.putExtra("PseudoDisplayedRightTop", pseudoPlayer);
                startActivity(itConnexionToModeSelection);

            } else {
                Toast.makeText(ConnexionPage.this, "Merci d'entrer un pseudo",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}