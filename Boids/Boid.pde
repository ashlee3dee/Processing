class Boid {
  PVector position;
  PVector velocity;
  PVector acceleration;

  float maxForce;
  float maxSpeed;
  int size = 10;
  Boid() {
    this.position = new PVector(random(width), random(height), random(height));
    this.velocity = PVector.random3D();
    this.velocity.setMag(random(2, 4));
    this.acceleration = new PVector();
    this.maxForce = 1f;
    this.maxSpeed = 10f;
  }

  void edges() {
    if (this.position.x > width) {
      this.position.x = 0;
    } else if (this.position.x < 0) {
      this.position.x = width;
    }
    if (this.position.y > height) {
      this.position.y = 0;
    } else if (this.position.y < 0) {
      this.position.y = height;
    }
    if (this.position.z > height) {
      this.position.z = 0;
    } else if (this.position.z < 0) {
      this.position.z = height;
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

  void show() {
    //stroke(255);
    //noStroke();
    color c = color((xAngle(this.velocity)/PI)*255, (yAngle(this.velocity)/PI)*255, (zAngle(this.velocity)/PI)*255);
    stroke(c);
    noFill();
    strokeWeight(2);
    line(this.position.x, this.position.y, this.position.z, this.position.x+(this.velocity.x*size), this.position.y+(this.velocity.y*size), this.position.z+(this.velocity.z*size));
    //stroke(atan2(velocity.x, velocity.y)*255);
    //ellipse(this.position.x, this.position.y, size, size);
    pushMatrix();
    noStroke();
    fill(c);
    //strokeWeight(1);
    translate(this.position.x, this.position.y, this.position.z);
    sphere(this.size/2);
    popMatrix();
  }

  void showVisual() {
    stroke(this.velocity.x*255, this.velocity.y*255, this.velocity.z*255);
    strokeWeight(2);
    point(this.position.x, this.position.y);
  }
  void showDebug() {
    noFill();
    strokeWeight(alignmentValue*2);
    stroke(255, 0, 0);
    ellipse(this.position.x, this.position.y, alignmentRadius, alignmentRadius);
    strokeWeight(separationValue*2);
    stroke(0, 255, 0);
    ellipse(this.position.x, this.position.y, separationRadius, separationRadius);
    strokeWeight(cohesionValue*2);
    stroke(0, 0, 255);
    ellipse(this.position.x, this.position.y, cohesionRadius, cohesionRadius);
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
