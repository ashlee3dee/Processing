package g4p.tool.controls;

import java.util.HashSet;
import java.util.Set;

/**
 * Singleton class used to create unique names for variables and event handlers. <br>
 * 
 * Prevents the use of Java keywords and many of the methods defined by Processing.
 * 
 * @author Peter Lager
 *
 */
public final class NameGen {

	private static NameGen instance;
	
	public static NameGen instance(){
		if(instance == null){
			instance = new NameGen();
		}
		return instance;
	}
	
	private Set<String> words;
	
	private NameGen(){
		words = new HashSet<String>();
		addReservedWords();
	}
	
	/**
	 * See if a name is reserved
	 * @param name
	 * @return
	 */
	public boolean used(String name){
		return words.contains(name);
	}

	public void add(String name){
		if(name.length() > 0)
			words.add(name);
	}
	
	public void remove(String name){
		if(name.length() > 0)
			words.remove(name);
	}
	
	public void reset(){
		words.clear();
		addReservedWords();
	}
	
	/**
	 * Get the next available name-number combination and add it to 
	 * the reserved list.
	 * 
	 * @param name
	 * @return
	 */
	public String getNext(String name){
		int nbr = 0;
		do {
			nbr++;
		} while(words.contains(name + nbr));
		name = name + nbr;
		words.add(name);
		return name;
	}
	
	private void addReservedWords(){
		// Java keywords
        String w = "abstract continue for new switch assert default ";
        w += "goto package synchronized boolean do if private this ";
        w += "break double implements protected throw byte else ";
        w += "import public throws case enum instanceof return ";
        w += "transient catch extends int short try char final ";
        w += "interface static void class finally long strictfp ";
        w += "volatile const float native super while ";
        // Processing keywords and stuff
        w += "Sketch_Display SKETCH APPLICATION APPLET ";        
        w += "abs acos alpha ambient ambientLight append applyMatrix ";
        w += "arc Array arrayCopy ArrayList asin atan atan2 background ";
        w += "beginCamera beginRaw beginRecord beginShape bezier ";
        w += "bezierDetail bezierPoint bezierTangent bezierVertex binary ";
        w += "blend blendColor blue box break brightness BufferedReader ";
        w += "camera ceil color color colorMode concat constrain copy ";
        w += "cos createFont createGraphics createImage createInput ";
        w += "createOutput createReader createWriter cursor curve ";
        w += "curveDetail curvePoint curveTangent curveTightness ";
        w += "curveVertex day degrees delay directionalLight dist draw ";
        w += "ellipse ellipseMode emissive endCamera endRaw endRecord ";
        w += "endShape exp expand extends fill filter floor focused ";
        w += "frameCount frameRate frustum get green HashMap height ";
        w += "hex hint hour hue image imageMode implements import ";
        w += "int int join key keyCode keyPressed keyPressed keyReleased ";
        w += "keyTyped lerp lerpColor lightFalloff lights lightSpecular line link ";
        w += "loadBytes loadFont loadImage loadPixels loadShape loadStrings ";
        w += "log loop mag map match matchAll max millis min minute ";
        w += "modelX modelY modelZ month mouseButton mouseClicked ";
        w += "mouseDragged mouseMoved mousePressed mousePressed ";
        w += "mouseReleased mouseX mouseY new nf nfc nfp nfs noCursor ";
        w += "noFill noise noiseDetail noiseSeed noLights noLoop norm normal ";
        w += "noSmooth noStroke noTint null Object online open ortho param ";
        w += "perspective PFont PGraphics PImage pixels[] pmouseX pmouseY ";
        w += "point pointLight popMatrix popStyle pow print printCamera println ";
        w += "printMatrix printProjection PrintWriter PShape public pushMatrix ";
        w += "pushStyle PVector quad radians random randomSeed rect rectMode ";
        w += "red redraw requestImage resetMatrix return reverse rotate ";
        w += "rotateX rotateY rotateZ round saturation save saveBytes saveFrame ";
        w += "saveStream saveStrings scale screen screenX screenY screenZ second ";
        w += "selectFolder selectInput selectOutput set setup shape shapeMode ";
        w += "shearX shearY shininess shorten sin size smooth sort specular ";
        w += "sphere sphereDetail splice split splitTokens spotLight sq sqrt status ";
        w += "str String stroke strokeCap strokeJoin strokeWeight subset tan text ";
        w += "textAlign textAscent textDescent textFont textLeading textMode ";
        w += "textSize texture textureMode textWidth this tint translate triangle ";
        w += "trim true try unbinary unhex updatePixels vertex width XMLElement year ";

        String[] ws = w.split(" ");
        for (String s : ws) {
            words.add(s);
        }
	}
 

}
