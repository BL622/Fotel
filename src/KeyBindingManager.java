import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class KeyBindingManager {
    private final Fotel parent;
    private final ArmChairModel model;

    public KeyBindingManager(Fotel parent, ArmChairModel model) {
        this.parent = parent;
        this.model = model;
    }

    public void setupKeyBindings() {
        JComponent contentPane = (JComponent) parent.getContentPane();
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();

        // File operations
        addKeyBinding(inputMap, actionMap, "saveAction",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
                () -> new FileManager(parent, model).saveLocal());

        addKeyBinding(inputMap, actionMap, "loadAction",
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK),
                () -> {
                    new FileManager(parent, model).loadLocal();
                    parent.updateSliders();
                    parent.repaint();
                });

        addKeyBinding(inputMap, actionMap, "deleteAction",
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK),
                () -> {
                    new FileManager(parent, model).deleteLocal();
                });

        addKeyBinding(inputMap, actionMap, "saveOutAction",
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                () -> new FileManager(parent, model).saveToFile());

        addKeyBinding(inputMap, actionMap, "loadOutAction",
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                () -> {
                    new FileManager(parent, model).loadFromFile();
                    parent.updateSliders();
                    parent.repaint();
                });

        // Randomize operations
        addKeyBinding(inputMap, actionMap, "randomize",
                KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK),
                () -> {
                    model.Randomize(true, true);
                    parent.updateSliders();
                    parent.repaint();
                });

        addKeyBinding(inputMap, actionMap, "randomizeColor",
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
                () -> {
                    model.Randomize(true, false);
                    parent.updateSliders();
                    parent.repaint();
                });

        addKeyBinding(inputMap, actionMap, "randomizeSize",
                KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK),
                () -> {
                    model.Randomize(false, true);
                    parent.updateSliders();
                    parent.repaint();
                });

        // Exit
        addKeyBinding(inputMap, actionMap, "exit",
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                () -> System.exit(0));

        addKeyBinding(inputMap, actionMap, "help",
                KeyStroke.getKeyStroke(KeyEvent.VK_H,KeyEvent.CTRL_DOWN_MASK),
                () -> parent.help());




        switch (parent.setFocusOnView){
            case 0:
                normalViewKeyBindings(inputMap, actionMap);
                break;
            case 1:
                focusViewKeyBindings(inputMap, actionMap);
                break;

        }


    }

    private void normalViewKeyBindings(InputMap inputMap, ActionMap actionMap) {
        // Slider controls
        addKeyBinding(inputMap, actionMap, "focusWidth",
                KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),
                () -> parent.setFocusedSlider(0));

        addKeyBinding(inputMap, actionMap, "focusHeight",
                KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),
                () -> parent.setFocusedSlider(1));

        addKeyBinding(inputMap, actionMap, "focusDepth",
                KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),
                () -> parent.setFocusedSlider(2));

        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_RIGHT, parent::increaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_LEFT, parent::decreaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_UP, parent::increaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_DOWN, parent::decreaseFocusedSlider);

        // Shift-modified arrow keys
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK, parent::largeIncreaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK, parent::largeDecreaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK, parent::largeIncreaseFocusedSlider);
        addArrowKeyBinding(inputMap, actionMap, KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK, parent::largeDecreaseFocusedSlider);

        addKeyBinding(inputMap, actionMap, "openBaseColor",
                KeyStroke.getKeyStroke(KeyEvent.VK_4, 0),
                ()->{
                    parent.openColorChooser("alapszínt", model.getBaseColor(), color -> model.setBaseColor(color));
                });
        addKeyBinding(inputMap, actionMap, "openCushionColor",
                KeyStroke.getKeyStroke(KeyEvent.VK_5, 0),
                ()->{
                    parent.openColorChooser("párna színt", model.getCushionColor(), color -> model.setCushionColor(color));
                });
        addKeyBinding(inputMap, actionMap, "openLegColor",
                KeyStroke.getKeyStroke(KeyEvent.VK_6, 0),
                ()->{
                    parent.openColorChooser("láb színt", model.getLegColor(), color -> model.setLegColor(color));
                });
        addKeyBinding(inputMap, actionMap, "openPillowColor",
                KeyStroke.getKeyStroke(KeyEvent.VK_7, 0),
                ()->{
                    parent.openColorChooser("díszpárna színt", model.getPillowColor(), color -> model.setPillowColor(color));
                });

        addKeyBinding(inputMap, actionMap, "changeToFocusedView",
                KeyStroke.getKeyStroke(KeyEvent.VK_8, 0),
                () ->{parent.setFocusOnView = 1;
                    parent.updateView();
                });
    }

    private void focusViewKeyBindings(InputMap inputMap, ActionMap actionMap) {
        addKeyBinding(inputMap, actionMap, "setFocusOnFrontView",
                KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),
                () ->{parent.focusedViewIndex = 0;
                    parent.updateView();
                });
        addKeyBinding(inputMap, actionMap, "setFocusOnSideView",
                KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),
                () -> {parent.focusedViewIndex = 1;
                    parent.updateView();
                });
        addKeyBinding(inputMap, actionMap, "setFocusOnTopView",
                KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),
                () -> {parent.focusedViewIndex = 2;
                        parent.updateView();
                        });
        addKeyBinding(inputMap, actionMap, "changeToNormalView",
                KeyStroke.getKeyStroke(KeyEvent.VK_0, 0),
                () ->{parent.setFocusOnView = 0;
                    parent.updateView();
                });

    }

    private void addArrowKeyBinding(InputMap inputMap, ActionMap actionMap, int keyCode, Runnable action) {
        addKeyBinding(inputMap, actionMap, "arrow_" + keyCode, KeyStroke.getKeyStroke(keyCode, 0), action);
    }

    private void addArrowKeyBinding(InputMap inputMap, ActionMap actionMap, int keyCode, int modifiers, Runnable action) {
        addKeyBinding(inputMap, actionMap, "arrow_" + keyCode + "_mod", KeyStroke.getKeyStroke(keyCode, modifiers), action);
    }

    private void addKeyBinding(InputMap inputMap, ActionMap actionMap,
                               String name, KeyStroke keyStroke, Runnable action) {
        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }
}