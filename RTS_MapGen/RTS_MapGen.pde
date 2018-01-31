import g4p_controls.*;

public TerrainEngine te;
public PImage result;
GWindow window;
GCustomSlider scaleSlider, octaveSlider;
TerrainData tempData;

void setup()
{
  size(512, 512);
  colorMode(RGB);
  noiseDetail(6);
  frameRate = 30;
  te = new TerrainEngine();
  result = new PImage();
  tempData = new TerrainData(new PVector(512, 512));
}
void draw() {
  background(255);
  if (frameCount % 10 == 0) {
    te.PopulateData();
    result = te.GetImage();
  }
}