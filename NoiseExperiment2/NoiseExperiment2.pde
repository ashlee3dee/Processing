import g4p_controls.*;
public float speed = 1, scale = 1, osc1Speed, osc1Amp;
public int filter = 1, thickness = 1;
public boolean enableSymmetry = true;
public boolean render = false;
public boolean enableImages = false;
public int renderBegin = 0;
public int framesToRender = 240;
public PImage image1, image2;
void setup() {

  image1 = loadImage("data/image1.png");
  image2 = loadImage("data/image2.png");
  colorMode(HSB);
  noiseDetail(2);
  size(512, 512);
  frameRate(30);
  createGUI();
  initGUIValues();
}
public void initGUIValues() {
  osc1freqSlider_changed(osc1freqSlider, null);
  speedSlider_changed(speedSlider, null);
  scaleSlider_changed(scaleSlider, null);
  lineGapSlider_changed(lineGapSlider, null);  
  thicknessSlider_changed(thicknessSlider, null); 
  detailSlider_changed(detailSlider, null);
  osc1ampSlider_changed(osc1ampSlider, null);
}
void draw() {
  background(0);
  int x = (enableSymmetry) ? 2 : 1;
  for (int ix = 0; ix < width/x; ix++) {
    for (int iy=0; iy < height/x; iy++) {
      float noiseValue = noise(ix*scale, iy*scale, frameCount*speed);
      color c = color(0);
      if (enableImages) {
        if (int((noiseValue*256))%filter <= thickness) {
          c =color(image1.get(int((ix+width/4)+(cos(frameCount*osc1Speed)*osc1Amp)), int((iy+height/4)+(sin(frameCount*osc1Speed)*osc1Amp))));
        } else {
          c =color(image2.get(int((ix+width/4)+(sin(frameCount*osc1Speed)*osc1Amp)), int((iy+height/4)+(cos(frameCount*osc1Speed)*osc1Amp))));
        }
      } else { 
        if (int((noiseValue*256))%filter <= thickness) {
          c =color(255);
        } else {
          c =color(0);
        }
      }
      if (enableSymmetry) {
        set(ix+1, iy+1, c);
        set((width-1)-ix, iy+1, c);
        set(ix+1, (height-1)-iy, c);
        set((width-1)-ix, (height-1)-iy, c);
      } else {
        set(ix, iy, c);
      }
    }
  }
  if (render) {
    if (frameCount < renderBegin + framesToRender) {
      save("render0"+frameCount+".tga");
      println("frame " + (frameCount-renderBegin+1) + " of " + framesToRender + " rendered.");
    } else {
      exit();
    }
  }
}