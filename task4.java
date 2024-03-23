import java.util.*;
import java.util.concurrent.*;

class QuizQuestion {
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    public QuizQuestion(String question, List<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

class Quiz {
    private List<QuizQuestion> questions;
    private int score;
    private int currentQuestionIndex;
    private ScheduledExecutorService timer;

    public Quiz(List<QuizQuestion> questions) {
        this.questions = questions;
        this.score = 0;
        this.currentQuestionIndex = 0;
        this.timer = Executors.newScheduledThreadPool(1);
    }

    public void displayQuestion() {
        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        System.out.println(currentQuestion.getQuestion());
        List<String> options = currentQuestion.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        // Start the timer for this question
        startTimer();
    }

    public void submitAnswer(int selectedOption) {
        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        if (selectedOption - 1 == currentQuestion.getCorrectAnswerIndex()) {
            score++;
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
        }
        currentQuestionIndex++;

        // Stop timer for the current question
        stopTimer();

        // Move to the next question if available
        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            displayResult();
        }
    }

    public int getScore() {
        return score;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    private void startTimer() {
        timer.schedule(() -> {
            System.out.println("Time's up!");
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                displayResult();
            }
        }, 30, TimeUnit.SECONDS); // Timer set for 30 seconds
    }

    private void stopTimer() {
        timer.shutdownNow();
        timer = Executors.newScheduledThreadPool(1);
    }

    private void displayResult() {
        System.out.println("Quiz Over!");
        System.out.println("Your Score: " + score + "/" + questions.size());
    }
}

class QuizApp {
    public static void main(String[] args) {
        // Sample quiz questions
        QuizQuestion question1 = new QuizQuestion("What is the capital of France?",
                Arrays.asList("A. London", "B. Paris", "C. Rome", "D. Berlin"), 1);
        QuizQuestion question2 = new QuizQuestion("Which planet is known as the Red Planet?",
                Arrays.asList("A. Jupiter", "B. Mars", "C. Venus", "D. Saturn"), 1);
        QuizQuestion question3 = new QuizQuestion("Who wrote 'To Kill a Mockingbird'?",
                Arrays.asList("A. J.K. Rowling", "B. Harper Lee", "C. Stephen King", "D. Ernest Hemingway"), 1);

        List<QuizQuestion> questions = Arrays.asList(question1, question2, question3);
        Quiz quiz = new Quiz(questions);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Quiz!");
        quiz.displayQuestion();

        while (quiz.getCurrentQuestionIndex() < questions.size()) {
            int selectedOption = getUserInput(scanner);
            quiz.submitAnswer(selectedOption);
        }
    }

    public static int getUserInput(Scanner scanner) {
        int selectedOption;
        while (true) {
            try {
                System.out.print("Enter your answer (1-4): ");
                selectedOption = Integer.parseInt(scanner.nextLine());
                if (selectedOption < 1 || selectedOption > 4) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
            }
        }
        return selectedOption;
    }
}