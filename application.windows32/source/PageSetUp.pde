void resizable() {
  surface.setLocation((displayWidth/2)-width/2, (displayHeight/2)-height/2);
  surface.setTitle("Hello World!");
  surface.setResizable(true);
}

void prettyScreen() {
  stroke(0);
  line(width-1,buffer,width-1,height-1);
}

boolean insideRect( float x, float y, float rectX, float rectY, float w, float h ){
  return x < rectX+w && x > rectX && y < rectY+h && y > rectY;
}

boolean insideCircle( float x, float y, float circleX, float circleY, float circleR ){
   return dist( x,y,circleX,circleY ) <= circleR;
}

void saveCurrentSet() {
  PrintWriter output;
  output = createWriter(currentSet.file);
  for(int i = 0 ; i < currentSet.setBand.size(); i++) {
    boolean ghost = currentSet.setBand.get(i).ghostSet;
    boolean name = currentSet.setBand.get(i).nameSet;
    
    float outPutGhostX = -1.0;
    float outPutGhostY = -1.0;
    String outPutName = "noName";
    if(ghost) {
      outPutGhostX = currentSet.setBand.get(i).ghostX/width;
      outPutGhostY = currentSet.setBand.get(i).ghostY/height;
    }
    if(name)
      outPutName = currentSet.setBand.get(i).name.msg;

    output.println( currentSet.setBand.get(i).x/width + " " + //playerX
                    currentSet.setBand.get(i).y/height + " " +//playerY
                    outPutGhostX + " " +                      //ghostX
                    outPutGhostY + " " +                      //ghostY
                    outPutName);                              //name
  }
  output.flush(); // Writes the remaining data to the file
  output.close(); // Finishes the file
  if(currentSet.beatsSet && showPicked){
    String[] state = loadStrings(show+"/"+show+"Beats.txt");
    String[] fileList = loadStrings(show+"/"+show+"Sets.txt");
    PrintWriter beatsWriter = createWriter(show+"/"+show+"Beats.txt");
    for(int i = 0; i < fileList.length; i++) {
      if((show+"/"+fileList[i]).equals(currentSet.file)) {
        state[i-1] = str(currentSet.allowedBeats);
      }
    }
    for(int i = 0; i < state.length; i++) {
      beatsWriter.println(state[i]);
    }
    beatsWriter.flush(); // Writes the remaining data to the file
    beatsWriter.close(); // Finishes the file
  }
}








void pickShow() {
  String[] lines = loadStrings("ShowList.txt");
  Button[] userChoices = new Button[lines.length];
  int sections = (height/(lines.length+1));
  if(!recordingNewShow) {
    Button addNewShow = new Button( "+", 0, 0, width, sections );
    addNewShow.fontSize = 60;
    addNewShow.show();
    if(addNewShow.clicked())
      recordingNewShow = true;
  }
  for (int i = 0 ; i < lines.length; i++) {
    userChoices[i] = new Button(lines[i], 0,sections*(i+1),width,sections);
    userChoices[i].fontSize = 36;
    userChoices[i].show();
    if(userChoices[i].clicked()){
      show = lines[i];
      showPicked = true;
      
      currentSet = new Set(show+"/"+show+"Set1.txt");
      currentSet.loadFromFile();
    }
  }
}










//**ON FIELD**

void toggleAddLeft() {
  if(addLeft)
    addLeft = false;
  else
    addLeft = true;
}

void toggleMinusLeft() {
  if(minusLeft)
    minusLeft = false;
  else
    minusLeft = true;
}

void showsMenu() {
  String[] lines = loadStrings(show+"/"+show+"Sets.txt");
  int numSet = Integer.parseInt(lines[0]);
  Button[] buttonSets = new Button[numSet]; 
  int section = width/(numSet+1);
  Button addSet;
  Button minusSet;
  
  
  if(addLeft)
    addSet = new Button("+",0,0,section/4,floor(buffer));
  else
    addSet = new Button("+",section/4,0,section/4,floor(buffer));
  
  if(addSet.clicked() && !insertMode && !recordingMode && !deleteMode) {
    addSet();
    toggleAddLeft();
  }
  
  
  if(minusLeft)
    minusSet = new Button("-",section/2,0,section/4,floor(buffer));
  else
    minusSet = new Button("-",(section/2)+(section/4),0,section/4,floor(buffer));
  if(minusSet.clicked() && !insertMode && !recordingMode && !deleteMode) {
    minusSet();
    toggleMinusLeft();
  }
  
  
  addSet.show();
  minusSet.show();
  for (int i = 0 ; i < numSet; i++) {
    buttonSets[i] = new Button("Set"+(i+1), section*(i+1),0,section,floor(buffer));
    if((show+"/"+lines[i+1]).equals(currentSet.file)) {
      buttonSets[i].c = color(150,150,150);
      if(currentSet.beatsSet)
        buttonSets[i].msg = buttonSets[i].msg+"-"+floor(currentSet.allowedBeats);
      else
        buttonSets[i].msg = buttonSets[i].msg+"-"+floor(currentSet.allowedBeats);
    }
    buttonSets[i].show();
    if(buttonSets[i].clicked() && !insertMode && !recordingMode && !deleteMode){
      currentSet = new Set(show+"/"+lines[i+1]);
      currentSet.loadFromFile();
      currentSet.show();
    }
  }
}

void addSet() {
  String[] recordedBeats = loadStrings(show+"/"+show+"Beats.txt");
  PrintWriter beatsRecord = createWriter(show+"/"+show+"Beats.txt");
  for(int i = 0; i < recordedBeats.length+1; i++) {
    if(i < recordedBeats.length)
      beatsRecord.println(recordedBeats[i]);
    else
      beatsRecord.println("0.0");
  }
  beatsRecord.flush(); // Writes the remaining data to the file
  beatsRecord.close(); // Finishes the file
  String[] fileList = loadStrings(show+"/"+show+"Sets.txt");
  PrintWriter listFiles = createWriter(show+"/"+show+"Sets.txt");
  for(int i = 0; i < fileList.length+1; i++) {
    if( i == 0 )
      listFiles.println( Integer.parseInt(fileList[i])+1 );
    else if (i < fileList.length)
      listFiles.println(fileList[i]);
    else
      listFiles.println(show+"Set"+fileList.length+".txt");
  }
  listFiles.flush();
  listFiles.close();
  PrintWriter makeNewFile = createWriter(show+"/"+show+"Set"+fileList.length+".txt");
  makeNewFile.flush();
  makeNewFile.close();
}

void minusSet() {
  String[] fileList = loadStrings(show+"/"+show+"Sets.txt");
  if( fileList.length != 2 ) {
    String[] recordedBeats = loadStrings(show+"/"+show+"Beats.txt");
    PrintWriter beatsRecord = createWriter(show+"/"+show+"Beats.txt");
    for(int i = 0; i < recordedBeats.length-1; i++) {
        beatsRecord.println("0.0");
    }
    beatsRecord.flush();
    beatsRecord.close();
    
    PrintWriter listFiles = createWriter(show+"/"+show+"Sets.txt");
    for(int i = 0; i < fileList.length-1; i++) {
      if( i == 0 )
        listFiles.println( Integer.parseInt(fileList[i])-1);
      else
        listFiles.println(fileList[i]);
    }
    listFiles.flush();
    listFiles.close();
  }
}

void displayMode() {
  String modeMsg = "";
  if( insertMode )
    modeMsg = "INSERT";
  if( deleteMode )
    modeMsg = "DELETE";
  Label modeLabel = new Label( modeMsg, 0, height );
  modeLabel.p1Left = true;
  modeLabel.p2Bottom = true;
  modeLabel.c = color(0);
  modeLabel.show();
}

void addNewPlayer() {
  if( mouseButton == LEFT && insertMode && showPicked && insideRect(mouseX,mouseY,0,floor(buffer),width,height-(buffer*2)) ) {
    float newPlayerX = adjustX(mouseX);
    float newPlayerY = adjustY(mouseY);
    currentSet.setBand.add( new Player(newPlayerX, newPlayerY) );
  }
}

void deletePlayer() {
  if(mouseButton == LEFT && showPicked && deleteMode && showPicked) {
    for( int i = 0 ; i < currentSet.setBand.size(); i++ ) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r ))
        currentSet.setBand.remove(i);
    }
  }
}

void toggleInsertMode() {
  if(insertMode)
    insertMode = false;
   else
     insertMode = true;
}
void toggleDeleteMode() {
  if(deleteMode)
    deleteMode = false;
  else
    deleteMode = true;
}
void toggleRecording(){
  if(recordingMode)
    recordingMode = false;
   else
    recordingMode = true;
}



void selectPlayer() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r ))
        currentSet.setBand.get(i).selected = true;
    }
  }
}

void dragPlayer() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).selected){
        circle( mouseX, mouseY, currentSet.setBand.get(i).r );
        draggingGhost = true;
      }    
    }
  }
}

void selectLabel() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).nameSet) {
        if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).name.x, currentSet.setBand.get(i).name.y, currentSet.setBand.get(i).r )) {
          currentSet.setBand.get(i).labelSelected = true;
        }
      }
    }
  }
}

void dragLabel() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).nameSet) {
        if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).name.x, currentSet.setBand.get(i).name.y, currentSet.setBand.get(i).r )) {
          currentSet.setBand.get(i).name.x = mouseX;
          currentSet.setBand.get(i).name.y = mouseY;
          draggingLabel = true;
        }
      }
    }
  }
}

void createGhost() {
  for(int i = 0; i < currentSet.setBand.size(); i++) {
    if(currentSet.setBand.get(i).selected) {
      if(currentSet.setBand.get(i).paceSet) {
        currentSet.setBand.get(i).resetPace();
      }
      currentSet.setBand.get(i).setGhost(mouseX,mouseY);
      draggingGhost = false;
      currentSet.setBand.get(i).selected = false;
    }
  }
}

void loadPreviousFile() {
  saveCurrentSet();
  String[] lines = loadStrings(show+"/"+show+"Sets.txt");
  int index = -1;
  for(int i = 0; i < lines.length; i++) {
    if((show+"/"+lines[i]).equals(currentSet.file)) {
      if(i >= 2 ) {
        index = i-1;
      }
    }
  }
  if(index != -1) {
    currentSet.loadFromFile(show+"/"+lines[index]);
  }
}

void displayPlayButton() {
  Button playShow = new Button("Play",floor(width-buffer*4),floor(height-buffer),floor(buffer*4), floor(buffer));
  playShow.show();
  if(readyToRun && playShow.clicked()){
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).ghostSet && !currentSet.setBand.get(i).paceSet){
        currentSet.setBand.get(i).setPace( currentSet.allowedBeats );
      }
    }
    runShow = true;
    saveCurrentSet();
  }
}

void displayPlayAll() {
  Button playAll = new Button("Play All",floor(width-buffer*8),floor(height-buffer),floor(buffer*4),floor(buffer));
  playAll.show();
  if(readyToRun && playAll.clicked()){
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).ghostSet && !currentSet.setBand.get(i).paceSet){
        currentSet.setBand.get(i).setPace( currentSet.allowedBeats );
      }
    }
    runningAll = true;
    currentSet = new Set(show+"/"+show+"Set1.txt");
    currentSet.loadFromFile();
    saveCurrentSet();
  }
}
