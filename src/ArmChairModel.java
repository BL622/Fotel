import java.awt.*;
import java.util.Random;

public class ArmChairModel {
    private int width;
    private int height;
    private int depth;
    private final int armWidth = 20;
    private final int legHeight = 20;
    private Color baseColor;
    private Color cushionColor;
    private Color legColor;
    private Color pillowColor;

    public ArmChairModel(){
        Randomize(true, true);
    }

    public void Randomize(boolean color, boolean size){
        if (color) {
            Random rnd = new Random();
            baseColor = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            cushionColor = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            legColor = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            pillowColor = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
        if (size) {
            Random rnd = new Random();
            width = rnd.nextInt(160, 300);
            height = rnd.nextInt(160, 250);
            depth = rnd.nextInt(160, 240);
        }
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getDepth() {return depth;}
    public int getArmWidth() {return armWidth;}
    public int getLegHeight() {return legHeight;}
    public Color getBaseColor() {return baseColor;}
    public Color getCushionColor() {return cushionColor;}
    public Color getLegColor() {return legColor;}
    public Color getPillowColor() {return pillowColor;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setDepth(int depth) {this.depth = depth;}
    public void setBaseColor(Color baseColor) {this.baseColor = baseColor;}
    public void setCushionColor(Color cushionColor) {this.cushionColor = cushionColor;}
    public void setLegColor(Color legColor) {this.legColor = legColor;}
    public void setPillowColor(Color pillowColor) {this.pillowColor = pillowColor;}
}
