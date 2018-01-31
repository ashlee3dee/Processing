class River {
  int xpos;
  int ypos;
  int decayRate;
  color col;
  public River(int x, int y, color c, int dc) {
    xpos = x;
    ypos = y;
    col = c;
    decayRate = dc;
  }
  public void update() {
    //color in current pixel
    i.set(xpos, ypos, col);
    o.set(xpos, ypos, col);
    //get values surrounding current pixel
    PixelValuePair pix[]= new PixelValuePair[8];
    pix[0] = new PixelValuePair(xpos-1, ypos-1, brightness(i.get(xpos-1, ypos-1)));  //upper left pixel
    pix[1] = new PixelValuePair(xpos, ypos-1, brightness(i.get(xpos, ypos-1)));    //upper pixel
    pix[2] = new PixelValuePair(xpos+1, ypos-1, brightness(i.get(xpos+1, ypos-1)));  //upper right pixel
    pix[3] = new PixelValuePair(xpos-1, ypos, brightness(i.get(xpos-1, ypos)));  //left pixel
    pix[4] = new PixelValuePair(xpos+1, ypos, brightness(i.get(xpos+1, ypos)));  //right pixel
    pix[5] = new PixelValuePair(xpos+1, ypos-1, brightness(i.get(xpos+1, ypos-1)));  //lower left pixel
    pix[6] = new PixelValuePair(xpos+1, ypos, brightness(i.get(xpos+1, ypos)));  //lower pixel
    pix[7] = new PixelValuePair(xpos+1, ypos+1, brightness(i.get(xpos+1, ypos+1)));  //lower right pixel
    //find lowest brightness value pixel
    int lowestIndex = 0;
    float minValue = pix[0].v;
    for (int i = 1; i < pix.length; i++) {
      if (pix[i].v < minValue) {
        minValue = pix[i].v;
        lowestIndex = i;
      }
    }
    //move current pixel to lowest value pixel
    xpos = pix[lowestIndex].x;
    ypos = pix[lowestIndex].y;
    //adjust the brightness of the color
    col = color(hue(col), saturation(col), brightness(col)-decayRate);
  }
  public boolean inBounds() {
    if (xpos > 0 &
      xpos < i.width &
      ypos > 0 &
      ypos < i.height) {
      return true;
    }
    return false;
  }
}