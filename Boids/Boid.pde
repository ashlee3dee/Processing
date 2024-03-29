class Boid {
  PVector position;
  PVector velocity;
  PVector acceleration;
  PVector sprite;

  int size;

  float maxForce;
  private float maxSpeed;

  Boid() {
    this.position = new PVector(random(width*4), random(width*4), random(width*3));
    this.velocity = PVector.random3D();
    this.velocity.setMag(random(100));
    this.acceleration = new PVector();
    this.maxForce = 1f;
    this.maxSpeed = 5f;
    this.size=round(random(10, 50));
    this.sprite=new PVector(round(random(0, 8)), round(random(0, 2)));
  }

  void edges() {
    if (this.position.x > width*4) {
      this.position.x = 0;
    } else if (this.position.x < 0) {
      this.position.x = width*4;
    }
    if (this.position.y > width*4) {
      this.position.y = 0;
    } else if (this.position.y < 0) {
      this.position.y = width*4;
    }
    if (this.position.z > width*4) {
      this.position.z = 0;
    } else if (this.position.z < 0) {
      this.position.z = width*4;
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
    //float[] camPos = cam.getPosition();
    //println(dist(
    //  this.position.x, this.position.y, this.position.z, 
    //  camPos[0], camPos[1], camPos[2]));
  }
  void setMaxSpeed(float newSpeed) {
    this.maxSpeed = newSpeed;
  }
  float getMaxSpeed() {
    return this.maxSpeed;
  }
  void showSpheres() {
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

  void showDebug() {
    float a = map(dist(
      this.position.x, this.position.y, this.position.z, 
      0, 0, 0), 
      width/2f, width*2, 
      1f, 0f);
    noStroke();
    fill(a*255);
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z); 
    sphere(size);
    popMatrix();
  }  
  void showSprite() {
    pushMatrix();
    translate(this.position.x, this.position.y, this.position.z);
    float[] rot = cam.getRotations();
    rotateX(rot[0]);
    rotateY(rot[1]);
    rotateZ(rot[2]);
    drawQuad();
    popMatrix();
  }
  public void drawQuad() {
    hint(DISABLE_DEPTH_MASK);
    blendMode(ADD);
    beginShape(QUADS);
    noStroke();
    noFill();
    float textureWidth = 1f/8f;
    texture(sprites);
    float a = map(dist(
      this.position.x, this.position.y, this.position.z, 
      width*2, width*2, width*2), 

      0, width*2, 
      1f, 0.0);
    tint(255, 255*(pow(a*3, log(1.5))));
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
