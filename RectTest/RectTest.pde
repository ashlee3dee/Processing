PImage sprites;
int sprite =0;
void setup() {
  size(400, 400, P3D);
  PImage sprites = loadImage("star_sprites.png");
  rectMode(CENTER);
  textureMode(NORMAL);
}

void draw() {
  background(0);
  translate(width/2, height/2);
  drawQuad();
}

public void drawQuad() {
  beginShape(QUADS);
  noStroke();
  noFill();
  float textureWidth = 1f/8f;
  //PImage = image(sprites.get(sprite*32, (sprite+1)*32, 32, 32), 0, 0);
  texture(sprites);
  tint(255, 128);
  vertex(0, 0, textureWidth*(this.sprite), 0);
  vertex(32, 0, textureWidth*(this.sprite+1), 0);
  vertex(32, 32, textureWidth*(this.sprite+1), textureWidth);
  vertex(0, 32, textureWidth*(this.sprite), textureWidth);
  endShape();
}
