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
        matrix[i][j] = new PVector(i1*INTERVAL,j1*INTERVAL,0.0);
      }
    }
  }
  
  void drawMatrix(){
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

  void pushLine(PVector[] newLine){
    for(int i = TIMESIZE-1; i > 0; i--){
      for(int j  = 0; j < BUFFERSIZE; j++){
        matrix[i][j].z = matrix[i-1][j].z;
      }
    }
    matrix[0] = newLine;
  }
  void setColor(int i1, int j1, int i2, int j2){
    r = (RED + (map(matrix[i1][j1].z, 0, 5,0,200)+ map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    g = (GREEN + (map(matrix[i1][j1].z, 0, 5,0,200)+ map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    b = (BLUE + (map(matrix[i1][j1].z, 0, 5,0,200) + map(matrix[i2][j2].z, 0, 5,0,200))/2) % 450;
    stroke(r,g,b,80);
  }
}