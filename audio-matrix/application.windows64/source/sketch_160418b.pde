import ddf.minim.*;    // Minim AudioPlayer
import ddf.minim.analysis.*;  //FFT
import peasy.*;  // PeasyCam
final static int TIMESIZE = 60;
final static int BUFFERSIZE = 30;
final static float HEIGHT = 5;
final static float INTERVAL = 1.5;
boolean isStop = false;
float BASERED = 255.0;
float BASEGREEN = 255.0;
float BASEBLUE = 255.0;
float RED = 255.0;
float GREEN = 97.0;
float BLUE = 0.0;
float angle = 0.0;
int range = 100;
float bandWidth;
Minim minim;
AudioPlayer groove;
PeasyCam cam;
Matrix mt;
FFT fft;
void setup(){
  size(800, 800, P3D);
  colorMode(RGB);
  noFill();
  frameRate = 60;
  smooth();
  
  cam = new PeasyCam(this,range);
  minim = new Minim(this);
  groove = minim.loadFile("SchoolChimes.mp3");
  groove.loop();

  mt = new Matrix();
  fft = new FFT(groove.bufferSize(),groove.sampleRate());
  bandWidth = fft.getBandWidth();
  mt.drawMatrix();
 
}

void draw(){
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
      newLine[j] = new PVector(0.0, j * INTERVAL, map(fft.getBand(j-1),0,bandWidth,0,HEIGHT));
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

void mousePressed(){
}

void keyPressed(){
  if(key == 't'){
    angle += 0.05;
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

void changeColor(){
  RED = map(sin(angle*2.5), -1, 1, 0, 1) * BASERED;
  GREEN =map(sin(angle*2.5 + PI/2), -1, 1, 0, 1) * BASEGREEN;
  BLUE = map(sin(angle*2.5 - PI/2), -1, 1, 0, 1) * BASEBLUE;
}
void stop(){
  groove.close();
  minim.stop();
  super.stop();
}