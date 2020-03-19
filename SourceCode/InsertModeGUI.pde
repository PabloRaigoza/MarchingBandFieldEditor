void insertModeLines() {
  if(insertMode) {
    float spacingW = width/100.0;
    float spacingH = (height-buffer*2)/70.0;
    stroke(215,215,215,150);
    for(float i = 0.0; i < 101.0; i++) {
      line(i*spacingW,buffer,i*spacingW,height-buffer);
    }
    for(float i = 0.0; i < 71.0; i++) {
      line(0,spacingH*i+buffer,width,spacingH*i+buffer);
    }
  }
}

float adjustX(float x) {
  float spacingW = width/100.0;
  float adjustedX = x;
  for(float i = 0.0; i < 101.0; i++) {
    if(abs(spacingW*i - x) < 8)
      adjustedX = spacingW*i;
  }
  return adjustedX;
}

float adjustY(float y) {
  float spacingH = (height-buffer*2)/70.0;
  float adjustedY = y;
  for(float i = 0.0; i < 71.0; i++) {
    if(abs(spacingH*i - y) < 8)
      adjustedY = spacingH*i;
  }
  return adjustedY+3;
}















