import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Vector;

public class Main extends Application {
    Stage _primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Sudoku");
        _primaryStage.setScene(new Scene(loadMainScreen(), 350, 400));
        _primaryStage.show();
    }

    public BorderPane loadMainScreen() {
        Button button = new Button();
        button.setText(" Play ");

        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("1~4", "1~6", "1~9", "1~10", "1~12");
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setTooltip(new Tooltip("Select the size of a puzzle."));
        choiceBox.setMinSize(80, 30);
        Label labelchoice = new Label("Select size:");
        VBox vb = new VBox(5);
        vb.getChildren().addAll(labelchoice, choiceBox);
        vb.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setCenter(vb);
        root.setBottom(button);
        BorderPane.setAlignment(root.getBottom(), Pos.CENTER);
        BorderPane.setAlignment(root.getCenter(), Pos.CENTER);
        button.setOnAction(event -> {
            String boxValue = (String) choiceBox.getValue();
            button.getScene().setRoot(loadSudokuScreen(boxValue.substring(boxValue.length() - 1)));
        });
        _primaryStage.setMinWidth(350);
        _primaryStage.setMinHeight(400);
        _primaryStage.setMaxWidth(350);
        _primaryStage.setMaxHeight(400);
        return root;
    }

    public VBox loadSudokuScreen(String size) {
        VBox vbRoot = new VBox(15);
        HBox hb = new HBox(10);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backButton.getScene().setRoot(loadMainScreen()));
        //backButton.setPrefSize(50, 50);

        Button resetButton = new Button("Reset");
        String finalSize = size;
        resetButton.setOnAction(e -> resetButton.getScene().setRoot(loadSudokuScreen(finalSize)));

        hb.getChildren().addAll(backButton, resetButton);
        int x = 0;
        int y = 0;
        Vector<String> acceptableVals = new Vector<>();
        String hardPuzzle = "";
        if (size.equals("0")) {
            size = "10";
        } else if (size.equals("2")) {
            size = "12";
        }
        for (int i = 1; i < Integer.parseInt(size) + 1; ++i) {
            if (i > 9) {
                int c = i + 55;
                acceptableVals.add(String.valueOf(Character.toString((char) c)));
            } else {
                acceptableVals.add(String.valueOf(i));
            }
        }
        acceptableVals.add(" ");
        switch (size) {
            case "4":
                x = 2;
                y = 2;
                hardPuzzle = " 2  4 1  1 3  2 ";
                break;
            case "6":
                x = 2;
                y = 3;
                hardPuzzle = "16   5  52  5   3  4   1  41  3   54";
                break;
            case "9":
                x = 3;
                y = 3;
                hardPuzzle = "  7 93       5  4 1 5     6   8 9 2 25      14  5 1         763 3 6  8    4 8 2  ";
                break;
            case "10":
                x = 2;
                y = 5;
                hardPuzzle = "  5 68 4   8  92  5 A 18  75 6  69  42  15      4262      19  32  8A  5 76  19 3 6  19  7   4 A5 6  ";
                break;
            case "12":
                x = 3;
                y = 4;
                hardPuzzle = "BC        472 9      6 1 4 68  3A 9   B  3A  1    6 58B4 2     32  C4      4A  59     8 3B91 5    1  C2  A   8 7C  A6 3 A C      7 936        5A";
                break;
        }
        double sizeOfPaneX = 45.0 + 50.0 * x * y;
        double sizeOfPaneY = 105.0 + 50.0 * x * y;
        SudokuGrid ss = new SudokuGrid(x, y, acceptableVals, hardPuzzle);
        vbRoot.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
        vbRoot.getChildren().addAll(hb, ss.getSudokuGrid());
        _primaryStage.setMinHeight(sizeOfPaneY);
        _primaryStage.setMinWidth(sizeOfPaneX);
        _primaryStage.setMaxWidth(sizeOfPaneX);
        _primaryStage.setMaxHeight(sizeOfPaneY);
        return vbRoot;
    }
}
