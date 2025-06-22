import javax.swing.*;
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
        JMenu fileMenu = createFileMenu();
        fileMenu.setToolTipText("Fájl menü [Alt+F]");
        JMenu viewMenu = createViewMenu();
        viewMenu.setToolTipText("Nézet menü [Alt+V]");
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(createHelpButton());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Fájl");

        fileMenu.add(createMenuItem("Mentés [Ctrl + S]", _ -> new FileManager(parent, model).saveLocal()));
        fileMenu.add(createMenuItem("Mentés másként [Ctrl + Shift + S]", _ -> new FileManager(parent, model).saveToFile()));

        fileMenu.add(createMenuItem("Betöltés [Ctrl + O]", _ -> {
            new FileManager(parent, model).loadLocal();
            parent.updateSliders();
            parent.repaint();
        }));

        fileMenu.add(createMenuItem("Betöltés máshonnan [Ctrl + Shift + O]", _ -> {
            new FileManager(parent, model).loadFromFile();
            parent.updateSliders();
            parent.repaint();
        }));

        fileMenu.add(createMenuItem("Törlés [Ctrl + DELETE]", _ -> new FileManager(parent, model).deleteLocal()));

        fileMenu.add(createMenuItem("Kilépés [Esc]", _ -> System.exit(0)));

        return fileMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Nézet");

        viewMenu.add(createMenuItem("Minden Randomizálása [Ctrl + R]", _ -> {
            model.Randomize(true, true);
            parent.updateSliders();
            parent.repaint();
        }));

        viewMenu.add(createMenuItem("Méretek randomizálása [Ctrl + T]", _ -> {
            model.Randomize(false, true);
            parent.updateSliders();
            parent.repaint();
        }));

        viewMenu.add(createMenuItem("Színek randomizálása [Ctrl + Z]", _ -> {
            model.Randomize(true, false);
            parent.updateSliders();
            parent.repaint();
        }));

        return viewMenu;
    }

    private JButton createHelpButton() {
        JButton helpButton = new JButton("Súgó");
        helpButton.setFocusable(false);
        helpButton.addActionListener(_ -> Fotel.help());
        helpButton.setBorderPainted(false);
        helpButton.setContentAreaFilled(false);
        helpButton.setFocusPainted(false);
        helpButton.setOpaque(false);
        helpButton.setToolTipText("Súgó [Ctrl + H]");
        return helpButton;
    }

    private JMenuItem createMenuItem(String text, ActionListener listener) {
        Icon icon = null;
        if (text.startsWith("Mentés [")) {
            icon = UIManager.getIcon("FileView.floppyDriveIcon");
        } else if (text.startsWith("Mentés másként")) {
            icon = UIManager.getIcon("FileView.hardDriveIcon");
        } else if (text.startsWith("Betöltés [")) {
            icon = UIManager.getIcon("FileView.directoryIcon");
        } else if (text.startsWith("Betöltés máshonnan")) {
            icon = UIManager.getIcon("FileView.directoryIcon");
        } else if (text.startsWith("Törlés")) {
            icon = UIManager.getIcon("FileChooser.newFolderIcon");
        } else if (text.startsWith("Kilépés")) {
            icon = UIManager.getIcon("InternalFrame.closeIcon");
        }
        JMenuItem item = new JMenuItem(text, icon);
        item.addActionListener(listener);
        return item;
    }
}
