package com.google.flourbot.datastorage;

public interface DataStorage {
    QueryDocumentSnapshot getDocument (String userEmail, String message) throws Exception; 
}