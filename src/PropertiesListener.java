import javafx.scene.control.Alert;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;

public class PropertiesListener implements PropertyChangeListener {

    private int bigGridX;
    private int bigGridY;
    private int bigGridSize;
    private ArrayList<SudokuBean> allBeans;
    private HashSet<SudokuBean> wrongBeans;

    PropertiesListener(ArrayList<SudokuBean> beans, int x, int y) {
        allBeans = beans;
        bigGridX = x;
        bigGridY = y;
        bigGridSize = bigGridX * bigGridY;
        wrongBeans = new HashSet<>();
    }

    private boolean checkIfInRow(String number, int beanIdx, int x) {
        int curIdx = x * bigGridSize;
        for (int i = 0; i < bigGridSize; ++i) {
            if (curIdx != beanIdx) {
                if (allBeans.get(curIdx).getNumber().equals(number)) {
                    makeWrongBean(curIdx);
                    return true;
                }
            }
            curIdx++;
        }
        return false;
    }

    private boolean checkIfInColumn(String number, int beanIdx, int y) {
        int curIdx = y;
        for (int i = 0; i < bigGridSize; ++i) {
            if (curIdx != beanIdx) {
                if (allBeans.get(curIdx).getNumber().equals(number)) {
                    makeWrongBean(curIdx);
                    return true;
                }
            }
            curIdx += bigGridSize;
        }
        return false;
    }

    private boolean checkIfInSmallGrid(String number, int beanIdx, int x, int y) {
        int curIdx;
        int calX = x / bigGridX;
        int calY = y / bigGridY;
        for (int i = 0; i < bigGridX; ++i) {
            for (int j = 0; j < bigGridY; ++j) {
                curIdx = (calX * bigGridX + i) * bigGridSize + calY * bigGridY + j;
                if (curIdx != beanIdx) {
                    if (allBeans.get(curIdx).getNumber().equals(number)) {
                        makeWrongBean(curIdx);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int HowManyBeansFilled() {
        int amount = 0;
        for (SudokuBean sb : allBeans) {
            if (!sb.getNumber().equals(" ")) {
                amount++;
            }
        }
        return amount;
    }

    private void makeWrongBean(int idx) {
        wrongBeans.add(allBeans.get(idx));
        allBeans.get(idx).setBackgroundRed();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String value = (String) evt.getNewValue();
        SudokuBean sudokuBean = (SudokuBean) evt.getSource();
        if (!value.equals(" ")) {

            int beanIdx = sudokuBean.get_positionX() * bigGridSize + sudokuBean.get_positionY();
            if (checkIfInRow(value, beanIdx, sudokuBean.get_positionX())) {
                wrongBeans.add(sudokuBean);
            }
            if (checkIfInColumn(value, beanIdx, sudokuBean.get_positionY())) {
                wrongBeans.add(sudokuBean);
            }
            if (checkIfInSmallGrid(value, beanIdx, sudokuBean.get_positionX(), sudokuBean.get_positionY())) {
                wrongBeans.add(sudokuBean);
            }

            for (SudokuBean sb : wrongBeans) {
                sb.setBackgroundRed();
            }
        }
        if (value.equals(" ") && wrongBeans.contains(sudokuBean)) {
            wrongBeans.remove(sudokuBean);
            sudokuBean.setBackgroundNormal();
        }


        HashSet<SudokuBean> toremove = new HashSet<>();
        HashSet<SudokuBean> dupWrongBeans = new HashSet<>(wrongBeans);
        for (SudokuBean sb : dupWrongBeans) {
            int curIdx = sb.get_positionX() * bigGridSize + sb.get_positionY();
            if (!checkIfInRow(sb.getNumber(), curIdx, sb.get_positionX()) && !checkIfInColumn(sb.getNumber(), curIdx, sb.get_positionY()) && !checkIfInSmallGrid(sb.getNumber(), curIdx, sb.get_positionX(), sb.get_positionY())) {
                toremove.add(sb);
            }
        }

        for (SudokuBean sb : toremove) {
            wrongBeans.remove(sb);
            sb.setBackgroundNormal();
        }

        toremove.clear();

        if (wrongBeans.size() == 0) {
            System.out.print(HowManyBeansFilled() == bigGridSize * bigGridSize);
            System.out.print(bigGridSize * bigGridSize);
            System.out.print(HowManyBeansFilled());
            if (HowManyBeansFilled() == bigGridSize * bigGridSize) {
                for (SudokuBean sb : allBeans) {
                    sb.setBackgroundGreen();
                    sb.setLockAfterWin();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CONGRATULATIONS!");
                alert.setHeaderText(null);
                alert.setContentText("You won a game.");
                alert.showAndWait();
            }
        }

    }
}
