import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Fotel::new);
    }
}