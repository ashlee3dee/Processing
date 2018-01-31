public class DataObject {
  private int dInteger;
  private float dFloat;
  private PVector dPVector;
  private DataType dDataType;
  public DataObject(int data) {
    dInteger = data;
    dDataType= DataType.INT;
  }
  public DataObject(float data) {
    dFloat = data;
    dDataType= DataType.FLOAT;
  }  
  public DataObject(PVector data) {
    dPVector = data;
    dDataType= DataType.PVEC;
  }
  public Object GetData() {
    Object o = null;
    switch(dDataType) {
    case INT:
      o = dInteger;
      break;
    case FLOAT:
      o= dFloat;
      break;
    case PVEC:
      o= dPVector;
      break;
    default:
      break;
    }
    return o;
  }
}
private enum DataType {
  INT, 
    FLOAT, 
    PVEC
}