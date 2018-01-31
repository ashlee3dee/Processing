class Particle {
  PVector pos = new PVector(0, 0);
  PVector vel = new PVector(0, 0);
  PVector acc = new PVector(0, 0);
  public Particle() {
  }
  public void update() {
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);
  }
  public void applyForce(PVector force) {
    acc.add(force);
  }

  public void show() {
    stroke(0);
    point(pos.x, pos.y);
  }
}