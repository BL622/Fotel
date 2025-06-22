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
                                () -> new FileManager(parent, model).deleteLocal());

                addKeyBinding(inputMap, actionMap, "saveOutAction",
                                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                                () -> new FileManager(parent, model).saveToFile());

                addKeyBinding(inputMap, actionMap, "loadOutAction",
                                KeyStroke.getKeyStroke(KeyEvent.VK_O,
                                                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                                () -> {
                                        new FileManager(parent, model).loadFromFile();
                                        parent.updateSliders();
                                        parent.repaint();
                                });

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

                addKeyBinding(inputMap, actionMap, "exit",
                                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                                () -> System.exit(0));

                addKeyBinding(inputMap, actionMap, "help",
                                KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK),
                                Fotel::help);

                switch (parent.setFocusOnView) {
                        case 0:
                                normalViewKeyBindings(inputMap, actionMap);
                                break;
                        case 1:
                                focusViewKeyBindings(inputMap, actionMap);
                                break;

                }

                addKeyBinding(inputMap, actionMap, "openFileMenu",
                                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_DOWN_MASK),
                                () -> {
                                        JMenuBar menuBar = parent.getJMenuBar();
                                        if (menuBar != null && menuBar.getMenuCount() > 0) {
                                                JMenu fileMenu = menuBar.getMenu(0);
                                                fileMenu.doClick();
                                        }
                                });

                addKeyBinding(inputMap, actionMap, "openViewMenu",
                                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK),
                                () -> {
                                        JMenuBar menuBar = parent.getJMenuBar();
                                        if (menuBar != null && menuBar.getMenuCount() > 1) {
                                                JMenu viewMenu = menuBar.getMenu(1);
                                                viewMenu.doClick();
                                        }
                                });
        }

        private void normalViewKeyBindings(InputMap inputMap, ActionMap actionMap) {

                addKeyBinding(inputMap, actionMap, "focusWidth",
                                KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),
                                () -> parent.setFocusedSlider(0));

                addKeyBinding(inputMap, actionMap, "focusHeight",
                                KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),
                                () -> parent.setFocusedSlider(1));

                addKeyBinding(inputMap, actionMap, "focusDepth",
                                KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),
                                () -> parent.setFocusedSlider(2));

                addKeyBinding(inputMap, actionMap, "arrow_right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                                parent::increaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                                parent::decreaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                                parent::increaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                                parent::decreaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_right_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK),
                                parent::largeIncreaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_left_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK),
                                parent::largeDecreaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_up_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK),
                                parent::largeIncreaseFocusedSlider);
                addKeyBinding(inputMap, actionMap, "arrow_down_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK),
                                parent::largeDecreaseFocusedSlider);

                addKeyBinding(inputMap, actionMap, "openBaseColor",
                                KeyStroke.getKeyStroke(KeyEvent.VK_4, 0),
                                () -> parent.colorChooser(model.getBaseColor(), model::setBaseColor, "alapszínt"));
                addKeyBinding(inputMap, actionMap, "openCushionColor", KeyStroke.getKeyStroke(KeyEvent.VK_5, 0),
                                () -> parent.colorChooser(model.getCushionColor(), model::setCushionColor,
                                                "párna színt"));
                addKeyBinding(inputMap, actionMap, "openLegColor",
                                KeyStroke.getKeyStroke(KeyEvent.VK_6, 0),
                                () -> parent.colorChooser(model.getLegColor(), model::setLegColor, "láb színt"));
                addKeyBinding(inputMap, actionMap, "openPillowColor",
                                KeyStroke.getKeyStroke(KeyEvent.VK_7, 0),
                                () -> parent.colorChooser(model.getPillowColor(), model::setPillowColor,
                                                "díszpárna színt"));

                addKeyBinding(inputMap, actionMap, "changeToFocusedView",
                                KeyStroke.getKeyStroke(KeyEvent.VK_8, 0),
                                () -> {
                                        parent.setFocusOnView = 1;
                                        parent.updateView();
                                });
        }

        private void focusViewKeyBindings(InputMap inputMap, ActionMap actionMap) {

                addKeyBinding(inputMap, actionMap, "setFocusOnFrontView",
                                KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),
                                () -> {
                                        parent.focusedViewIndex = 0;
                                        parent.updateView();
                                });
                addKeyBinding(inputMap, actionMap, "setFocusOnSideView",
                                KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),
                                () -> {
                                        parent.focusedViewIndex = 1;
                                        parent.updateView();
                                });
                addKeyBinding(inputMap, actionMap, "setFocusOnTopView",
                                KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),
                                () -> {
                                        parent.focusedViewIndex = 2;
                                        parent.updateView();
                                });
                addKeyBinding(inputMap, actionMap, "changeToNormalView",
                                KeyStroke.getKeyStroke(KeyEvent.VK_0, 0),
                                () -> {
                                        parent.setFocusOnView = 0;
                                        parent.updateView();
                                });

                addKeyBinding(inputMap, actionMap, "arrow_right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), () -> {
                });
                addKeyBinding(inputMap, actionMap, "arrow_left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), () -> {
                });
                addKeyBinding(inputMap, actionMap, "arrow_up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), () -> {
                });
                addKeyBinding(inputMap, actionMap, "arrow_down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), () -> {
                });
                addKeyBinding(inputMap, actionMap, "arrow_right_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK), () -> {
                                });
                addKeyBinding(inputMap, actionMap, "arrow_left_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK), () -> {
                                });
                addKeyBinding(inputMap, actionMap, "arrow_up_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK), () -> {
                                });
                addKeyBinding(inputMap, actionMap, "arrow_down_mod",
                                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK), () -> {
                                });
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