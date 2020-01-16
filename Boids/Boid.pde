class Boid {
  PVector position;
  PVector velocity;
  PVector acceleration;

  float maxForce;
  float maxSpeed;
  int size = 10;
  PVector sprite;
  Boid() {
    this.position = new PVector(random(width), random(width), random(width));
    this.velocity = PVector.random3D();
    this.velocity.setMag(random(5, 10));
    this.acceleration = new PVector();
    this.maxForce = 1f;
    this.maxSpeed = 5f;
    sprite=new PVector(round(random(0, 8)), round(random(0, 1)));
    //sprite=0;
  }

  void edges() {
    if (this.position.x > width) {
      this.position.x = 0;
    } else if (this.position.x < 0) {
      this.position.x = width;
    }
    if (this.position.y > width) {
      this.position.y = 0;
    } else if (this.position.y < 0) {
      this.position.y = width;
    }
    if (this.position.z > width) {
      this.position.z = 0;
    } else if (this.position.z < 0) {
      this.position.z = width;
    }
  }

  PVector align(Boid[] boids) {
    PVector steering = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(this.position.x, this.position.y, this.position.z, other.position.x, other.position.y, other.position.z);
      if (other != this && d < alignmentRadius) {
        steering.add(other.velocity);
        total++;
      }
    }
    if (total > 0) {
      steering.div(total);
      steering.setMag(this.maxSpeed);
      steering.sub(this.velocity);
      steering.limit(this.maxForce);
    }
    return steering;
  }

  PVector separation(Boid[] boids) {
    PVector steering = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(this.position.x, this.position.y, this.position.z, other.position.x, other.position.y, other.position.z);
      //println(d);
      if (other != this && d < separationRadius+(this.size*2)) {
        PVector diff = PVector.sub(this.position, other.position);
        diff.div(d*d);
        steering.add(diff);
        total++;
      }
    }
    if (total > 0) {
      steering.div(total);
      steering.setMag(this.maxSpeed);
      steering.sub(this.velocity);
      steering.limit(this.maxForce);
    }
    return steering;
  }

  PVector cohesion(Boid[] boids) {
    PVector steering = new PVector();
    int total = 0;
    for (Boid other : boids) {
      float d = dist(this.position.x, this.position.y, this.position.z, other.position.x, other.position.y, other.position.z);
      if (other != this && d < cohesionRadius && d > this.size*2) {
        steering.add(other.position);
        total++;
      }
    }
    if (total > 0) {
      steering.div(total);
      steering.sub(this.position);
      steering.setMag(this.maxSpeed);
      steering.sub(this.velocity);
      steering.limit(this.maxForce);
    }
    return steering;
  }

  void flock(Boid[] boids) {
    PVector alignment = this.align(boids);
    PVector cohesion = this.cohesion(boids);
    PVector separation = this.separation(boids);

    alignment.mult(alignmentValue);
    cohesion.mult(cohesionValue);
    separation.mult(separationValue);

    this.acceleration.add(alignment);
    this.acceleration.add(cohesion);
    this.acceleration.add(separation);
  }

  void update() {
    this.position.add(this.velocity);
    this.velocity.add(this.acceleration);
    this.velocity.limit(this.maxSpeed);
    this.acceleration.mult(0);
  }

  void showSpheres() {
    //stroke(255);
    noStroke();
    fill(abs(xAngle(this.velocity)/PI)*255, abs(yAngle(this.velocity)/PI)*255, abs(zAngle(this.velocity)/PI)*255);
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z); 
    sphere(this.size/2);
    popMatrix();
  }

  void showVisual() {
    noFill();
    strokeWeight(2);
    line(this.position.x, this.position.y, this.position.z, this.position.x+(this.velocity.x*size), this.position.y+(this.velocity.y*size), this.position.z+(this.velocity.z*size));

    stroke((xAngle(this.velocity)/PI)*255, (yAngle(this.velocity)/PI)*255, (zAngle(this.velocity)/PI)*255);
    strokeWeight(size);
    point(this.position.x, this.position.y, this.position.z);
  }
  void showSprite() {
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z);
    float[] rot = cam.getRotations();
    rotateX(rot[0]);
    rotateY(rot[1]);
    rotateZ(rot[2]);
    //scale(10f);
    //sphere(size);
    drawQuad();
    popMatrix();
  }
  void showDebug() {
    noFill();
    strokeWeight(alignmentValue*2);
    //stroke(255, 0, 0);
    //pushMatrix();
    //translate(this.position.x, this.position.y, this.position.z); 
    //sphere(alignmentRadius);
    //popMatrix();
    strokeWeight(separationValue*2);
    stroke(0, 255, 0);
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z); 
    sphere(separationRadius);
    popMatrix();
    strokeWeight(cohesionValue*2);
    stroke(0, 0, 255);
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z); 
    sphere(cohesionRadius);
    popMatrix();
  }
  public void drawQuad() {
    hint(DISABLE_DEPTH_MASK);
    blendMode(ADD);
    beginShape(QUADS);
    noStroke();
    noFill();
    float textureWidth = 1f/8f;
    //PImage = image(sprites.get(sprite*32, (sprite+1)*32, 32, 32), 0, 0);
    texture(sprites);
    //tint(255, 128);
    vertex(0, 0, textureWidth*(this.sprite.x), textureWidth*(this.sprite.y));
    vertex(this.size, 0, textureWidth*(this.sprite.x+1), textureWidth*(this.sprite.y));
    vertex(this.size, this.size, textureWidth*(this.sprite.x+1), textureWidth*(this.sprite.y+1));
    vertex(0, this.size, textureWidth*(this.sprite.x), textureWidth*(this.sprite.y+1));
    endShape();
  }
}
public static float xAngle(PVector vector) {
  return acos(vector.x / (vector.mag()));
}

public static float yAngle(PVector vector) {
  return acos(vector.y / (vector.mag()));
}

public static float zAngle(PVector vector) {
  return acos(vector.z / (vector.mag()));
}
