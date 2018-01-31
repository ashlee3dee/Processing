public class TerrainEngine {
  private TerrainData terrainBuffer;
  public TerrainEngine() {
    terrainBuffer = new TerrainData(new PVector(512, 512));
  }
  public TerrainEngine(TerrainData dat) {
    terrainBuffer = dat;
  }
  //=============================================================
  // terrain engine functional methods
  
  //this should be called by g4ps event handler
  public void UpdateParameters(TerrainData newData) {
    terrainBuffer = newData;
  }
  public PImage GetImage() {
    PImage pi = new PImage((int)terrainBuffer.size.x, (int)terrainBuffer.size.y);
    for (int xi = 0; xi < terrainBuffer.size.x; xi++) {
      for (int yi =0; yi < terrainBuffer.size.y; yi++) {
        PVector cellpos = new PVector(xi, yi);
        pi.set(xi, yi, terrainBuffer.pickColor(terrainBuffer.GetNoiseFromCell(cellpos)));
      }
    }
    return pi;
  }
  public void randomizeSeed() {
    
  }
  private void PopulateData() {
    for (int xi = 0; xi < terrainBuffer.size.x; xi++) {
      for (int yi =0; yi < terrainBuffer.size.y; yi++) {
        terrainBuffer.SetCell(new PVector(xi, yi), noise(xi*terrainBuffer.GetNoiseScale(), yi*terrainBuffer.GetNoiseScale()));
      }
    }
  }
  // END TERRAIN ENGINE
}