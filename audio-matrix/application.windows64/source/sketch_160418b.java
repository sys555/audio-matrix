import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_160418b extends PApplet {

    // Minim AudioPlayer
  //FFT
  // PeasyCam
final static int TIMESIZE = 60;
final static int BUFFERSIZE = 30;
final static float HEIGHT = 5;
final static float INTERVAL = 1.5f;
boolean isStop = false;
float BASERED = 255.0f;
float BASEGREEN = 255.0f;
float BASEBLUE = 255.0f;
float RED = 255.0f;
float GREEN = 97.0f;
float BLUE = 0.0f;
float angle = 0.0f;
int range = 100;
float bandWidth;
Minim minim;
AudioPlayer groove;
PeasyCam cam;
Matrix mt;
FFT fft;
public void setup(){
  
  colorMode(RGB);
  noFill();
  frameRate = 60;
  
  
  cam = new PeasyCam(this,range);
  minim = new Minim(this);
  groove = minim.loadFile("SchoolChimes.mp3");
  groove.loop();

  mt = new Matrix();
  fft = new FFT(groove.bufferSize(),groove.sampleRate());
  bandWidth = fft.getBandWidth();
  mt.drawMatrix();
 
}

public void draw(){
   translate(-30,-30,0);
   if(!isStop){
        if(frameCount%3 == 0){
    background(0);
    fft.forward(groove.mix);
    mt.drawMatrix();
    stroke(255,97,0,20);
    PVector[] newLine = new PVector[BUFFERSIZE];
    for(int i = 0,j = 0; i < groove.bufferSize() - 24; i+=34, j++){
      //line(i, 50 + groove.left.get(i) * 50, i + 1, 50 + groove.left.get(i+1) * 50);
      newLine[j] = new PVector(0.0f, j * INTERVAL, map(fft.getBand(j-1),0,bandWidth,0,HEIGHT));
    }
    mt.pushLine(newLine); 
    }
   }else{
     background(0);
    //fft.forward(groove.mix);
    mt.drawMatrix();
    stroke(255,97,0,20);
   }

}

public void mousePressed(){
}

public void keyPressed(){
  if(key == 't'){
    angle += 0.05f;
    changeColor();
  }
  else if(key == 's'){
    isStop = !isStop;
    if ( groove.isPlaying() ) {
        groove.pause();
      }
      else{
        groove.loop();
      }
  }
  else if(key == 'r'){
    RED = 255;
    GREEN =97;
    BLUE = 0;
  }
}

public void changeColor(){
  RED = map(sin(angle*2.5f), -1, 1, 0, 1) * BASERED;
  GREEN =map(sin(angle*2.5f + PI/2), -1, 1, 0, 1) * BASEGREEN;
  BLUE = map(sin(angle*2.5f - PI/2), -1, 1, 0, 1) * BASEBLUE;
}
public void stop(){
  groove.close();
  minim.stop();
  super.stop();
}
class Matrix{
  PVector[][] matrix = new PVector[TIMESIZE][BUFFERSIZE];
  int i1;
  int j1;
  float r;
  float g;
  float b;
  Matrix(){
    for(int i = 0, i1 = 0; i < TIMESIZE; i++, i1++){
      for(int j = 0, j1 = 0; j < BUFFERSIZE; j++, j1++){
        matrix[i][j] = new PVector(i1*INTERVAL,j1*INTERVAL,0.0f);
      }
    }
  }
  
  public void drawMatrix(){
    stroke(150 + map(matrix[0][0].z, 0, 5,0,100),125,150,100);
    for(int i = 0; i < TIMESIZE; i++){  // y 
      beginShape();
      curveVertex(matrix[i][0].x,matrix[i][0].y,matrix[i][0].z);
      for(int j = 0; j < BUFFERSIZE; j++){
        if(j == 0){
          setColor(i,j+1,i,j);
        }
        else{
          setColor(i,j-1,i,j);
        }
        strokeWeight(2);
        curveVertex(matrix[i][j].x,matrix[i][j].y,matrix[i][j].z);
      }
      curveVertex(matrix[i][BUFFERSIZE-1].x,matrix[i][BUFFERSIZE-1].y,matrix[i][BUFFERSIZE-1].z);
      endShape();
    }
   
    for(int i = 0; i < TIMESIZE; i++){  //bianzhi1
      beginShape();
      curveVertex(matrix[i][0].x,matrix[i][0].y,matrix[i][0].z);
      for(int j = 0; j < BUFFERSIZE; j++){
        if(j%2 == 0){
          if(i != TIMESIZE - 1 && j != BUFFERSIZE - 1)
            setColor(i,j,i+1,j+1);
          else if(j != 0)
            setColor(i,j,i-1,j-1);
          else
            setColor(i,j,i-1,j+1);
          curveVertex(matrix[i][j].x,matrix[i][j].y,matrix[i][j].z);
        }
        else if(i != TIMESIZE-1 && j != BUFFERSIZE -1){
          if(i != 0)
            setColor(i,j,i-1,j-1);
          else
            setColor(i,j,i,j-1);
          curveVertex(matrix[i+1][j].x,matrix[i+1][j].y,matrix[i+1][j].z);
        }
        else if(j == BUFFERSIZE -1 && i != TIMESIZE -1)
        {
           curveVertex(matrix[i+1][j].x,matrix[i+1][j].y,matrix[i+1][j].z);
        }
      }
        curveVertex(matrix[i][BUFFERSIZE-1].x,matrix[i][BUFFERSIZE-1].y,matrix[i][BUFFERSIZE-1].z);
      endShape();
    }
    
    for(int i = 0; i < TIMESIZE; i++){  //bianzhi2
     beginShape();
     curveVertex(matrix[i][0].x,matrix[i][0].y,matrix[i][0].z);
     for(int j = 0; j < BUFFERSIZE; j++){
       if(j%2 != 0){
         if(i == TIMESIZE -1)
           setColor(i-1,j-1,i,j);
         else
           setColor(i+1,j-1,i,j);
         curveVertex(matrix[i][j].x,matrix[i][j].y,matrix[i][j].z);
       }
       else if(i != TIMESIZE-1 && j != BUFFERSIZE -1){
         if(j == 0 || i == 0)
           setColor(i+1,j,i,j+1);
         else
           setColor(i-1,j-1,i,j);
         curveVertex(matrix[i+1][j].x,matrix[i+1][j].y,matrix[i+1][j].z);
       }
     }
     curveVertex(matrix[i][BUFFERSIZE-1].x,matrix[i][BUFFERSIZE-1].y,matrix[i][BUFFERSIZE-1].z);
     endShape();
    }
    
    for(int j = 0; j < BUFFERSIZE; j++){  //x
     beginShape();
     curveVertex(matrix[0][j].x,matrix[0][j].y,matrix[0][j].z);
     for(int i = 0; i < TIMESIZE; i++){
       if(i == 0){
         setColor(i+1,j,i,j);
       }
       else{
         setColor(i-1,j,i,j);
       }
       curveVertex(matrix[i][j].x,matrix[i][j].y,matrix[i][j].z);
     }
     curveVertex(matrix[TIMESIZE-1][j].x,matrix[TIMESIZE-1][j].y,matrix[TIMESIZE-1][j].z);
     endShape();
    }
  }

  public void pushLine(PVector[] newLine){
    for(int i = TIMESIZE-1; i > 0; i--){
      for(int j  = 0; j < BUFFERSIZE; j++){
        matrix[i][j].z = matrix[i-1][j].z;
      }
    }
    matrix[0] = newLine;
  }
  public void setColor(int i1, int j1, int i2, int j2){
    r = (RED + (map(matrix[i1][j1].z, 0, 5,0,200)+ map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    g = (GREEN + (map(matrix[i1][j1].z, 0, 5,0,200)+ map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    b = (BLUE + (map(matrix[i1][j1].z, 0, 5,0,200) + map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    stroke(r,g,b,80);
  }
}
  public void settings() {  size(800, 800, P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_160418b" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
