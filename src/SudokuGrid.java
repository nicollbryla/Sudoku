import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Vector;


public class SudokuGrid {
    private int _x;
    private int _y;
    private GridPane _sudokuGrid;

    SudokuGrid(int x, int y, Vector<String> acceptableVals, String hardPuzzle) {
        _x = x;
        _y = y;

        _sudokuGrid = makeGrid(acceptableVals, hardPuzzle);
    }

    private GridPane makeGrid(Vector<String> acceptableVals, String hardPuzzle) {
        GridPane sudokuGrid = new GridPane();

        VetoListener vetoL = new VetoListener(acceptableVals, _x, _y, hardPuzzle);
        int amountfOfVeto = _x * _x * _y * _y;
        ArrayList allBeans = new ArrayList();
        for (int i = 0; i < amountfOfVeto; ++i) {
            allBeans.add(new SudokuBean());
        }
        PropertiesListener propL = new PropertiesListener(allBeans, _x, _y);
        for (int i = 0; i < _y; ++i) {
            for (int j = 0; j < _x; ++j) {
                GridPane smallerGrid = new GridPane();
                smallerGrid.setStyle("-fx-border-color: #000000; -fx-border-width: 1.5");

                for (int ii = 0; ii < _x; ++ii) {
                    for (int jj = 0; jj < _y; ++jj) {
                        int row = i * _x + ii;
                        int column = j * _y + jj;
                        int idx = row * _x * _y + column;
                        String newNumber = hardPuzzle.substring(idx, idx + 1);
                        boolean locked = false;
                        if (!newNumber.equals(" ")) {
                            locked = true;
                        }
                        SudokuBean bean = new SudokuBean(newNumber, row, column, locked);
                        bean.addVetoableChangeListener(vetoL);
                        bean.addPropertyChangeListener(propL);
                        bean.getTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                if (newValue.equals("")) {
                                    newValue = " ";
                                }
                                if (!newValue.equals(" ")) {
                                    newValue = newValue.trim();
                                }
                                bean.setNumber(newValue);
                            } catch (PropertyVetoException exc) {
                                System.out.println("exc sudokuGrid setting val");
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning Dialog");
                                alert.setHeaderText("This value cannot be put here, because...");
                                alert.setContentText(exc.getMessage());
                                alert.showAndWait();
                                bean.getTextField().setText(oldValue);
                            }
                        });
                        smallerGrid.add(bean.getTextField(), jj, ii);
                        allBeans.set(idx, bean);
                    }
                }
                sudokuGrid.add(smallerGrid, j, i);
            }
        }
        return sudokuGrid;
    }


    public GridPane getSudokuGrid() {
        return _sudokuGrid;
    }
}
