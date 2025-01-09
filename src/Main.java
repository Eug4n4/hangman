import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;


public class Main {
    private static Scanner startGamePrompt = new Scanner(System.in);
    private static Random random = new Random();
    private static final String FILENAME = "nouns.txt";
    private static final int MAX_LINES = 33335;

    public static boolean isGameStartAnswerValid(String answer) {
        return Objects.equals(answer, "Д") || Objects.equals(answer, "Н");
    }

    public static boolean shouldGameStart() {
        String answer;

        do {
            System.out.println("Хотите сыграть в виселицу?");
            System.out.println("Введите Д чтобы начать игру, Н чтобы выйти");
            answer = startGamePrompt.nextLine();
        } while (!isGameStartAnswerValid(answer));


        return Objects.equals(answer, "Д");
    }


    public static String readRandomWord() {
        int lineNumber = random.nextInt(MAX_LINES);

        try (Scanner scanner = new Scanner(new FileReader(FILENAME))) {
            for (int i = 0; i < lineNumber; i++) {
                scanner.nextLine();
            }
            return scanner.nextLine();
        } catch (IOException exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        while (shouldGameStart()) {

        }

    }
}