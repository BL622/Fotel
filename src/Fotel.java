import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Fotel extends JFrame {
    private ArmChairModel model;
    private JSlider widthSlider;
    private JSlider heightSlider;
    private JSlider depthSlider;
    private List<JSlider> sliders = new ArrayList<>();
    private int focusedSliderIndex = 0;
    public int setFocusOnView = 0;
    public int focusedViewIndex = 0;

    public Fotel() {
        model = new ArmChairModel();

        setTitle("Vetület Modell – Fotel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu
        MenuBuilder menuBuilder = new MenuBuilder(this, model);
        setJMenuBar(menuBuilder.buildMenuBar());

        switch (setFocusOnView) {
            case 0:
                normalViewMenu();
                break;
            case 1:
                focusViewMenu();
                break;
        }
    }

    public void updateView() {
        getContentPane().removeAll();

        switch (setFocusOnView) {
            case 0:
                normalViewMenu();
                break;
            case 1:
                focusViewMenu();
                break;
        }
        new KeyBindingManager(this, model).setupKeyBindings();

        revalidate();
        repaint();
    }


    private void normalViewMenu(){
        // Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);
        // Views
        JPanel gridPanel = new JPanel(new GridLayout(2, 2));
        gridPanel.add(new ArmChairViewPanel("Elölnézet", model) {
            @Override
            protected void drawView(Graphics g) {
                drawFrontView(g);
            }
        });

        gridPanel.add(new ArmChairViewPanel("Oldalnézet", model) {
            @Override
            protected void drawView(Graphics g) {
                drawSideView(g);
            }
        });

        gridPanel.add(new ArmChairViewPanel("Felülnézet", model) {
            @Override
            protected void drawView(Graphics g) {
                drawTopView(g);
            }
        });

        gridPanel.add(new ArmChairViewPanel("Méretek", model) {
            @Override
            protected void drawView(Graphics g) {
                drawSizes(g);
            }
        });

        add(gridPanel, BorderLayout.CENTER);

        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Focus setup
        setupFocusHandling();
        getContentPane().setFocusable(true);
        getContentPane().requestFocusInWindow();

        setVisible(true);

        // Setup key bindings
        new KeyBindingManager(this, model).setupKeyBindings();
    }

    private void focusViewMenu() {
        setTitle("Fókuszált Nézet – Fotel");

        JPanel gridPanel = new JPanel(new GridLayout(1, 1));

        ArmChairViewPanel viewPanel = new ArmChairViewPanel("Nézet", model) {
            @Override
            protected void drawView(Graphics g) {
                switch (focusedViewIndex) {
                    case 0:
                        drawFrontView(g);
                        break;
                    case 1:
                        drawSideView(g);
                        break;
                    case 2:
                        drawTopView(g);
                        break;
                }
            }
        };

        gridPanel.add(viewPanel);
        add(gridPanel, BorderLayout.CENTER);

        // Control Panel on the left
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(200, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Váltás nézetek között"));

        JButton frontButton = new JButton("Elölnézet [1]");
        frontButton.addActionListener(e -> {
            focusedViewIndex = 0;
            updateView();
        });

        JButton sideButton = new JButton("Oldalnézet [2]");
        sideButton.addActionListener(e -> {
            focusedViewIndex = 1;
            updateView();
        });

        JButton topButton = new JButton("Felülnézet [3]");
        topButton.addActionListener(e -> {
            focusedViewIndex = 2;
            updateView();
        });

        JButton backToNormal = new JButton("Vissza [0]");
        backToNormal.addActionListener(e -> {
            setFocusOnView = 0;
            updateView();
        });

        controlPanel.add(frontButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(sideButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(topButton);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(backToNormal);

        add(controlPanel, BorderLayout.WEST);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Beállítások"));
        controlPanel.setPreferredSize(new Dimension(250, 0));
        controlPanel.setFocusable(false);

        // Sliders
        widthSlider = new JSlider(160, 300, model.getWidth());
        heightSlider = new JSlider(160, 250, model.getHeight());
        depthSlider = new JSlider(160, 240, model.getDepth());

        widthSlider.setBorder(BorderFactory.createTitledBorder("Szélesség [1]"));
        heightSlider.setBorder(BorderFactory.createTitledBorder("Magasság [2]"));
        depthSlider.setBorder(BorderFactory.createTitledBorder("Mélység [3]"));

        widthSlider.addChangeListener(e -> {
            model.setWidth(widthSlider.getValue());
            repaint();
        });

        heightSlider.addChangeListener(e -> {
            model.setHeight(heightSlider.getValue());
            repaint();
        });

        depthSlider.addChangeListener(e -> {
            model.setDepth(depthSlider.getValue());
            repaint();
        });

        // Disable focus for sliders
        widthSlider.setFocusable(false);
        heightSlider.setFocusable(false);
        depthSlider.setFocusable(false);

        controlPanel.add(widthSlider);
        controlPanel.add(heightSlider);
        controlPanel.add(depthSlider);

        // Add to sliders list
        sliders.clear();
        sliders.add(widthSlider);
        sliders.add(heightSlider);
        sliders.add(depthSlider);

        // Set initial focus
        updateSliderFocusVisual();

        // Color pickers
        addColorButton(controlPanel, "Alapszín [4]", model.getBaseColor(),
                color -> model.setBaseColor(color));
        addColorButton(controlPanel, "Párna szín [5]", model.getCushionColor(),
                color -> model.setCushionColor(color));
        addColorButton(controlPanel, "Láb szín [6]", model.getLegColor(),
                color -> model.setLegColor(color));
        addColorButton(controlPanel, "Díszpárna szín [7]", model.getPillowColor(),
                color -> model.setPillowColor(color));

        JButton addFocusButton = new JButton("Fókusz [8]");
        addFocusButton.addActionListener(e -> {
            setFocusOnView = 1;
            updateView();
        });

        controlPanel.add(addFocusButton);

        return controlPanel;
    }


    private void addColorButton(JPanel panel, String text, Color currentColor, Consumer<Color> colorConsumer) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser(currentColor);

            AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
            for (AbstractColorChooserPanel p : panels) {
                if (!p.getDisplayName().equals("Swatches") && !p.getDisplayName().equals("RGB")) {
                    colorChooser.removeChooserPanel(p);
                }
            }

            colorChooser.setPreviewPanel(new JPanel());

            JDialog dialog = JColorChooser.createDialog(
                    this,
                    "Válassz színt",
                    true,
                    colorChooser,
                    ev -> {
                        Color chosen = colorChooser.getColor();
                        if (chosen != null) {
                            colorConsumer.accept(chosen);
                            repaint();
                        }
                    },
                    null
            );

            colorChooser.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
        panel.add(button);
    }

    private void setupFocusHandling() {
        getContentPane().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateSliderFocusVisual();
            }
        });
    }

    public JSlider getFocusedSlider() {
        return sliders.get(focusedSliderIndex);
    }

    public void setFocusedSlider(int index) {
        if (index >= 0 && index < sliders.size()) {
            focusedSliderIndex = index;
            updateSliderFocusVisual();
            getContentPane().requestFocusInWindow();
        }
    }

    private void updateSliderFocusVisual() {
        for (int i = 0; i < sliders.size(); i++) {
            JSlider slider = sliders.get(i);
            if (i == focusedSliderIndex) {
                slider.setBackground(new Color(200, 230, 255));
                slider.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLUE, 2),
                        BorderFactory.createTitledBorder(
                                i == 0 ? "Szélesség [1]" :
                                        i == 1 ? "Magasság [2]" : "Mélység [3]")
                ));
            } else {
                slider.setBackground(null);
                slider.setBorder(BorderFactory.createTitledBorder(
                        i == 0 ? "Szélesség [1]" :
                                i == 1 ? "Magasság [2]" : "Mélység [3]"));
            }
        }
    }

    public void increaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> {
            slider.setValue(Math.min(slider.getValue() + 1, slider.getMaximum()));
        });
    }

    public void decreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> {
            slider.setValue(Math.max(slider.getValue() - 1, slider.getMinimum()));
        });
    }

    public void largeIncreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> {
            slider.setValue(Math.min(slider.getValue() + 10, slider.getMaximum()));
        });
    }

    public void largeDecreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> {
            slider.setValue(Math.max(slider.getValue() - 10, slider.getMinimum()));
        });
    }

    public void updateSliders() {
        widthSlider.setValue(model.getWidth());
        heightSlider.setValue(model.getHeight());
        depthSlider.setValue(model.getDepth());
    }

    public void openColorChooser(String text, Color currentColor, Consumer<Color> colorConsumer) {

        JColorChooser colorChooser = new JColorChooser(currentColor);

        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel p : panels) {
            if (!p.getDisplayName().equals("Swatches") && !p.getDisplayName().equals("RGB")) {
                colorChooser.removeChooserPanel(p);
            }
        }

        colorChooser.setPreviewPanel(new JPanel());

        JDialog dialog = JColorChooser.createDialog(
                this,
                "Válassz " + text,
                true,
                colorChooser,
                ev -> {
                    Color chosen = colorChooser.getColor();
                    if (chosen != null) {
                        colorConsumer.accept(chosen);
                        repaint();
                    }
                },
                null
        );

        colorChooser.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void help() {
        JFrame helpWindow = new JFrame("Súgó");
        helpWindow.setSize(600, 400);
        helpWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this window

        // Create content for the help window
        JTextArea helpText = new JTextArea(
                "Névjegy:\n" +
                        "Készítette:\nBalics Attila Ádám - Z58T3N\nBalogh Levente HOAFBT\n\n\n" +
                        "Használati útmutató:\n" +
                        "[1] Szélesség állítása a Fel-Le, Jobb-Bal nyilakkal. illetve ugyan ezek a Shift + Fel-Le, Jobb-Bal nyilakkal nagyobb lépéselhez.\n" +
                        "[2] Magasság állítása a Fel-Le, Jobb-Bal nyilakkal. illetve ugyan ezek a Shift + Fel-Le, Jobb-Bal nyilakkal nagyobb lépéselhez.\n" +
                        "[3] Mélység állítása a Fel-Le, Jobb-Bal nyilakkal. illetve ugyan ezek a Shift + Fel-Le, Jobb-Bal nyilakkal nagyobb lépéselhez.\n" +
                        "[4] Alapszín Állítása.\n" +
                        "[5] Párna szín Állítása.\n" +
                        "[6] Láb szín Állítása.\n" +
                        "[7] Díszpárna szín Állítása.\n" +
                        "[Ctrl + S] Fotel mentése helyileg.\n" +
                        "[Ctrl + Shift + S] Fotel mentése tetszőleges helyre.\n" +
                        "[Ctrl + O] Fotel betöltése.\n" +
                        "[Ctrl + Shift + O] Fotel betöltése tetszőleges helyről.\n"+
                        "[Ctrl + Del] Fotel törlése.\n"+
                        "[Ctrl + Z] Fotel randomizálása szín szerint.\n" +
                        "[Ctrl + T] Fotel randomizálása méret szerint.\n" +
                        "[Ctrl + R] Fotel randomizálása minden szerint.\n" +
                        "[Ctrl + H] Súgó."
        );
        helpText.setWrapStyleWord(true);
        helpText.setLineWrap(true);
        helpText.setEditable(false);
        helpText.setMargin(new Insets(10, 10, 10, 10));

        // Add to scroll pane and frame
        helpWindow.add(new JScrollPane(helpText));

        // Show the window
        helpWindow.setVisible(true);
    }
}
