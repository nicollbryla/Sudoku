import javafx.geometry.Pos;
import javafx.scene.control.TextField;

import java.beans.*;
import java.io.Serializable;

public class SudokuBean implements Serializable {
    private TextField textField;
    private String number;
    private int _x;
    private int _y;
    private VetoableChangeSupport vetos = new VetoableChangeSupport(this);
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
    private boolean locked;
    private String normalBean = "-fx-border-color: black; -fx-border-width: 1;-fx-text-fill: black; -fx-font-size: 18;";
    private String lockedbean = " -fx-font-weight: bold;" + normalBean;


    SudokuBean() {
        new SudokuBean(" ", 0, 0, false);
    }

    SudokuBean(String nNumber, int x, int y, boolean iflocked) {
        textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        textField.setStyle(normalBean);
        number = nNumber;
        textField.setText(number);
        _x = x;
        _y = y;
        if (iflocked) {
            setLocked();
        }
        textField.setMinSize(50, 50);
        textField.setMaxSize(50, 50);
    }

    private void setLocked() {
        locked = true;
        textField.setEditable(false);
        textField.setStyle(lockedbean);
    }

    public void setLockAfterWin() {
        setLocked();
        textField.setStyle("-fx-background-color: #00cc00; " + lockedbean);
    }

    public int get_positionX() {
        return _x;
    }

    public int get_positionY() {
        return _y;
    }

    public TextField getTextField() {
        return textField;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String newValue) throws PropertyVetoException {
        String oldValue = this.number;
        vetos.fireVetoableChange("number", oldValue, newValue);
        number = newValue;
        textField.setText(newValue);
        propertyChange.firePropertyChange("number", oldValue, newValue);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    public synchronized void addVetoableChangeListener(VetoableChangeListener l) {
        vetos.addVetoableChangeListener(l);
    }

    public void setBackgroundNormal() {
        textField.setStyle(normalBean);
    }


    public void setBackgroundRed() {
        textField.setStyle("-fx-background-color: #ff1a1a; " + normalBean);
    }

    public void setBackgroundGreen() {
        if (locked) {
            textField.setStyle("-fx-background-color: #00cc00; " + lockedbean);
        } else {
            textField.setStyle(normalBean + "-fx-background-color: #00cc00;");
        }
    }
}
