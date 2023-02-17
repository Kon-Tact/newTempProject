package com.libgdx.roguelike;


import androidx.annotation.NonNull;

import com.badlogic.gdx.math.Vector2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidInterfaceClass implements FirebaseInterface {

    private float myX, myY;
    FirebaseFirestore db;
    CollectionReference dbCollection;
    DocumentReference myRef;
    Map<String, Object> user = new HashMap<>();

    public AndroidInterfaceClass() {
        db = FirebaseFirestore.getInstance();
        dbCollection = db.collection("ROGUELIKE2");

        ///////////// deleteDocuments();

        //myRef = dbRoguelike.document("");
        //myRef = db.collection("ROGUELIKE").document("Maposition");
    }

    // PROVOKE A BUG: OLD ACTIVE PLAYERS CANNOT WRITE AGAIN THEIR POSITIONS
//    private void deleteDocuments() {
//
//        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                List<Player> list = new ArrayList<>();
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        dbCollection.document(document.getId()).
//                                delete().
//                                addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        //           if (task.isSuccessful()) {          }
//                                    }
//                                });
//                    }
//                }
//            }
//        });
//    }

    @Override
    public void readDocumentsFromDB() {

        dbCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Player> list = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Player aPlayer = new Player(document.getId(),
                                //new Vector2(Float.parseFloat(document.getString("x")), Float.parseFloat(document.getString("y"))));
                                new Vector2(document.getLong("x"), document.getLong("y")));
                        list.add(aPlayer);
                    }

                    if (!libGDXRoguelike.lockOnListReadFromDB) {

                        //--------------------------------------------------------------------
                        libGDXRoguelike.lockOnListReadFromDB = true;
                        allPlayers.clear();
                        allPlayers.addAll(list);
                        libGDXRoguelike.lockOnListReadFromDB = false;
                        //--------------------------------------------------------------------

                        //                    libGDXRoguelike.lockOnListReadFromDB = true;
                        //                    documents.clear();
                        //                    documents.addAll(list);
                        //                    libGDXRoguelike.lockOnListReadFromDB = false;
                    }
                }
            }
        });

    }

    @Override
    public void sendToDB(float x, float y) {
        myX = x;
        myY = y;
        user.put("x", x);
        user.put("y", y);

        myRef.set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        System.out.println("OK sendXToDB -------------------- " + myX);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(/*@NonNull */Exception e) {
                        //Toast.makeText(MainActivity3.this, "ERREUR de l'ajout", Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, e.toString());
//                        System.out.println("ERROR sendXYToDB ----------------------" + myX);
                    }
                });
    }

    @Override
    public void init(String uniqueID) {
        myRef = dbCollection.document(uniqueID);
    }
}
