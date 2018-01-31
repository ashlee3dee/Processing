private class TerrainColor {
  private color STORED_COLOR = color(0, 0, 0);
  public TerrainColor(color col) {
    STORED_COLOR = col;
  }
  public color GetColor() {
    return STORED_COLOR;
  }
  public color SetColor() {
    return color(1);
  }
}