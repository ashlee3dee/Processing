import java.util.List;

public class Palette {
  private String ID;  // unique ID of palette
  private String NAME; // name assigned by user, passed as an argument during construction
  private PColor COLORS[];
  private int currentIndex = 0;

  // constructor for Palette object
  public Palette(String  paletteName) {
    this.ID = this.toString(); //set ID to memory address of this instance of palette class
    this.NAME = paletteName; // set NAME to the user specified name
    this.COLORS = new PColor[32];
  }
  public void AddColor(color c, String name) {
    //create new PColor object that encapsulates c
    PColor newColor = new PColor(name, c);
    //add color in next empty slot
    COLORS[currentIndex] = newColor;
    //increment the index counter
    currentIndex++;
  }
  public color getColorFromValue(float v) {
    return color(v);
  }
  public color getColorFromIndex(int i) {
    color c = color(0, 0, 0);
    if (i < currentIndex) {
      c = COLORS[i].getColor();
    } else {
      println("error! bad color index given. black returned");
      c = color(0);
    }
    return c;
  }
  public float getHue(int i) {
    print("return color at pallette space index: " + i);
    float v = hue(getColorFromIndex(i));
    return v;
  }
  public void ChangeName(String newName) {
    this.NAME = newName;
  }
  public int getSize() {
    return currentIndex;
  }

  public class PColor {
    private float[] VALUES = {0, 0, 0};
    private String ID;
    private String NAME;

    public PColor(String colorName, color col) {
      this.ID = this.toString();
      this.NAME = colorName;
      setColor(col);
    }
    public void setColor(color c) {
      this.VALUES = new float[]{hue(c), saturation(c), brightness(c)};
    }
    public color getColor() {
      return color(VALUES[0], VALUES[1], VALUES[2]);
    }
  }
}