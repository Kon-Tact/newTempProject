package com.libgdx.roguelike;

import java.util.ArrayList;
import java.util.List;

public interface FirebaseInterface {

    public static List<Player> allPlayers = new ArrayList<>();
    public static List<String> documents = new ArrayList<>();

    void readDocumentsFromDB();

    void sendToDB(float x, float y);

    void init(String uniqueID);
}
