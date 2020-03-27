class Particle {
  PVector pos = new PVector(0, 0);
  PVector vel = new PVector(0, 0);
  PVector acc = new PVector(0, 0);
  public Particle() {
  }
  public Particle(PVector p) {
    pos = p;
  }
  public void update() {
    if (pos.x<0)pos.x=width;
    else if (pos.x>width)pos.x=0;
    if (pos.y<0)pos.y=height;
    else if (pos.y>height)pos.y=0;
    vel.limit(2);
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);
  }
  public void applyForce(PVector force) {
    acc.add(force.mult(0.1));
  }

  public void show() {
    stroke(0);
    ellipse(pos.x, pos.y, 10, 10);
  }
}
