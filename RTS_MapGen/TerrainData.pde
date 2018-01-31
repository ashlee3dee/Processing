public class TerrainData {
  private TerrainPalette tP;
  private float[] nD;
  private float noiseScale = 0.01;
  private int baseNoiseOctaves = 6;
  private PVector size;
  public TerrainData(PVector s) {
    tP = new TerrainPalette();
    size = s;
    nD = new float[(int)(size.x*size.y)];
    for (int i = 0; i<size.x*size.y; i++) {
      nD[i] = 0.0;
    }
    noiseDetail(baseNoiseOctaves);
  }
  public void UpdateParameter(ParameterType tp, DataObject d) {
    switch(tp) {
    case NOISE_SCALE:
      noiseScale = (float)d.GetData();
      break;
    case BASE_DETAIL:
      baseNoiseOctaves = (int)d.GetData();
      noiseDetail(baseNoiseOctaves);
      break;
    case PALETTE:
      tP = (TerrainPalette)d.GetData();
    default:
      break;
    }
  }
  public color pickColor(float value) {
    color finalColor = color(0, 0, 0);
    int terrainHeight = (int)(value*255);
    for (int i = 0; i < tP.Size(); i++) {
      if (terrainHeight <= tP.GetThreshold(i)) {
        finalColor = tP.GetColor(i);
        break;
      }
    }
    return finalColor;
  } 
  public float GetNoiseScale() {
    return noiseScale;
  }
  public float GetNoiseFromCell(PVector cellpos) {
    float value =0.0;
    if (PositionInBounds(cellpos)) {
      value = nD[(int)(cellpos.x + cellpos.y * size.x)];
    }
    return value;
  }
  private boolean PositionInBounds(PVector position) {
    if (position.x < size.x & position.y < size.y) {
      return true;
    } else {
      return false;
    }
  }
  public void SetCell(PVector cellpos, float value) {
    if (PositionInBounds(cellpos)) {
      nD[(int)(cellpos.x + cellpos.y * size.x)] = value;
    }
  }
}
public enum ParameterType {
  NOISE_SCALE, 
    BASE_DETAIL, 
    PALETTE
}