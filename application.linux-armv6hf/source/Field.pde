void displayField() {
  stroke(0);
  fill(0);
  strokeWeight(1);
  textSize(24);
  displayHashes();
  displayNumbers();
}

void displayHashes() {
  float spacing = width/100.0;
  stroke(0);
  line(0,buffer,width,buffer);
  line(0,height-buffer,width,height-buffer);
  for(float i = 0.0; i < 101.0; i++) {
    line(i*spacing,buffer,i*spacing,buffer+height/20);
    line(i*spacing,(height-height/20)-buffer,i*spacing,height-buffer);
    if( i % 5 == 0 )
      line(i*spacing, buffer, i*spacing, height-buffer);
  }
}

void displayNumbers() {
  float disHash = width/10;
  float yardCounter = 10;
  boolean goDown = false;
  textAlign(CENTER,CENTER);
  for(float i = 1; i < 10; i++) {
    push();
    translate(i*disHash, ((12*width)/100)+buffer);
    rotate(PI);
    text(floor(yardCounter),0,0);
    pop();
    if(!goDown)
      yardCounter = yardCounter + 10;
    else
      yardCounter = yardCounter - 10;
    if(yardCounter > 50) {
      goDown = true;
      yardCounter = yardCounter - 20;  
    }
  }
  goDown = false;
  yardCounter = 10;
  for(float i = 1; i < 10; i++) {
    text(floor(yardCounter),i*disHash,(height-(12*width)/100)-buffer);
    if(!goDown)
      yardCounter = yardCounter + 10;
    else
      yardCounter = yardCounter - 10;
    if(yardCounter > 50) {
      goDown = true;
      yardCounter = yardCounter - 20;  
    }
  }
}
