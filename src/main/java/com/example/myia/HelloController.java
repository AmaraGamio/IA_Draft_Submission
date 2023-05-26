package com.example.myia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class HelloController {

    public GridPane gameGridPane;
    public HBox difficultyHBox;
    public String difficulty = "";
    public static ObservableList<Letter> vocabularyBank = FXCollections.observableArrayList();
    public static ArrayList<String> wordLine = new ArrayList<>();

    public void initialize(){
        //puts the contents of the hbox into the center
        difficultyHBox.setAlignment(Pos.CENTER);

        // Add rows and columns to the gameGridPane
        int gridSize = 10; // Grid size (10x10)
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button();
                button.setText(getRandomLetter());
                button.setMinSize(30, 30);
                button.setMaxSize(30, 30);
                gameGridPane.add(button, col, row);
            }
        }
    }

    //**Loading the Vocabulary** https://www.cambridgeenglish.org/images/23387-ket-schools-vocabulary-list.pdf
    //I used this website to help: https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
    private static void loadVocab(String fileName) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(fileName + ".json")) {
            // Convert JSON file to List<Letter> object
            Type letterListType = new TypeToken<ArrayList<Letter>>(){}.getType();
            List<Letter> imports = gson.fromJson(reader, letterListType);
            vocabularyBank = FXCollections.observableArrayList(imports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This gets a random number between 0 and 26 which represents a letter. A = 65 ascii code, so add
    //the letterValue and you get a random letter.
    private String getRandomLetter() {
        Random random = new Random();
        int letterValue = random.nextInt(26); // Generate a random number between 0 and 25
        char letter = (char) ('A' + letterValue); // Convert the random number to an uppercase letter

        return String.valueOf(letter);
    }

    public static ArrayList<String> generateWordLine(int wordCount) {
        /*
         * 1. get a random first word.
         * 2. get a word that ends in the last word of the wordline.
         * 3. make sure the last word isn't already in the wordLine, if it is try again
         * 3. repeat for 5 words
         */

        wordLine.add(getRandomWord());
        System.out.println("first word: "+wordLine.get(0));
        int counter = 0;
        while(counter<wordCount) {
            wordLine.add(getWordStartingWith(wordLine.get(counter).charAt(wordLine.get(counter).length()-1)));
            counter++;
        }
        return wordLine;
    }

    private static String getWordStartingWith(char letterStart) {
        //https://stackoverflow.com/a/8879771 - how to get the int value of a character.
        Random rand = new Random();
        System.out.println("LetterStart: "+letterStart);
        int arraySelector = letterStart - 'a';
        System.out.println("arraySelector: "+arraySelector);
        int randomWord = rand.nextInt(vocabularyBank.get(arraySelector).words.length);
        String word = vocabularyBank.get(arraySelector).words[randomWord]; //
        System.out.println(word);
        return word; //
    }

    private static String getRandomWord() {
        Random rand = new Random();
        int randomLetter = rand.nextInt(26); //
        int randomWord = rand.nextInt(vocabularyBank.get(randomLetter).words.length);
        String word = vocabularyBank.get(randomLetter).words[randomWord]; //
        return word; //it gets a random letter between 0 and 26. Then it gets a random
        //word from size of the letters array.
    }

    public void onStartClick(ActionEvent actionEvent) {
        loadVocab(difficulty);
        /*Print vocabularyBank to test it loads correctly.*/
        vocabularyBank.forEach(letter -> {
            System.out.println(letter.toString());
        });

        System.out.println(generateWordLine(5));

        placeWords();
    }

    private void placeWords() {
        Random rand = new Random();
        int randomStartRow= rand.nextInt(10);
        int currentLetterPosX = 0;
        int currentLetterPosY = randomStartRow;

        int totalCharNum = 0;
        for (String s:wordLine) {
            totalCharNum += s.length();
        }
        totalCharNum = totalCharNum - wordLine.size()-1;
        int extraColumnsLeft = currentLetterPosX-totalCharNum;

        boolean forwardOrUPDOWN = true;
        int extraChars = 0;
        while(currentLetterPosX!=10){

        if((10-currentLetterPosY)==extraColumnsLeft){
            //place all letters until the end.
            currentLetterPosX++;
            Button btn = (Button) gameGridPane.getChildren().get(5 * 10 + 0);
            btn.setText(String.valueOf(wordLine.get(0).charAt(0)));
        }else {
            if (forwardOrUPDOWN == true) {
                extraChars = (10 - currentLetterPosY) - extraColumnsLeft;
                if (extraColumnsLeft >= 4) { //When there are more than 4 extra characters left
                    int lettersToPlace = rand.nextInt(4);
                    for(int i = 0;i<lettersToPlace;i++){

                    }
                }

            } else {
                boolean direction = rand.nextBoolean();
                //Check if there are enough spaces to go up or down depending on the direction
                extraChars = (10 - currentLetterPosY) - extraColumnsLeft;

                //t = up
                //f = down
                if (extraColumnsLeft >= 4) { //When there are more than 4 extra characters left
                    int lettersToPlace = rand.nextInt(4);

                    //Checks if there are enough spaces to go up or down
                    if (direction == true && currentLetterPosY < lettersToPlace) { //up
                        lettersToPlace = currentLetterPosY;
                        //place characters up
                    } else if (direction == false && 10 - currentLetterPosY < lettersToPlace) { //up
                        lettersToPlace = currentLetterPosY;
                        //place characters down
                    }
                } else {
                    int lettersToPlace = rand.nextInt(extraChars);
                    //Checks if there are enough spaces to go up or down
                    if (direction == true && currentLetterPosY < lettersToPlace) { //up
                        lettersToPlace = currentLetterPosY;
                        //place characters up
                    } else if (direction == false && 10 - currentLetterPosY < lettersToPlace) { //up
                        lettersToPlace = currentLetterPosY;
                        //place characters down
                    }
                }
            }
        }
        }

    }

    public void ketDifficultyAction(ActionEvent actionEvent) {
        difficulty = "KET";
    }

    public void petDifficultyAction(ActionEvent actionEvent) {
        difficulty = "PET";
    }

    public void fceDifficultyAction(ActionEvent actionEvent) {
        difficulty = "FCE";
    }
}