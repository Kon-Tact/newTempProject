package com.libgdx.roguelike.preGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.libgdx.roguelike.R;

public class ModeSelection extends AppCompatActivity {

    public String pseudoFromLastActivity;
    public TextView pseudoDisplayedRightTop;

    public void initUi() {
        pseudoDisplayedRightTop = findViewById(R.id.pseudoDisplayedRightTop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);
        Intent intent = getIntent();
        initUi();

        //on vérifie si l'intent n'est pas nul
        if (intent != null) {
            if(intent.hasExtra("PseudoDisplayedRightTop")) {
                pseudoFromLastActivity = intent.getStringExtra("PseudoDisplayedRightTop");
            }
        }

        //On l'affiche sur le text view prévu a cet effet
        pseudoDisplayedRightTop.setText(pseudoFromLastActivity);
    }


}