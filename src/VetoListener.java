import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Vector;

public class VetoListener implements VetoableChangeListener {
    private int bigGridX;
    private int bigGridY;
    private String puzzle;
    private int bigGridSize;
    private Vector<String> acceptableValues;

    VetoListener(Vector<String> acceptableVals, int x, int y, String sPuzzle) {
        bigGridX = x;
        bigGridY = y;
        puzzle = sPuzzle;
        bigGridSize = bigGridX * bigGridY;
        acceptableValues = acceptableVals;
    }

    //return true if there is already this number in row
    public boolean checkRow(String number, int x, int y) {
        System.out.println("checking row...");
        int curIdx = x * bigGridSize;
        for (int i = 0; i < bigGridSize; ++i) {
            if (puzzle.substring(curIdx, curIdx + 1).equals(number)) {
                return true;
            }
            curIdx++;
        }
        System.out.println("all good");
        return false;
    }

    public boolean checkColumn(String number, int x, int y) {
        System.out.println("checking column...");
        int curIdx = y;
        for (int i = 0; i < bigGridSize; ++i) {
            if (puzzle.substring(curIdx, curIdx + 1).equals(number)) {
                return true;
            }
            curIdx += bigGridSize;
        }
        System.out.println("all good");
        return false;
    }

    public boolean checkSmallGrid(String number, int x, int y) {
        System.out.println("checking grid...");
        int curIdx = 0;
        int calX = x / bigGridX;
        int calY = y / bigGridY;
        for (int i = 0; i < bigGridX; ++i) {
            for (int j = 0; j < bigGridY; ++j) {
                curIdx = (calX * bigGridX + i) * bigGridSize + calY * bigGridY + j;
                if (puzzle.substring(curIdx, curIdx + 1).equals(number)) {
                    return true;
                }
            }
        }
        System.out.println("all good");
        return false;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        String newVal = (String) evt.getNewValue();
        SudokuBean sBean = (SudokuBean) evt.getSource();
        if (newVal.equals(" ")) {
            return; //dwa razy, bo stara wartosc pusta
        }
        newVal = newVal.trim();
        if (acceptableValues.contains(newVal)) {
            if (checkRow(newVal, sBean.get_positionX(), sBean.get_positionY()) || checkColumn(newVal, sBean.get_positionX(), sBean.get_positionY()) || checkSmallGrid(newVal, sBean.get_positionX(), sBean.get_positionY())) {
                throw new PropertyVetoException("the same number is already in row, column or small grid.", evt);
            }
        } else {
            throw new PropertyVetoException("it is not in range of acceptables values.", evt);
        }
    }
}
