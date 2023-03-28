import java.util.Random;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private ComboBox<Operator> operatorComboBox;

    @FXML
    private ComboBox<Difficulty> difficultyComboBox;

    @FXML
    private Label problemLabel;

    @FXML
    private TextField answerTextField;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timeLabel;

    private IntegerProperty score = new SimpleIntegerProperty(0);
    private IntegerProperty timeLeft = new SimpleIntegerProperty(60);
    private Operator operator;
    private Difficulty difficulty;
    private Random random = new Random();

    public enum Operator {
        PLUS("+"),
        MINUS("-"),
        TIMES("*"),
        DIVIDE("/");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum Difficulty {
        EASY(10),
        MEDIUM(50),
        HARD(100);

        private final int limit;

        Difficulty(int limit) {
            this.limit = limit;
        }

        public int getLimit() {
            return limit;
        }
    }

    public void initialize() {
        operatorComboBox.getItems().addAll(Operator.values());
        difficultyComboBox.getItems().addAll(Difficulty.values());
        operatorComboBox.setValue(Operator.PLUS);
        difficultyComboBox.setValue(Difficulty.EASY);
        scoreLabel.textProperty().bind(score.asString("Score: %d"));
        timeLabel.textProperty().bind(timeLeft.asString("Time left: %d seconds"));
        startTimer();
        generateProblem();
    }

    @FXML
    public void handleSubmit() {
        try {
            int answer = Integer.parseInt(answerTextField.getText());
            int correctAnswer = evaluateProblem();
            if (answer == correctAnswer) {
                score.set(score.get() + 1);
            }
            scoreLabel.setText("Score: " + score);
            generateProblem();
            answerTextField.clear();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please enter a valid number");
            alert.showAndWait();
        }
    }

    private void generateProblem() {
        if (timeLeft.get() == 0) {
            endGame();
            return;
        }
        operator = operatorComboBox.getValue();
        difficulty = difficultyComboBox.getValue();
        int operand1 = random.nextInt(difficulty.getLimit()) + 1;
        int operand2 = random.nextInt(difficulty.getLimit()) + 1;
        problemLabel.setText(operand1 + " " + operator.getSymbol() + " " + operand2 + " = ");
    }

    private int evaluateProblem() {
        String problemString = problemLabel.getText();
        String[] parts = problemString.split("\\s+");
        int operand1 = Integer.parseInt(parts[0]);
        operator = operatorComboBox.getValue();
        int operand2 = Integer.parseInt(parts[2]);
        int answer;
        switch (operator) {
            case PLUS:
                answer = operand1 + operand2;
                break;
            case MINUS:
                answer = operand1 - operand2;
                break;
            case TIMES:
                answer = operand1 * operand2;
                break;
            case DIVIDE:
                answer = operand1 / operand2;
                break;
            default:
                answer = 0;
        }
        return answer;
    }

    private void endGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Your score is " + score);
        alert.showAndWait();
        Platform.exit();
    }

    public void startTimer() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (timeLeft.get() > 0) {
                    Thread.sleep(1000);
                    timeLeft.set(timeLeft.get() - 1);
                    updateMessage("Time: " + timeLeft.get() + "s");
                }
                return null;
            }
        };
        timeLabel.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(event -> endGame());
        new Thread(task).start();
    }
}