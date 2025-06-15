import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuBuilder {
    private final Fotel parent;
    private final ArmChairModel model;

    public MenuBuilder(Fotel parent, ArmChairModel model) {
        this.parent = parent;
        this.model = model;
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Fájl");

        fileMenu.add(createMenuItem("Mentés [Ctrl + S]", e ->{
           new FileManager(parent, model).saveLocal();
        }));
        fileMenu.add(createMenuItem("Mentés másként [Ctrl + Shift + S]", e->{
            new FileManager(parent, model).saveToFile();
        }));

        fileMenu.add(createMenuItem("Betöltés [Ctrl + O]", e ->{
           new FileManager(parent, model).loadLocal();
           parent.updateSliders();
           parent.repaint();
        }));

        fileMenu.add(createMenuItem("Betöltés máshonnan [Ctrl + Shift + O]", e->{
            new FileManager(parent, model).loadFromFile();
            parent.updateSliders();
            parent.repaint();
        }));

        fileMenu.add(createMenuItem("Törlés [Ctrl + DELETE]", e->{
            new FileManager(parent, model).deleteLocal();
        }));

        fileMenu.add(createMenuItem("Kilépés [Esc]", e -> System.exit(0)));

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Nézet");

        viewMenu.add(createMenuItem("Minden Randomizálása [Ctrl + R]", e -> {
            model.Randomize(true, true);
            parent.updateSliders();
            parent.repaint();
        }));

        viewMenu.add(createMenuItem("Méretek randomizálása [Ctrl + T]", e ->{
            model.Randomize(false, true);
            parent.updateSliders();
            parent.repaint();
        }));

        viewMenu.add(createMenuItem("Színek randomizálása [Ctrl + Z]", e ->{
            model.Randomize(true, false);
            parent.updateSliders();
            parent.repaint();
        }));

        return viewMenu;
    }

    private JMenu createHelpMenu(){
        JMenu helpMenu = new JMenu("Súgó");
        helpMenu.add(createMenuItem("Súgó [Ctrl + H]", e -> parent.help()));

        return helpMenu;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }
}
