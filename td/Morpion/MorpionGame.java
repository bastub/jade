package td.Morpion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MorpionGame implements Serializable {
    char[][] morpionGrid;

    public MorpionGame(char[][] _morpionGrid) {
        morpionGrid = _morpionGrid;
    }

    public MorpionGame() {
        morpionGrid = new char[4][4];
        for(int i = 0 ; i < 4 ; i ++) {
            for(int j = 0 ; j < 4 ; j ++) {
                morpionGrid[i][j] = ' ';
            }
        }
    }

    public char[][] getMorpionGrid() {
        return morpionGrid;
    }

    public void setMorpionGrid(char[][] _morpionGrid) {
        morpionGrid = _morpionGrid;
    }

    @Override
    public String toString() {
        String morpionString = "";
        for(int i = 0 ; i < 4 ; i ++) {
            morpionString += "\n|";
            for(int j = 0 ; j < 4 ; j ++) {
                morpionString += String.valueOf(morpionGrid[i][j]) + "|";
            }
        }
        return morpionString;
    }

    public Boolean play(String coo, int id) {
        if (!coo.matches("([1-4],[1-4])")) return false;
        String[] cooArray = coo.split(",");
        int x = Integer.parseInt(cooArray[0]) - 1;
        int y = Integer.parseInt(cooArray[1]) - 1;
        if (morpionGrid[x][y] != ' ') return false;
        if (id == 1) {
            morpionGrid[x][y] = 'x';
            return true;
        } if (id == 2) {
            morpionGrid[x][y] = 'o';
            return true;
        } if (id == 3) {
            morpionGrid[x][y] = 's';
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        MorpionGame M = new MorpionGame();
        System.out.println(M);
        int id = 1;
        while(true) {
            System.out.println(M);
            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            String coos = reader.readLine();
            Boolean played = false;
            while (!played) {
                System.out.println("Your time to shine, Player " + id + "!");
                played = M.play(coos, id);
            }
            id++;
        }

    }
}
