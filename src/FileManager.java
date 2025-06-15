import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            inputPanel.add(new JLabel("File Name:"));
            inputPanel.add(filenameField);

            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Return");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            panel.add(inputPanel, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Create and show a dialog
            JDialog dialog = new JDialog(parent, "Save File", true);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);

            // Save button action
            saveButton.addActionListener(e -> {
                String filename = filenameField.getText().trim();
                if (!filename.isEmpty()) {
                    File fileToSave = new File(filename);
                    if (!fileToSave.getName().toLowerCase().endsWith(".fotel")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".fotel");
                    }
                    try (PrintWriter writer = new PrintWriter(fileToSave)) {

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
                        JOptionPane.showMessageDialog(parent, "Mentés sikeres!", "Mentés",
                                JOptionPane.INFORMATION_MESSAGE);

                        dialog.dispose();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n" + ex.getMessage(), "Hiba",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n A fáljnév nem maradhat üressen.",
                            "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Cancel button action
            cancelButton.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);
        });
    }

    public void loadLocal() {
        JFrame frame = new JFrame("Fotel választó");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());

        String rootPath = System.getProperty("user.dir");
        File rootDir = new File(rootPath);

        // Create the dropdown
        JComboBox<String> dropdown = new JComboBox<>();

        // List .fotel files and add them to the dropdown
        File[] fotelFiles = rootDir.listFiles((dir, name) -> name.endsWith(".fotel"));
        if (fotelFiles != null) {
            for (File file : fotelFiles) {
                dropdown.addItem(file.getName().split(".fotel")[0]);
            }
        }

        // Create buttons
        JButton loadButton = new JButton("Betöltés");
        JButton returnButton = new JButton("Vissza");

        // Top panel with dropdown
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Válaszd ki a betöltendő modelt"));
        topPanel.add(dropdown);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(loadButton);
        bottomPanel.add(returnButton);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> {
            try {
                File fileToLoad = new File(dropdown.getSelectedItem().toString() + ".fotel");

                Scanner sc = new Scanner(fileToLoad);
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

                JOptionPane.showMessageDialog(parent, "Betöltés sikeres!", "Betöltés", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a betöltés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> frame.dispose());

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

        String rootPath = System.getProperty("user.dir");
        File rootDir = new File(rootPath);

        // Create the dropdown
        JComboBox<String> dropdown = new JComboBox<>();

        // List .fotel files and add them to the dropdown
        File[] fotelFiles = rootDir.listFiles((dir, name) -> name.endsWith(".fotel"));
        if (fotelFiles != null) {
            for (File file : fotelFiles) {
                dropdown.addItem(file.getName().split(".fotel")[0]);
            }
        }

        // Create buttons
        JButton deleteButton = new JButton("Törlés");
        JButton returnButton = new JButton("Vissza");

        // Top panel with dropdown
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Válaszd ki a törlendő modelt"));
        topPanel.add(dropdown);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteButton);
        bottomPanel.add(returnButton);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> {
            try {
                File fileToDelete = new File(dropdown.getSelectedItem().toString() + ".fotel");

                fileToDelete.delete();

                JOptionPane.showMessageDialog(parent, "Törlés sikeres!", "Törlés", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a törlés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    public void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Mentés fájlba");

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".fotel")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".fotel");
                }
                PrintWriter writer = new PrintWriter(fileToSave);

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
                JOptionPane.showMessageDialog(parent, "Mentés sikeres!", "Mentés", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a mentés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Betöltés fájlból");

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToLoad = fileChooser.getSelectedFile();
                if (!fileToLoad.getName().toLowerCase().endsWith(".fotel")) {
                    throw new Exception("A fájlnak .fotel kiterjesztésűnek kell lennie");
                }
                Scanner sc = new Scanner(fileToLoad);
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

                JOptionPane.showMessageDialog(parent, "Betöltés sikeres!", "Betöltés", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Hiba a betöltés közben:\n" + ex.getMessage(), "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void openSavePanel(JFrame parent) {
        // Create a custom panel

    }
}
