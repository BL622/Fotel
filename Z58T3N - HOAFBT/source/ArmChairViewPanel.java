import javax.swing.*;
import java.awt.*;

public abstract class ArmChairViewPanel extends JPanel {
    protected final ArmChairModel model;

    public ArmChairViewPanel(String title, ArmChairModel model) {
        this.model = model;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(title));
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawView(g);
    }

    protected abstract void drawView(Graphics g);

    protected void drawFrontView(Graphics g) {
        int width = model.getWidth();
        int height = model.getHeight();
        int armWidth = model.getArmWidth();
        int legHeight = model.getLegHeight();

        int seatHeight = height / 4;
        int x = (getWidth() / 2) - (width / 2);
        int y = getHeight() - 60;

        g.setColor(model.getLegColor());
        g.fillRect(x + 5, y, 10, legHeight);
        g.fillRect(x + width - 15, y, 10, legHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y, 10, legHeight);
        g.drawRect(x + width - 15, y, 10, legHeight);

        g.setColor(model.getBaseColor());
        g.fillRect(x, y - (int) (height * 0.8) + legHeight, armWidth, (int) (height * 0.8) - legHeight);
        g.fillRect(x + width - armWidth, y - (int) (height * 0.8) + legHeight, armWidth,
                (int) (height * 0.8) - legHeight);
        g.fillRect(x + armWidth, y - height + legHeight, width - 2 * armWidth, height - seatHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x, y - (int) (height * 0.8) + legHeight, armWidth, (int) (height * 0.8) - legHeight);
        g.drawRect(x + width - armWidth, y - (int) (height * 0.8) + legHeight, armWidth,
                (int) (height * 0.8) - legHeight);
        g.drawRect(x + armWidth, y - height + legHeight, width - 2 * armWidth, height - seatHeight);

        g.setColor(model.getCushionColor());
        g.fillRect(x + armWidth, y - seatHeight, width - 2 * armWidth, seatHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x + armWidth, y - seatHeight, width - 2 * armWidth, seatHeight);

        g.setColor(model.getPillowColor());
        g.fillRect(x + armWidth + 5, y - seatHeight - height / 4 - 1, width - (armWidth * 2) - 10, height / 4);
        g.setColor(Color.BLACK);
        g.drawRect(x + armWidth + 5, y - seatHeight - height / 4 - 1, width - (armWidth * 2) - 10, height / 4);
    }

    protected void drawSideView(Graphics g) {
        int height = model.getHeight();
        int depth = model.getDepth();
        int armWidth = model.getArmWidth();
        int legHeight = model.getLegHeight();

        int x = (getWidth() / 2) - (depth / 2);
        int y = getHeight() - 60;

        g.setColor(model.getLegColor());
        g.fillRect(x + 5, y, 10, legHeight);
        g.fillRect(x + depth - 15, y, 10, legHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y, 10, legHeight);
        g.drawRect(x + depth - 15, y, 10, legHeight);

        g.setColor(model.getBaseColor());
        g.fillRect(x, y - height + legHeight, armWidth, (int) (height * 0.2));
        g.fillRect(x, y - (int) (height * 0.8) + legHeight, depth, (int) (height * 0.8) - legHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x, y - height + legHeight, armWidth, (int) (height * 0.2));
        g.drawRect(x, y - (int) (height * 0.8) + legHeight, depth, (int) (height * 0.8) - legHeight);
    }

    protected void drawTopView(Graphics g) {
        int width = model.getWidth();
        int depth = model.getDepth();
        int armWidth = model.getArmWidth();

        int x = (getWidth() / 2) - (width / 2);
        int y = (getHeight() / 2) - (depth / 2);

        g.setColor(model.getBaseColor());
        g.fillRect(x, y, armWidth, depth);
        g.fillRect(x + width - armWidth, y, armWidth, depth);
        g.fillRect(x + armWidth, y, width - 2 * armWidth, armWidth);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, armWidth, depth);
        g.drawRect(x + width - armWidth, y, armWidth, depth);
        g.drawRect(x + armWidth, y, width - 2 * armWidth, armWidth);

        g.setColor(model.getCushionColor());
        g.fillRect(x + armWidth, y + armWidth, width - 2 * armWidth, depth - armWidth);
        g.setColor(Color.BLACK);
        g.drawRect(x + armWidth, y + armWidth, width - 2 * armWidth, depth - armWidth);

        g.setColor(model.getPillowColor());
        g.fillRect(x + armWidth + 5, y + armWidth + 1, width - (armWidth * 2) - 10, armWidth);
        g.setColor(Color.BLACK);
        g.drawRect(x + armWidth + 5, y + armWidth + 1, width - (armWidth * 2) - 10, armWidth);
    }

    protected void drawSizes(Graphics g) {
        int width = model.getWidth();
        int height = model.getHeight();
        int depth = model.getDepth();
        int armWidth = model.getArmWidth();
        int legHeight = model.getLegHeight();

        int surface, area;
        int seatHeight = height / 4;
        int insideWidth = width - armWidth * 2;
        int sideHeight = (int) (height * 0.8) - legHeight;
        surface = (int) ((depth * sideHeight) * 2
                + (depth * armWidth) * 4 + (sideHeight * armWidth) * 4
                + (sideHeight - seatHeight) * (depth - armWidth) * 2
                + (insideWidth * seatHeight)
                + (insideWidth * (depth - armWidth)) * 2
                + (insideWidth * (height - legHeight))
                + (insideWidth * (height - legHeight - seatHeight))
                + (armWidth * insideWidth) * 2
                + ((height - legHeight * 0.2) * armWidth) * 2);

        area = (int) ((insideWidth * seatHeight * (depth - armWidth))
                + (armWidth * sideHeight * depth) * 2
                + (insideWidth * (height - legHeight) * armWidth)
                + (5 * 5 * Math.PI * legHeight) * 4
                + (float) ((height / 4) * armWidth * (insideWidth - 10)));

        g.setFont(new Font("default", Font.PLAIN, 12));
        g.drawString((float) width / 200 + " m", 200, 40);
        g.drawString((float) height / 200 + " m", 200, 60);
        g.drawString((float) depth / 200 + " m", 200, 80);
        g.drawString((float) surface / 40000 + " m^2", 200, 100);
        g.drawString((float) area / 8000000 + " m^3", 200, 120);
        g.drawString((float) (width * depth) / 40000 + " m^2", 200, 140);
        g.drawString((float) (width * depth * height) / 8000000 + " m^3", 200, 160);
        g.setFont(new Font("default", Font.BOLD, 12));
        g.drawString("Szélesség:", 10, 40);
        g.drawString("Magasság:", 10, 60);
        g.drawString("Mélység:", 10, 80);
        g.drawString("Valós Felület:", 10, 100);
        g.drawString("Valós Térfogat:", 10, 120);
        g.drawString("Elfoglalt Terület (m^2):", 10, 140);
        g.drawString("Elfoglalt Terület (m^3):", 10, 160);
    }
}
