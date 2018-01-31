import java.util.List;
public class TerrainPalette {

  public ArrayList<TerrainColor> storedColors;
  public ArrayList<Integer> storedThresholds;
  public TerrainPalette(int initialThreshold, color initialColor) {
    InitializeDataStructure();
    AddColor(initialThreshold, initialColor);
  }
  public TerrainPalette() {
    InitializeDataStructure();
    PopulateDeaultPalette();
  }
  public void AddColor(int threshold, color newColor) {
    TerrainColor tc = new TerrainColor(newColor);
    storedColors.add(tc);
    storedThresholds.add(threshold);
  }
  public color GetColor(int index) {
    return storedColors.get(index).GetColor();
  }
  public int GetThreshold(int index) {
    return storedThresholds.get(index);
  }
  public int Size() {
    return storedColors.size();
  }
  public void Clear() {
    storedColors.clear();
    storedThresholds.clear();
  }
  private void InitializeDataStructure() {
    println("palette created");
    storedColors = new ArrayList<TerrainColor>();
    storedThresholds = new ArrayList<Integer>();
  }
  public void PopulateDeaultPalette() {
    Clear();
    AddColor(110, color(2, 27, 84));
    AddColor(123, color(1, 42, 147));
    AddColor(127, color(0, 191, 214));
    AddColor(134, color(215, 232, 118));
    AddColor(172, color(28, 159, 2));  
    AddColor(189, color(47, 47, 47));
    AddColor(255, color(253, 253, 255));
  }
}