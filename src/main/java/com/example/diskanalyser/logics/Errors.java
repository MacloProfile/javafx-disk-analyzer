package com.example.diskanalyser.logics;

import com.example.diskanalyser.AnalyzerApplication;

public class Errors {

    public static void errorCheck(int id) {
        String message;
        switch (id) {
            case (1):
                message = "insufficient permissions to scan this folder";
                break;
            default:
                message = "FATAL ERROR";
        }
        try {
            AnalyzerApplication.error(message);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
