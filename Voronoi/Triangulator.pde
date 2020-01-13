class Triangulator {
  boolean draw = true;
  public Triangulator() {
  }
  void plotSite(PVector site) {
    stroke(255, 0, 0);
    fill(color(255, 0, 0));
    plotCoord(site.x, site.y);
  }
  void plotBisector() {
    //clipLine(PVector edge)
  }
  void plotEndpoint() {
  }
  void plotVertex(PVector site) {
    stroke(0, 255, 0);
    fill(color(0, 255, 0));
    plotCoord(site.x, site.y);
  }
  void plotTriple() {
  }
  void plotCoord(float x, float y) {
    arc(x, y, 1.0f, 1.0f, 0.0f, (float)Math.PI * 2.0f, OPEN);
  }
  void clipLine() {
    float dy = height, 
      dx = width, 
      d = (dx > dy)? dx : dy, 
      pxmin = - (d - dx)/2, 
      pxmax = width + (d - dx)/2, 
      pymin = - (d - dy)/2, 
      pymax = height + (d - dy)/2;
    float s1, s2;
    float x1, x2, y1, y2;
    //if (e.a == 1 && e.b >= 0) {
    //  s1 = e.ep.right;
    //  s2 = e.ep.left;
    //} else {
    //  s1 = e.ep.left;
    //  s2 = e.ep.right;
    //}
    //if (e.a == 1) {
    //  y1 = pymin;
    //  if (s1 && s1.y > pymin) {
    //    y1 = s1.y;
    //  }
    //  if (y1 > pymax) {
    //    return;
    //  }
    //  x1 = e.c - e.b * y1;
    //  y2 = pymax;
    //  if (s2 && s2.y < pymax) {
    //    y2 = s2.y;
    //  }
    //  if (y2 < pymin) {
    //    return;
    //  }
    //  x2 = e.c - e.b * y2;
    //  if (((x1 > pxmax) && (x2 > pxmax)) || ((x1 < pxmin) && (x2 < pxmin))) {
    //    return;
    //  }
    //  if (x1 > pxmax) {
    //    x1 = pxmax;
    //    y1 = (e.c - x1) / e.b;
    //  }
    //  if (x1 < pxmin) {
    //    x1 = pxmin;
    //    y1 = (e.c - x1) / e.b;
    //  }
    //  if (x2 > pxmax) {
    //    x2 = pxmax;
    //    y2 = (e.c - x2) / e.b;
    //  }
    //  if (x2 < pxmin) {
    //    x2 = pxmin;
    //    y2 = (e.c - x2) / e.b;
    //  }
    //} else {
    //  x1 = pxmin;
    //  if (s1 && s1.x > pxmin) {
    //    x1 = s1.x;
    //  }
    //  if (x1 > pxmax) {
    //    return;
    //  }
    //  y1 = e.c - e.a * x1;
    //  x2 = pxmax;
    //  if (s2 && s2.x < pxmax) {
    //    x2 = s2.x;
    //  }
    //  if (x2 < pxmin) {
    //    return;
    //  }
    //  y2 = e.c - e.a * x2;
    //  if (((y1 > pymax) && (y2 > pymax)) || ((y1 < pymin) && (y2 < pymin))) {
    //    return;
    //  }
    //  if (y1 > pymax) {
    //    y1 = pymax;
    //    x1 = (e.c - y1) / e.a;
    //  }
    //  if (y1 < pymin) {
    //    y1 = pymin;
    //    x1 = (e.c - y1) / e.a;
    //  }
    //  if (y2 > pymax) {
    //    y2 = pymax;
    //    x2 = (e.c - y2) / e.a;
    //  }
    //  if (y2 < pymin) {
    //    y2 = pymin;
    //    x2 = (e.c - y2) / e.a;
    //  }
    //}
    stroke(0, 0, 255);
    fill(color(255, 255, 0));
    //line(x1, y1, x2, y2);
  }
}

class EdgeList {
  PVector[] list;
  PVector leftEnd = null;
  PVector rightEnd = null;

  public EdgeList() {
  }
  
  void createHaldEdge
}
