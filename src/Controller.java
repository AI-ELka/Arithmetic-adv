// CalculatorController.java
import java.util.Random;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ComboBox<String> operatorComboBox;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label problemLabel;

    @FXML
    private TextField answerTextField;

    @FXML
    private Label scoreLabel;

    @FXML
    public static Label timeLabel;

    private static int score = 0;
    public static int timeLeft = 60;
    private String operator;
    private int difficulty;
    private Random random = new Random();

    @FXML
    public void initialize() {
        if (operatorComboBox.getItems() != null){
        operatorComboBox.getItems().addAll("+", "-", "*", "/");}
        if (difficultyComboBox.getItems() != null){
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");}
    }

    @FXML
    public void handleSubmit() {
        int answer = Integer.parseInt(answerTextField.getText());
        int correctAnswer = evaluateProblem();
        if (answer == correctAnswer) {
            score++;
        }
        scoreLabel.setText("Score: " + score);
        generateProblem();
        answerTextField.clear();
    }

    private void generateProblem() {
        if (timeLeft == 0) {
            endGame();
            return;
        }
        operator = operatorComboBox.getValue();
        String difficultyString = difficultyComboBox.getValue();
        if (difficultyString.equals("Easy")) {
            difficulty = 10;
        } else if (difficultyString.equals("Medium")) {
            difficulty = 100;
        } else if (difficultyString.equals("Hard")) {
            difficulty = 1000;
        }
        int operand1 = random.nextInt(difficulty) + 1;
        int operand2 = random.nextInt(difficulty) + 1;
        
        problemLabel.setText(operand1 + " " + operator + " " + operand2 + " = ");
    }

    private int evaluateProblem() {
        String problemString = problemLabel.getText();
        String[] parts = problemString.split(" ");
        int operand1 = Integer.parseInt(parts[0]);
        operator = parts[1];
        int operand2 = Integer.parseInt(parts[2]);
        int answer;
        if (operator.equals("+")) {
            answer = operand1 + operand2;
        } else if (operator.equals("-")) {
            answer = operand1 - operand2;
        } else if (operator.equals("*")) {
            answer = operand1 * operand2;
        } else {
            answer = operand1 / operand2;
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
    
    public static void startTimer() {
        Thread timerThread = new Thread(() -> {
            try {
                while (timeLeft > 0) {
                    Thread.sleep(1000);
                    timeLeft--;
                    if (timeLabel != null) {
                        timeLabel.setText("some text");
                    }
                }
                 Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game over");
                    alert.setHeaderText("Your score is " + score);
                    alert.showAndWait();
                });
                 
            }
             catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        timerThread.start();
    }
}

