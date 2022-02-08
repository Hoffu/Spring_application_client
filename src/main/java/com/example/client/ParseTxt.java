package com.example.client;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ParseTxt {
    public ArrayList<String> parse(String path){
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String strLine;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            while ((strLine = reader.readLine()) != null) {
                lines.add(strLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public ArrayList<Consultation> getConsultationsFromFile(String path) {
        ArrayList<Consultation> consultations = new ArrayList<>();
        ArrayList<String> lines = parse(path);
        lines.forEach((line) -> {
            String[] splitted = line.split(",");
            consultations.add(new Consultation(splitted[0], splitted[1], splitted[2], LocalDateTime.parse(splitted[3])));
        });
        return consultations;
    }
}
