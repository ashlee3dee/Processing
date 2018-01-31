float frequency = 1;
float amplitude = 1;
float phase = 0.0;
float samplescale = 2;
float samplescalestep = 0.25;
float waveoffset = 1;
void setup()
{
  size(512, 512);
  //smooth(2);
}

void draw() 
{
  background(0);

  pushMatrix();
  noFill();
  stroke(255, 0, 0);
  translate(0, height/3);
  text("Carrier Wave", 10, height/12);
  beginShape();
  for (float t = 0.0; t < 1.0; t+=1.0/(width*samplescale))
  {
    float wave1 = sin(((t+phase)*(2*PI))*frequency)*amplitude;
    vertex((t*width), wave1);
  }
  endShape();
  translate(0, height/6);
  beginShape();
  stroke(0, 255, 0);
  for (float t = 0.0; t < 1.0; t+=1.0/(width*samplescale))
  {
    float wave2 = sin(((t+phase)*(2*PI))*(frequency+waveoffset))*amplitude;
    vertex((t*width), wave2);
  }
  endShape();
  translate(0, height/6);
  beginShape();
  stroke(0, 0, 255);
  for (float t = 0.0; t < 1.0; t+=1.0/(width*samplescale))
  {
    float carrier = sin(((t+phase)*(2*PI))*frequency)*amplitude;
    float modulator = sin(((t+phase)*(2*PI))*(frequency+waveoffset))*amplitude;
    float wave = carrier + modulator;
    vertex((t*width), wave);
  }
  endShape();
  popMatrix();

  textSize(10);
  fill(255);
  noStroke();
  text("Frequency (Hz): " + frequency, 10, 10);
  text("Amplitude: " + amplitude, 10, 20);
  text("Phase: " + phase, 10, 30);
  text("Wave Offset: " + waveoffset, 10, 40);
  text("Multisampling Factor: " + samplescale, 10, 50);
  phase+=0.01;
}

void keyPressed() {
  // frequency modifier
  if (key == 'q')
  {
    frequency += 1;
  }
  if (key == 'a')
  {
    frequency -= 1;
  }
  // amplitude modifier
  if (key == 'w')
  {
    amplitude += 10;
  }
  if (key == 's')
  {
    amplitude -= 10;
  }
  // phase modifier
  if (key == 'e')
  {
    phase += PI/frequency;
  }
  if (key == 'd')
  {
    phase -= PI/frequency;
  }
  // sampling modifier
  if (key == 'r')
  {
    samplescale += samplescalestep;
  }
  if (key == 'f' & samplescale > samplescalestep)
  {
    samplescale -= samplescalestep;
  }
  // wave offset modifier
  if (key == 't')
  {
    waveoffset++;
  }
  if (key == 'g')
  {
    waveoffset--;
  }
}