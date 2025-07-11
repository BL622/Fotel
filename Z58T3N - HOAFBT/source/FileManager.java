import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class FileManager {
    private final Fotel parent;
    private final ArmChairModel model;

    public FileManager(Fotel parent, ArmChairModel model) {
        this.parent = parent;
        this.model = model;
    }

    public void saveLocal() {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel(new BorderLayout(10, 10));

            JTextField filenameField = new JTextField(20);
            JPanel inputPanel = new JPanel();
            inputPanel.add(new JLabel("Fájl név:"));
            inputPanel.add(filenameField);

            JButton saveButton = new JButton("Mentés");
            JButton cancelButton = new JButton("Vissza");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            panel.add(inputPanel, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(parent, "Mentés", true);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);

            saveButton.addActionListener(_ -> {
                String filename = filenameField.getText().trim();
                if (!filename.isEmpty()) {
                    File fileToSave = new File(filename);
                    if (!fileToSave.getName().toLowerCase().endsWith(".fotel")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".fotel");
                    }
                    writeToFile(fileToSave);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n A fáljnév nem maradhat üressen.",
                            "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(_ -> dialog.dispose());

            dialog.setVisible(true);
        });
    }

    public void loadLocal() {
        JFrame frame = new JFrame("Fotel választó");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(parent);

        String rootPath = System.getProperty("user.dir");
        File rootDir = new File(rootPath);

        JComboBox<String> dropdown = new JComboBox<>();

        File[] fotelFiles = rootDir.listFiles((_, name) -> name.endsWith(".fotel"));
        if (fotelFiles != null) {
            for (File file : fotelFiles) {
                String baseName = file.getName().replaceFirst("\\.fotel$", "");
                dropdown.addItem(baseName);
            }
        }

        JButton loadButton = new JButton("Betöltés");
        JButton returnButton = new JButton("Vissza");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Válaszd ki a betöltendő modelt"));
        topPanel.add(dropdown);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(loadButton);
        bottomPanel.add(returnButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(_ -> {
            try {
                File fileToLoad = new File(Objects.requireNonNull(dropdown.getSelectedItem()) + ".fotel");

                loadFromFile(fileToLoad);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a betöltés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(_ -> frame.dispose());

        if (dropdown.getItemCount() == 0) {
            JOptionPane.showMessageDialog(parent, "Nincs betölthető modell!", "Figyelmeztetés",
                    JOptionPane.WARNING_MESSAGE);
            frame.dispose();
            return;
        }
        frame.setVisible(true);
    }

    public void deleteLocal() {
        JFrame frame = new JFrame("Fotel választó");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(parent);

        String rootPath = System.getProperty("user.dir");
        File rootDir = new File(rootPath);

        JComboBox<String> dropdown = new JComboBox<>();

        File[] fotelFiles = rootDir.listFiles((_, name) -> name.endsWith(".fotel"));
        if (fotelFiles != null) {
            for (File file : fotelFiles) {
                String baseName = file.getName().replaceFirst("\\.fotel$", "");
                dropdown.addItem(baseName);
            }
        }

        JButton deleteButton = new JButton("Törlés");
        JButton returnButton = new JButton("Vissza");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Válaszd ki a törlendő modelt"));
        topPanel.add(dropdown);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteButton);
        bottomPanel.add(returnButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(_ -> {
            try {
                String selected = (String) dropdown.getSelectedItem();
                if (selected == null)
                    return;
                File fileToDelete = new File(System.getProperty("user.dir"), selected + ".fotel");
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    dropdown.removeItem(selected);
                    JOptionPane.showMessageDialog(parent, "Törlés sikeres!", "Törlés", JOptionPane.INFORMATION_MESSAGE);
                    if (dropdown.getItemCount() == 0) {
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(parent,
                            "A fájl törlése nem sikerült!\nTeljes elérési út: " + fileToDelete.getAbsolutePath(),
                            "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a törlés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(_ -> frame.dispose());

        if (dropdown.getItemCount() == 0) {
            JOptionPane.showMessageDialog(parent, "Nincs törölhető modell!", "Figyelmeztetés",
                    JOptionPane.WARNING_MESSAGE);
            frame.dispose();
            return;
        }

        frame.setVisible(true);
    }

    public void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Mentés fájlba");

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".fotel")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".fotel");
            }
            writeToFile(fileToSave);
        }
    }

    public void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Betöltés fájlból");

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            if (!fileToLoad.getName().toLowerCase().endsWith(".fotel")) {
                JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n A fájl nem .fotel kiterjesztésű.",
                        "Hiba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            loadFromFile(fileToLoad);
        }
    }

    private void writeToFile(File fileToSave) {
        try (PrintWriter writer = new PrintWriter(fileToSave);) {
            writer.print(model.getWidth() + ";");
            writer.print(model.getHeight() + ";");
            writer.print(model.getDepth() + ";");
            writer.print(model.getBaseColor().getRed() + ";" + model.getBaseColor().getGreen() + ";"
                    + model.getBaseColor().getBlue() + ";");
            writer.print(model.getCushionColor().getRed() + ";" + model.getCushionColor().getGreen() + ";"
                    + model.getCushionColor().getBlue() + ";");
            writer.print(model.getLegColor().getRed() + ";" + model.getLegColor().getGreen() + ";"
                    + model.getLegColor().getBlue() + ";");
            writer.print(model.getPillowColor().getRed() + ";" + model.getPillowColor().getGreen() + ";"
                    + model.getPillowColor().getBlue() + "\n");
            writer.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n" + e.getMessage(), "Hiba",
                    JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(parent, "Mentés sikeres!", "Mentés", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadFromFile(File fileToLoad) {
        try (Scanner sc = new Scanner(fileToLoad)) {
            String[] data = sc.nextLine().split(";");
            model.setWidth(Integer.parseInt(data[0]));
            model.setHeight(Integer.parseInt(data[1]));
            model.setDepth(Integer.parseInt(data[2]));
            model.setBaseColor(
                    new Color(Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5])));
            model.setCushionColor(
                    new Color(Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8])));
            model.setLegColor(
                    new Color(Integer.parseInt(data[9]), Integer.parseInt(data[10]), Integer.parseInt(data[11])));
            model.setPillowColor(
                    new Color(Integer.parseInt(data[12]), Integer.parseInt(data[13]), Integer.parseInt(data[14])));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Hiba a betöltés közben:\n" + ex.getMessage(), "Hiba",
                    JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(parent, "Betöltés sikeres!", "Betöltés", JOptionPane.INFORMATION_MESSAGE);
        parent.updateView();
    }
}
