import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Main {
    private static final String FILENAME = "nouns.txt";
    private static final String STOP_GAME_ANSWER = "Н";
    private static final String START_GAME_ANSWER = "Д";
    private static final int LETTERS_TO_OPEN = 2;
    private static final char MASK_SYMBOL = '_';
    private static final int MAX_LINES = 21_271;
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
            ===============""",
            """
            |---------|
            |         0
            |
            |
            |
            |
            |
            ===============""",
            """
            |---------|
            |         0
            |         |
            |         |
            |
            |
            |
            ===============""",
            """
            |---------|
            |         0
            |       / |
            |         |
            |
            |
            |
            ===============""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |
            |
            |
            ===============""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |        /
            |
            |
            |
            ===============""",
            """
            |---------|
            |         0
            |       / | \\
            |         |
            |        / \\
            |
            |
            |
            ==============="""

    };
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static boolean isWordGuessed(String word, char[] mask) {
        return Arrays.equals(word.toCharArray(), mask);
    }

    public static boolean isGameOver(String word, char[] mask, int mistakes) {
        return isWordGuessed(word, mask) || mistakes == MAX_MISTAKES;
    }

    public static boolean isGameStartAnswerValid(String answer) {
        return Objects.equals(answer, START_GAME_ANSWER.toLowerCase()) || Objects.equals(answer, STOP_GAME_ANSWER.toLowerCase());
    }

    public static boolean shouldGameStart() {
        String answer;

        do {
            System.out.println("Хотите сыграть в виселицу?");
            System.out.println("Введите " + START_GAME_ANSWER + " чтобы начать игру, " +
                    STOP_GAME_ANSWER + " чтобы выйти");
            answer = scanner.nextLine().toLowerCase();
        } while (!isGameStartAnswerValid(answer));


        return Objects.equals(answer, START_GAME_ANSWER.toLowerCase());
    }

    public static String readRandomWord() throws FileNotFoundException, NoSuchElementException {
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
            input = scanner.nextLine().toLowerCase();
        } while (input.length() != 1);
        return input.toCharArray()[0];
    }

    public static void printHangman(int mistakes) {
        if (mistakes > hangman.length - 1) {
            System.out.println(hangman[hangman.length - 1]);
        } else {
            System.out.println(hangman[mistakes]);
        }
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

    public static void openLetter(String word, char[] mask, char letter, Set<Character> usedChars) {
        usedChars.add(letter);
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                mask[i] = letter;
            }
        }
    }

    public static void openRandomLetters(String word, char[] mask, int count, Set<Character> usedChars) {
        while (count > 0) {
            int index = random.nextInt(mask.length);

            if (isWordGuessed(word, mask)) {
                break;
            }
            if (mask[index] != MASK_SYMBOL) {
                continue;
            }
            openLetter(word, mask, word.charAt(index), usedChars);
            count--;
        }

    }

    public static void printGameResult(String word, int mistakes) {
        System.out.println();
        if (mistakes == MAX_MISTAKES) {
            System.out.println("Поражение");
        } else {
            System.out.println("Победа!");
        }
        System.out.println("Слово было: " + word);
        System.out.println();
    }

    public static void doGame(String word, char[] mask, Set<Character> usedChars) {
        int mistakes = 0;

        while (!isGameOver(word, mask, mistakes)) {
            printMaskedWord(mask);
            char guessedLetter = getInputLetter();

            if (!usedChars.contains(guessedLetter)) {
                openLetter(word, mask, guessedLetter, usedChars);

                if (word.chars().noneMatch(ch -> ch == guessedLetter)) {
                    ++mistakes;
                }
            }

            printHangman(mistakes);
            System.out.println("Использованные буквы: " + usedChars);
        }

        printGameResult(word, mistakes);
    }

    public static void startGame() throws FileNotFoundException, NoSuchElementException {
        String word = readRandomWord();
        Set<Character> usedChars = new HashSet<>();
        char[] mask = word.toCharArray();

        Arrays.fill(mask, MASK_SYMBOL);
        openRandomLetters(word, mask, LETTERS_TO_OPEN, usedChars);
        doGame(word, mask, usedChars);
    }

    public static void main(String[] args) {
        while (shouldGameStart()) {
            try {
                startGame();
            } catch (FileNotFoundException | NoSuchElementException exception) {
                System.out.println(exception.getMessage());
                break;
            }
        }

    }
}