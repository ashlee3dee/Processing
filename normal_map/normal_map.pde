public float extrusion = 0.2;
public boolean render = false;
public float scaleFactor = 0.01;
public float dimension3Speed = 0.1;
void setup() {
  frameRate(30);
  size(256, 256);
  noiseDetail(2);
  smooth(4);
}

void draw() {
  background(0);
  noiseDetail(2);
  extrusion = 0.81;
  scaleFactor = 0.0302;
  dimension3Speed = 0.025;
  //for (int xi = 0; xi < width/2; xi ++) {
  //  for (int yi = 0; yi < height; yi ++) {
  //    color c = color(noise(xi*scaleFactor, yi*scaleFactor, frameCount*0.01)*30);
  //    set(xi, yi, c);
  //  }
  //}
  for (int xi = 0; xi < width; xi ++) {
    for (int yi = 0; yi < height; yi ++) {
      float dimension3Index = frameCount*dimension3Speed;
      float up=         noise(xi*scaleFactor, (yi - 1)*scaleFactor, dimension3Index)*255;
      float down=       noise(xi*scaleFactor, (yi + 1)*scaleFactor, dimension3Index)*255;
      float left=       noise((xi - 1)*scaleFactor, yi*scaleFactor, dimension3Index)*255;
      float right=      noise((xi + 1)*scaleFactor, yi*scaleFactor, dimension3Index)*255;
      float upleft=     noise((xi - 1)*scaleFactor, (yi - 1)*scaleFactor, dimension3Index)*255;
      float upright=    noise((xi + 1)*scaleFactor, (yi - 1)*scaleFactor, dimension3Index)*255;
      float downleft=   noise((xi - 1)*scaleFactor, (yi + 1)*scaleFactor, dimension3Index)*255;
      float downright=  noise((xi + 1)*scaleFactor, (yi + 1)*scaleFactor, dimension3Index)*255;

      float vert = (down - up) * 2.0 + downright + downleft - upright - upleft;
      float horiz = (right - left) * 2.0 + upright + downright - upleft - downleft;
      float depth = 1.0 / extrusion;
      float scale = 127 / sqrt(vert*vert + horiz*horiz + depth*depth);

      float r = 128 - horiz * scale;
      float g = 128 + vert * scale;
      float b = 128 + depth * scale;
      //PVector p = new PVector(r, g, b);
      color c = color(r, g, b);
      int lower = 136;
      int gap = 7;
      if (b >=lower && b <= lower+gap) {
        c = color(r, g, b);
      }
      set(xi, yi, c);
    }
  }
  if (render) {
    if (frameCount <= 120) {
      String s = "";
      if (frameCount < 10) {
        s="0";
      }
      save("render"+s+frameCount+".tga");
    } else {
      exit();
    }
  }
}