import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class Fotel extends JFrame {
    private final ArmChairModel model;
    private JSlider widthSlider;
    private JSlider heightSlider;
    private JSlider depthSlider;
    private final List<JSlider> sliders = new ArrayList<>();
    private int focusedSliderIndex = 0;
    public int setFocusOnView = 0;
    public int focusedViewIndex = 0;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception _) {

        }
    }

    public Fotel() {
        model = new ArmChairModel();

        setTitle("Vetület Modell – Fotel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
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

        showStartupDialog();
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
        
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);
        
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

        
        setupFocusHandling();
        getContentPane().setFocusable(true);
        getContentPane().requestFocusInWindow();

        setVisible(true);

        
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

        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(200, 0));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Váltás nézetek között"));

        JButton frontButton = new JButton("Elölnézet [1]");
        frontButton.addActionListener(_ -> {
            focusedViewIndex = 0;
            updateView();
        });

        JButton sideButton = new JButton("Oldalnézet [2]");
        sideButton.addActionListener(_ -> {
            focusedViewIndex = 1;
            updateView();
        });

        JButton topButton = new JButton("Felülnézet [3]");
        topButton.addActionListener(_ -> {
            focusedViewIndex = 2;
            updateView();
        });

        JButton backToNormal = new JButton("Vissza [0]");
        backToNormal.addActionListener(_ -> {
            setFocusOnView = 0;
            updateView();
        });

        controlPanel.add(frontButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(sideButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(topButton);
        controlPanel.add(Box.createVerticalGlue());
        backToNormal.setPreferredSize(new Dimension(250, 40));
        backToNormal.setMaximumSize(new Dimension(250, 40));
        backToNormal.setMinimumSize(new Dimension(250, 40));
        backToNormal.setAlignmentX(JButton.CENTER_ALIGNMENT);
        backToNormal.setBackground(new Color(220, 53, 69));
        backToNormal.setForeground(Color.BLACK);
        backToNormal.setFont(backToNormal.getFont().deriveFont(Font.BOLD, 16f));
        backToNormal.setFocusPainted(false);
        backToNormal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 53, 69), 2, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        controlPanel.add(backToNormal);

        add(controlPanel, BorderLayout.WEST);


        int panelWidth = controlPanel.getPreferredSize().width;
        for (Component comp : controlPanel.getComponents()) {
            if (comp instanceof JButton) {
                Dimension buttonSize = new Dimension(panelWidth, comp.getPreferredSize().height);
                comp.setPreferredSize(buttonSize);
                comp.setMaximumSize(buttonSize);
                comp.setMinimumSize(buttonSize);
                ((JButton)comp).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

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

        
        widthSlider = new JSlider(160, 300, model.getWidth());
        heightSlider = new JSlider(160, 250, model.getHeight());
        depthSlider = new JSlider(160, 240, model.getDepth());


        try {
            widthSlider.setUI((javax.swing.plaf.SliderUI)Class.forName("com.sun.java.swing.plaf.windows.WindowsSliderUI").getConstructor(javax.swing.JSlider.class).newInstance(widthSlider));
            heightSlider.setUI((javax.swing.plaf.SliderUI)Class.forName("com.sun.java.swing.plaf.windows.WindowsSliderUI").getConstructor(javax.swing.JSlider.class).newInstance(heightSlider));
            depthSlider.setUI((javax.swing.plaf.SliderUI)Class.forName("com.sun.java.swing.plaf.windows.WindowsSliderUI").getConstructor(javax.swing.JSlider.class).newInstance(depthSlider));
        } catch (Exception _) {

        }

        widthSlider.setBorder(BorderFactory.createTitledBorder("Szélesség [1]"));
        heightSlider.setBorder(BorderFactory.createTitledBorder("Magasság [2]"));
        depthSlider.setBorder(BorderFactory.createTitledBorder("Mélység [3]"));

        widthSlider.addChangeListener(_ -> {
            model.setWidth(widthSlider.getValue());
            repaint();
        });

        heightSlider.addChangeListener(_ -> {
            model.setHeight(heightSlider.getValue());
            repaint();
        });

        depthSlider.addChangeListener(_ -> {
            model.setDepth(depthSlider.getValue());
            repaint();
        });

        
        widthSlider.setFocusable(false);
        heightSlider.setFocusable(false);
        depthSlider.setFocusable(false);

        controlPanel.add(widthSlider);
        controlPanel.add(heightSlider);
        controlPanel.add(depthSlider);


        sliders.clear();
        sliders.add(widthSlider);
        sliders.add(heightSlider);
        sliders.add(depthSlider);


        updateSliderFocusVisual();

        
        addColorButton(controlPanel, "Alapszín [4]", model.getBaseColor(),
            model::setBaseColor);
        controlPanel.add(Box.createVerticalStrut(10));
        addColorButton(controlPanel, "Párna szín [5]", model.getCushionColor(),
            model::setCushionColor);
        controlPanel.add(Box.createVerticalStrut(10));
        addColorButton(controlPanel, "Láb szín [6]", model.getLegColor(),
            model::setLegColor);
        controlPanel.add(Box.createVerticalStrut(10));
        addColorButton(controlPanel, "Díszpárna szín [7]", model.getPillowColor(),
            model::setPillowColor);
        controlPanel.add(Box.createVerticalGlue());

        JButton addFocusButton = new JButton("Fókusz [8]");
        addFocusButton.setPreferredSize(new Dimension(250, 40));
        addFocusButton.setMaximumSize(new Dimension(250, 40));
        addFocusButton.setMinimumSize(new Dimension(250, 40));
        addFocusButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        addFocusButton.setBackground(new Color(0, 120, 215));
        addFocusButton.setForeground(Color.BLACK);
        addFocusButton.setFont(addFocusButton.getFont().deriveFont(Font.BOLD, 16f));
        addFocusButton.setFocusPainted(false);
        addFocusButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        addFocusButton.addActionListener(_ -> {
            setFocusOnView = 1;
            updateView();
        });
        controlPanel.add(addFocusButton);

        return controlPanel;
    }


    private void styleSidePanelButton(JButton button) {
        int width = 250;
        int height = 20;
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
    }

    private void addColorButton(JPanel panel, String text, Color currentColor, Consumer<Color> colorConsumer) {
        JButton button = new JButton(text);
        styleSidePanelButton(button);
        button.addActionListener(_ -> colorChooser(currentColor, colorConsumer, text + "színt"));
        panel.add(button);
    }

    public void colorChooser(Color currentColor, Consumer<Color> colorConsumer, String text) {
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
                    "Válassz" + text,
                    true,
                    colorChooser,
                _ -> {
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
        SwingUtilities.invokeLater(() -> slider.setValue(Math.min(slider.getValue() + 1, slider.getMaximum())));
    }

    public void decreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> slider.setValue(Math.max(slider.getValue() - 1, slider.getMinimum())));
    }

    public void largeIncreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> slider.setValue(Math.min(slider.getValue() + 10, slider.getMaximum())));
    }

    public void largeDecreaseFocusedSlider() {
        JSlider slider = getFocusedSlider();
        SwingUtilities.invokeLater(() -> slider.setValue(Math.max(slider.getValue() - 10, slider.getMinimum())));
    }

    public void updateSliders() {
        widthSlider.setValue(model.getWidth());
        heightSlider.setValue(model.getHeight());
        depthSlider.setValue(model.getDepth());
    }

    public static void help() {
        JFrame helpWindow = new JFrame("Súgó");
        helpWindow.setSize(800, 540);
        helpWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            helpWindow.setIconImage(new ImageIcon("icons/help.png").getImage());
        } catch (Exception _) {
            
        }

        JTextPane helpText = new JTextPane();
        helpText.setContentType("text/html");
        helpText.setText(
            """
            <html><body style='font-family:sans-serif; font-size:15px; background:#f8f9fa; color:#222; padding:18px;'>
            <div style='border-radius:12px; border:1px solid #d1d5db; background:#fff; box-shadow:0 2px 8px #0001; padding:24px;'>
            <h1 style='color:#1976d2; margin-top:0;'>Fotel Súgó</h1>
            <h2 style='color:#388e3c;'>Névjegy</h2>
            <b>Készítette:</b><br>Balics Attila Ádám - Z58T3N<br>Balogh Levente HOAFBT<br><br>
            <h2 style='color:#388e3c;'>Használati útmutató</h2>
            <ul style='margin-bottom:0.5em;'>
                <li><b>[1]</b> Szélesség állítása a Fel-Le, Jobb-Bal nyilakkal, <b>Shift</b>-tel nagyobb lépés.</li>
                <li><b>[2]</b> Magasság állítása a Fel-Le, Jobb-Bal nyilakkal, <b>Shift</b>-tel nagyobb lépés.</li>
                <li><b>[3]</b> Mélység állítása a Fel-Le, Jobb-Bal nyilakkal, <b>Shift</b>-tel nagyobb lépés.</li>
                <li><b>[4]</b> Alapszín állítása.</li>
                <li><b>[5]</b> Párna szín állítása.</li>
                <li><b>[6]</b> Láb szín állítása.</li>
                <li><b>[7]</b> Díszpárna szín állítása.</li>
                <li><b>[8]</b> Nézetek fókuszba helyezése
                    <ul>
                        <li><b>[1]</b> Elölnézet fókuszba helyezése</li>
                        <li><b>[2]</b> Oldalnézet fókuszba helyezése</li>
                        <li><b>[3]</b> Felülnézet fókuszba helyezése</li>
                        <li><b>[0]</b> Vissza szerkesztés módba</li>
                    </ul>
                </li>
            </ul>
            <h2 style='color:#388e3c;'>Gyorsbillentyűk</h2>
            <ul style='margin-bottom:0.5em;'>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + S</span> Fotel mentése helyileg</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + Shift + S</span> Fotel mentése tetszőleges helyre</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + O</span> Fotel betöltése</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + Shift + O</span> Fotel betöltése tetszőleges helyről</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + Del</span> Fotel törlése</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + Z</span> Fotel randomizálása szín szerint</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + T</span> Fotel randomizálása méret szerint</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + R</span> Fotel randomizálása minden szerint</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Ctrl + H</span> Súgó</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Alt + F</span> Fájl menü megnyitása</li>
                <li><span style='font-family:monospace; background:#e3f2fd; padding:2px 6px; border-radius:5px;'>Alt + V</span> Nézet menü megnyitása</li>
            </ul>
            <div style='text-align:right; color:#888; font-size:12px; margin-top:2em;'>© 2025 Fotel</div>
            </div></body></html>
            """
        );
        helpText.setEditable(false);
        helpText.setCaretPosition(0);
        helpText.setMargin(new Insets(10, 10, 10, 10));

        helpWindow.add(new JScrollPane(helpText));
        helpWindow.setVisible(true);
    }

    private void showStartupDialog() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        boolean dontShow = prefs.getBoolean("dontShowStartupDialog", false);
        if (dontShow) return;

        JCheckBox dontShowAgain = new JCheckBox("Ne mutasd többet");
        Object[] params = {
            "<html><b>Üdvözlünk a Fotel alkalmazásban!</b><br>A csúszkák állításához használd a Fel-Le, Jobb-Bal nyilakat és a<b>Shift</b>-et a nagyobb lépéshez.</html>",
            dontShowAgain
        };
        JOptionPane.showMessageDialog(
            this,
            params,
            "Üdvözlet",
            JOptionPane.INFORMATION_MESSAGE
        );
        if (dontShowAgain.isSelected()) {
            prefs.putBoolean("dontShowStartupDialog", true);
        }
    }
}
