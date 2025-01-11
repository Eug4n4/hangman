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

    private static boolean isWordGuessed(String word, char[] mask) {
        return Arrays.equals(word.toCharArray(), mask);
    }

    private static boolean isGameOver(String word, char[] mask, int mistakes) {
        return isWordGuessed(word, mask) || mistakes == MAX_MISTAKES;
    }

    private static boolean isGameStartAnswerValid(String answer) {
        return Objects.equals(answer, START_GAME_ANSWER.toLowerCase()) || Objects.equals(answer, STOP_GAME_ANSWER.toLowerCase());
    }

    private static boolean shouldGameStart() {
        String answer;

        do {
            System.out.println("Хотите сыграть в виселицу?");
            System.out.printf("Введите %s чтобы начать игру, %s чтобы выйти\n", START_GAME_ANSWER, STOP_GAME_ANSWER);
            answer = scanner.nextLine().toLowerCase();
        } while (!isGameStartAnswerValid(answer));


        return Objects.equals(answer, START_GAME_ANSWER.toLowerCase());
    }

    private static String readRandomWord() throws FileNotFoundException, NoSuchElementException {
        int lineNumber = random.nextInt(MAX_LINES);

        try (Scanner scanner = new Scanner(new FileReader(FILENAME))) {
            for (int i = 0; i < lineNumber; i++) {
                scanner.nextLine();
            }
            return scanner.nextLine();
        }
    }

    private static boolean isCyrillicLetter(char letter) {
        return Character.UnicodeBlock.of(letter) == Character.UnicodeBlock.CYRILLIC;
    }

    private static char getInputLetter() {
        char[] input;

        do {
            System.out.println("Введите букву:");
            input = scanner.nextLine().toLowerCase().toCharArray();
        } while (input.length != 1 || !isCyrillicLetter(input[0]));
        return input[0];
    }

    private static void printHangman(int mistakes) {
        if (mistakes > hangman.length - 1) {
            System.out.println(hangman[hangman.length - 1]);
        } else {
            System.out.println(hangman[mistakes]);
        }
    }

    private static void printMaskedWord(char[] mask) {
        for (int i = 0; i < mask.length; i++) {
            System.out.print(mask[i]);
            if (i != mask.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

    }

    private static void openLetter(String word, char[] mask, char letter, Set<Character> usedChars) {
        usedChars.add(letter);
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                mask[i] = letter;
            }
        }
    }

    private static void openRandomLetters(String word, char[] mask, Set<Character> usedChars) {
        int count = LETTERS_TO_OPEN;

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

    private static void printGameResult(String word, int mistakes) {
        System.out.println();
        if (mistakes == MAX_MISTAKES) {
            System.out.println("Поражение");
        } else {
            System.out.println("Победа!");
        }
        System.out.println("Слово было: " + word);
        System.out.println();
    }

    private static void doGame(String word, char[] mask, Set<Character> usedChars) {
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

    private static void startGame() throws FileNotFoundException, NoSuchElementException {
        String word = readRandomWord();
        Set<Character> usedChars = new HashSet<>();
        char[] mask = word.toCharArray();

        Arrays.fill(mask, MASK_SYMBOL);
        openRandomLetters(word, mask, usedChars);
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