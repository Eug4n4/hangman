import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    private static final String FILENAME = "nouns.txt";
    private static final int MAX_LINES = 27359;
    private static final int MAX_MISTAKES = 6;
    private static final String[] hangman = new String[]{
            """
            |---------|
            |
            |
            |
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |
            |
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |         |
            |         |
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |       / |
            |         |
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |        /
            |
            |
            |
            ======================""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |        / \\
            |
            |
            |
            ======================"""

    };
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();


    public static boolean isGameStartAnswerValid(String answer) {
        return Objects.equals(answer, "Д") || Objects.equals(answer, "Н");
    }

    public static boolean shouldGameStart() {
        String answer;

        do {
            System.out.println("Хотите сыграть в виселицу?");
            System.out.println("Введите Д чтобы начать игру, Н чтобы выйти");
            answer = scanner.nextLine();
        } while (!isGameStartAnswerValid(answer));


        return Objects.equals(answer, "Д");
    }


    public static String readRandomWord() throws FileNotFoundException {
        int lineNumber = random.nextInt(MAX_LINES);

        try (Scanner scanner = new Scanner(new FileReader(FILENAME))) {
            for (int i = 0; i < lineNumber; i++) {
                scanner.nextLine();
            }
            return scanner.nextLine();
        }
    }

    public static char getInputLetter() {
        String input;

        do {
            System.out.println("Введите букву:");
            input = scanner.nextLine();
        } while (input.length() > 1);
        return input.toCharArray()[0];
    }

    public static void printMaskedWord(char[] mask) {
        for (int i = 0; i < mask.length; i++) {
            System.out.print(mask[i]);
            if (i != mask.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

    }

    public static void openRandomLetters(String word, char[] mask) {

        int c = 2;
        while (c != 0) {
            int index = random.nextInt(mask.length);

            if (mask[index] != '_') {
                continue;
            }
            for (int i = 0; i < word.length(); i++) {
                if (Objects.equals(word.charAt(i), word.charAt(index))) {
                    mask[i] = word.charAt(i);
                }
            }
            c--;
        }

    }

    public static void printHangman(int mistakes) {
        switch (mistakes) {
            case 0:
                System.out.println(hangman[0]);
                break;

            case 1:
                System.out.println(hangman[1]);
                break;

            case 2:
                System.out.println(hangman[2]);
                break;

            case 3:
                System.out.println(hangman[3]);
                break;

            case 4:
                System.out.println(hangman[4]);
                break;

            case 5:
                System.out.println(hangman[5]);
                break;
            case 6:
                System.out.println(hangman[6]);
                break;

        }
    }

    public static void startGame() throws FileNotFoundException {
        int mistakes = 0;
        String word = readRandomWord();
        Set<Character> usedChars = new HashSet<>();
        char[] mask = word.toCharArray();

        Arrays.fill(mask, '_');
        openRandomLetters(word, mask);
        while (mistakes < MAX_MISTAKES) {
            printMaskedWord(mask);
            if (Arrays.equals(mask, word.toCharArray())) {
                break;
            }
            char guessedLetter = getInputLetter();
            if (word.chars().anyMatch(c -> c == guessedLetter)) {
                for (int i = 0; i < word.length(); i++) {
                    if (Objects.equals(word.charAt(i), guessedLetter)) {
                        mask[i] = word.charAt(i);
                    }
                }
            } else {
                ++mistakes;
            }
            usedChars.add(guessedLetter);
            printHangman(mistakes);
            System.out.println("Использованные буквы: " + usedChars);
        }
        if (mistakes == MAX_MISTAKES) {
            System.out.println("Поражение");
        } else {
            System.out.println("Победа!");
        }
        System.out.println("Слово было: " + word);
        System.out.println();

    }

    public static void main(String[] args) {
        while (shouldGameStart()) {
            try {
                startGame();
            } catch (FileNotFoundException exception) {
                System.out.println(exception.getMessage());
                break;
            }
        }

    }
}