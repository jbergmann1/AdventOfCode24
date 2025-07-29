import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day4 implements Day {
    @Override
    public void execute() {
        String filePath = Day.filePath + "input4.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int lineCounter = 0;
            int wordCounter = 0;
            int xCounter = 0;
            char[][] words = new char[140][140];
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    words[lineCounter][i] = line.charAt(i);
                }
                lineCounter++;
            }
            for (int i = 0; i < words.length; i++) {
                for (int j = 0; j < words[i].length; j++) {
                    wordCounter += checkWord(words, i, j);
                    if (checkX(words, i, j)) xCounter++;
                }
            }
            System.out.println("XMAS: " + wordCounter + ", X-MAS: " + xCounter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int checkWord(char[][] words, int x, int y) {
        int wordCounter = 0;
        int middleIndex = 0;
        if (words[x][y] != 'X') return wordCounter;
        StringBuilder word = new StringBuilder();
        //horizontal
        for (int i = Math.max(0, y - 3); i < Math.min(words[x].length, y + 4); i++) {
            word.append(words[x][i]);
            if (i == y) middleIndex = word.length() - 1;
        }
        wordCounter += checkWord(String.valueOf(word), middleIndex);
        middleIndex = 0;
        //vertical
        word = new StringBuilder();
        for (int i = Math.max(0, x - 3); i < Math.min(words.length, x + 4); i++) {
            word.append(words[i][y]);
            if (i == x) middleIndex = word.length() - 1;
        }
        wordCounter += checkWord(String.valueOf(word), middleIndex);
        middleIndex = 0;
        //diagonal ascending
        word = new StringBuilder();
        for (int i = y - 3; i < y + 4; i++) {
            int j = x + (y - i);
            if (j < 0 || j >= words.length || i < 0 || i >= words[j].length) continue;
            word.append(words[j][i]);
            if (i == y) middleIndex = word.length() - 1;
        }
        wordCounter += checkWord(String.valueOf(word), middleIndex);
        middleIndex = 0;
        //diagonal descending
        word = new StringBuilder();
        for (int i = y - 3; i < y + 4; i++) {
            int j = x - (y - i);
            if (j < 0 || j >= words.length || i < 0 || i >= words[j].length) continue;
            word.append(words[j][i]);
            if (i == y) middleIndex = word.length() - 1;
        }
        wordCounter += checkWord(String.valueOf(word), middleIndex);
        middleIndex = 0;
        return wordCounter;
    }

    private boolean checkX(char[][] words, int x, int y) {
        int wordCounter = 0;
        if (words[x][y] != 'A') return false;
        //diagonal ascending
        StringBuilder word = new StringBuilder();
        for (int i = y - 1; i < y + 2; i++) {
            int j = x + (y - i);
            if (j < 0 || j >= words.length || i < 0 || i >= words[j].length) continue;
            word.append(words[j][i]);
        }
        wordCounter += checkX(String.valueOf(word));
        //diagonal descending
        word = new StringBuilder();
        for (int i = y - 1; i < y + 2; i++) {
            int j = x - (y - i);
            if (j < 0 || j >= words.length || i < 0 || i >= words[j].length) continue;
            word.append(words[j][i]);
        }
        wordCounter += checkX(String.valueOf(word));
        return wordCounter == 2;
    }

    private int checkWord(String word, int middleIndex) {
        if (word.length() <= middleIndex) return 0;
        int horizontalCounter = 0;
        if (word.substring(0, middleIndex + 1).equals("SAMX")) horizontalCounter++;
        if (word.substring(middleIndex).equals("XMAS")) horizontalCounter++;
        return horizontalCounter;
    }

    private int checkX(String word) {
        if (word.length() != 3) return 0;
        int counter = 0;
        if (word.equals("SAM")) counter++;
        if (word.equals("MAS")) counter++;
        return counter;
    }
}
