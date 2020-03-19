import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Band extends PApplet {

boolean showPicked = false;
String show;
float buffer = (height-(width/2))/2;
Set currentSet;
boolean insertMode = false;
boolean deleteMode = false;
boolean recordingMode = false;
boolean recordBeats = false;
boolean justSwitchedRecording = false;
String record = "";
boolean draggingGhost = false;
boolean draggingLabel = false;
int beatsCounter = 0;
int totalBeats = 0;
boolean readyToRun = false;
boolean runShow = false;
boolean addLeft = true;
boolean minusLeft = true;
boolean recordingNewShow = false;
boolean runningAll = false;






public void setup() {
  
}

public void draw() {
  background(255);
  if(!showPicked) {
    pickShow();
    if(recordingNewShow) {
      recordingNewShow();
    }
  }
  else{
    insertModeLines();
    displayField();
    showsMenu();
    currentSet.show();
    displayMode();
    recordingName();
    recordingBeats();
    playShow();
    playAllShows();
    prettyScreen();
    if(!runShow && !runningAll) {
      displayPlayButton();
      displayPlayAll();
    }
  }
}

public void mousePressed() {
  if(insertMode)
    addNewPlayer();
  else if(deleteMode)
    deletePlayer();
  else if( mouseButton == RIGHT )
    makeRecording();
  else if( mouseButton == LEFT ) {
    selectPlayer();
    selectLabel();
  }
}

public void mouseDragged() {
  if(mouseButton == LEFT){
    dragPlayer();
    dragLabel();
  }
}

public void mouseReleased() {
  if(draggingGhost)
    createGhost();
  if(draggingLabel)
    draggingLabel = false;
}

public void keyPressed() {
  if(recordingNewShow)
    updateNewShow();
  if(recordingMode && showPicked)
    updateRecording();
  else if(recordBeats && showPicked)
    updateRecordBeats();
  else if( key == 's' && showPicked)
    saveCurrentSet();
  else if( key == 'i' && !deleteMode && showPicked)
    toggleInsertMode();
  else if( key == 'd' && !insertMode && showPicked)
    toggleDeleteMode();
  else if( key == 'b' && !insertMode && !deleteMode && !recordingMode && showPicked ) 
    toggleRecordBeatsMode();
  else if( key == 'r' && !insertMode && !deleteMode && !recordingMode && showPicked ) 
    loadPreviousFile();
  else if( key == 'p' && showPicked )
    saveFrame( show+"Pictures/"+show+"#####.png" );
}
class Button {
  String msg;
  int x,y,w,h;
  int fontSize = 12;
  int c = color(255,255,255);
  int textColor = color(0);
  Button( String _msg, int _x, int _y, int _w, int _h ) {
    msg = _msg;
    x = _x;
    y = _y;
    w = _w;
    h = _h;
  }
  
  public void show() {
    fill(c);
    rect(x,y,w,h);
    fill(textColor);
    textSize(fontSize);
    textAlign(CENTER, CENTER);
    text(msg, x+w/2, y+h/2);
  }
  
  public boolean clicked() {
    return mouseX < x+w && mouseX > x && mouseY < y+h && mouseY > y && mousePressed; 
  }
  public boolean hovering() {
    return mouseX < x+w && mouseX > x && mouseY < y+h && mouseY > y;
  }
}
public void displayField() {
  stroke(0);
  fill(0);
  strokeWeight(1);
  textSize(24);
  displayHashes();
  displayNumbers();
}

public void displayHashes() {
  float spacing = width/100.0f;
  stroke(0);
  line(0,buffer,width,buffer);
  line(0,height-buffer,width,height-buffer);
  for(float i = 0.0f; i < 101.0f; i++) {
    line(i*spacing,buffer,i*spacing,buffer+height/20);
    line(i*spacing,(height-height/20)-buffer,i*spacing,height-buffer);
    if( i % 5 == 0 )
      line(i*spacing, buffer, i*spacing, height-buffer);
  }
}

public void displayNumbers() {
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
public void insertModeLines() {
  if(insertMode) {
    float spacingW = width/100.0f;
    float spacingH = (height-buffer*2)/70.0f;
    stroke(215,215,215,150);
    for(float i = 0.0f; i < 101.0f; i++) {
      line(i*spacingW,buffer,i*spacingW,height-buffer);
    }
    for(float i = 0.0f; i < 71.0f; i++) {
      line(0,spacingH*i+buffer,width,spacingH*i+buffer);
    }
  }
}

public float adjustX(float x) {
  float spacingW = width/100.0f;
  float adjustedX = x;
  for(float i = 0.0f; i < 101.0f; i++) {
    if(abs(spacingW*i - x) < 8)
      adjustedX = spacingW*i;
  }
  return adjustedX;
}

public float adjustY(float y) {
  float spacingH = (height-buffer*2)/70.0f;
  float adjustedY = y;
  for(float i = 0.0f; i < 71.0f; i++) {
    if(abs(spacingH*i - y) < 8)
      adjustedY = spacingH*i;
  }
  return adjustedY+3;
}
















class Label {
  String msg;
  int x,y;
  int fontSize = 12;
  int c = color(0);
  boolean p1Left, p1Right, p1Center, p2Top, p2Center, p2Bottom;
  Label( String _msg, int _x, int _y ) {
    msg = _msg;
    x = _x;
    y = _y;
  }
  public void show() {
    if(p1Left && p2Top)
      textAlign(LEFT,TOP);
    else if(p1Left && p2Bottom)
      textAlign(LEFT, BOTTOM);
    else if(p1Center && p2Center)
      textAlign(CENTER, CENTER);
    else if(p1Center && p2Bottom)
      textAlign(CENTER, BOTTOM);
    else if(p1Center && p2Top)
      textAlign(CENTER, TOP);
    fill(c);
    textSize(fontSize);
    text(msg, x, y);
  }
}
public void resizable() {
  surface.setLocation((displayWidth/2)-width/2, (displayHeight/2)-height/2);
  surface.setTitle("Hello World!");
  surface.setResizable(true);
}

public void prettyScreen() {
  stroke(0);
  line(width-1,buffer,width-1,height-1);
}

public boolean insideRect( float x, float y, float rectX, float rectY, float w, float h ){
  return x < rectX+w && x > rectX && y < rectY+h && y > rectY;
}

public boolean insideCircle( float x, float y, float circleX, float circleY, float circleR ){
   return dist( x,y,circleX,circleY ) <= circleR;
}

public void saveCurrentSet() {
  PrintWriter output;
  output = createWriter(currentSet.file);
  for(int i = 0 ; i < currentSet.setBand.size(); i++) {
    boolean ghost = currentSet.setBand.get(i).ghostSet;
    boolean name = currentSet.setBand.get(i).nameSet;
    
    float outPutGhostX = -1.0f;
    float outPutGhostY = -1.0f;
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








public void pickShow() {
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

public void toggleAddLeft() {
  if(addLeft)
    addLeft = false;
  else
    addLeft = true;
}

public void toggleMinusLeft() {
  if(minusLeft)
    minusLeft = false;
  else
    minusLeft = true;
}

public void showsMenu() {
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

public void addSet() {
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

public void minusSet() {
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

public void displayMode() {
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

public void addNewPlayer() {
  if( mouseButton == LEFT && insertMode && showPicked && insideRect(mouseX,mouseY,0,floor(buffer),width,height-(buffer*2)) ) {
    float newPlayerX = adjustX(mouseX);
    float newPlayerY = adjustY(mouseY);
    currentSet.setBand.add( new Player(newPlayerX, newPlayerY) );
  }
}

public void deletePlayer() {
  if(mouseButton == LEFT && showPicked && deleteMode && showPicked) {
    for( int i = 0 ; i < currentSet.setBand.size(); i++ ) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r ))
        currentSet.setBand.remove(i);
    }
  }
}

public void toggleInsertMode() {
  if(insertMode)
    insertMode = false;
   else
     insertMode = true;
}
public void toggleDeleteMode() {
  if(deleteMode)
    deleteMode = false;
  else
    deleteMode = true;
}
public void toggleRecording(){
  if(recordingMode)
    recordingMode = false;
   else
    recordingMode = true;
}



public void selectPlayer() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r ))
        currentSet.setBand.get(i).selected = true;
    }
  }
}

public void dragPlayer() {
  if( mouseButton == LEFT && !deleteMode && !insertMode && !recordingMode && showPicked) {
    for(int i = 0; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).selected){
        circle( mouseX, mouseY, currentSet.setBand.get(i).r );
        draggingGhost = true;
      }    
    }
  }
}

public void selectLabel() {
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

public void dragLabel() {
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

public void createGhost() {
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

public void loadPreviousFile() {
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

public void displayPlayButton() {
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

public void displayPlayAll() {
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
class Player {
  float x,y;
  float r = 8;
  int c = color(0);
  Label name;
  boolean labelSelected = false;
  boolean selected = false;
  boolean nameSet = false;
  
  float ghostX = -1.0f;
  float ghostY;
  boolean ghostSet = false;
  int ghostColor = color(200,200,200);
  
  ArrayList<Position> pace = new ArrayList<Position>();
  boolean paceSet = false;
  float tX, tY;
  int beatsStepped = 0;
  Player(float _x, float _y) {
    x = _x;
    y = _y;
    tX = _x;
    tY = _y;
  }
  public void show() {
    fill(c);
    circle(tX,tY,r);
    if(nameSet && tX == x && tY == y && beatsStepped == 0){
      name.show();
    }
    if(ghostSet){
      fill(ghostColor);
      circle( ghostX, ghostY, r );
      line(tX,tY,ghostX,ghostY);
    }
  }
  public void setTheName( String title ) {
    name = new Label(title,floor(x),floor(y-(r*2)));
    name.p1Center = true;
    name.p2Center = true;
    nameSet = true;
  }
  public void setGhost( float _x, float _y ) {
    ghostX = _x;
    ghostY = _y;
    ghostSet = true;
  }
  public void setPace( float beats ) {
    float xIntervals = (ghostX-x)/beats;
    float yIntervals = (ghostY-y)/beats;
    for(int i = 0; i < beats+1; i++) {
      pace.add( new Position( i*xIntervals+x, i*yIntervals+y ) );
    }
    paceSet = true;
    beatsStepped = 0;
  }
  public void oneBeat() {
    tX = pace.get(beatsStepped).x;
    tY = pace.get(beatsStepped).y;
    if( beatsStepped < pace.size()-1 )
      beatsStepped++;
    else {
      tX = x;
      tY = y;
      beatsStepped = 0;
    }
  }
  public void resetPace() {
    pace.clear();
    beatsStepped = 0;
    tX = x;
    tY = y;
    paceSet = false;
  }
}
class Position {
  float x,y;
  Position( float _x, float _y ) {
    x = _x;
    y = _y;
  }
}
public void toggleRecordBeatsMode() {
  if(recordBeats)
    recordBeats = false;
  else{
    recordBeats = true;
  }
}

public void updateRecordBeats() {
  if( key == ' ' ){
    if(record.length() == 0) {
      toggleRecordBeatsMode();
    }
    else {
      recordBeats = false;
      updateBeats();
    }
  }
  else if( keyCode == BACKSPACE ){
    if( record.length() > 0 )
      record = record.substring(0,record.length()-1);
  }
  else if( key == '1' || key == '2' || key == '3' || key == '4' || key == '5' || key == '6' || key == '7' || key == '8' || key == '9' || key == '0')
    record = record + str(key);
  else if(key == 'b')
    toggleRecordBeatsMode();
}

public void recordingBeats() {
  if(recordBeats){
    Label recording = new Label( "RECORDING-BEATS", floor(width/2), floor(height-(buffer/2)) );
    if(record.length() > 0)
      recording.msg = record;
    recording.p1Center = true;
    recording.p2Center = true;
    recording.c = color(0);
    recording.show();
  }
}

public void updateBeats() {
  currentSet.allowedBeats = Float.parseFloat(record);
  for(int i = 0; i < currentSet.setBand.size(); i++) {
    if(currentSet.setBand.get(i).paceSet)
      currentSet.setBand.get(i).resetPace();
    if(currentSet.setBand.get(i).ghostSet)
      currentSet.setBand.get(i).setPace( currentSet.allowedBeats );
  }
  record = "";
  readyToRun = true;
  currentSet.beatsSet = true;
}

public void moveAllPlayers() {
  for(int i = 0; i < currentSet.setBand.size(); i++) {
    if(currentSet.setBand.get(i).paceSet){
      currentSet.setBand.get(i).oneBeat();
      if(currentSet.setBand.get(i).beatsStepped == 0){
        runShow = false;
      }
    }
  }
}

public void playShow() {
  if(runShow){
    if(totalBeats < 30)
      totalBeats++;
    else {
      totalBeats = 0;
      moveAllPlayers();
    }
  }
}

public boolean movePlayers() {
  for(int i = 0; i < currentSet.setBand.size(); i++) {
    if(currentSet.setBand.get(i).paceSet){
      currentSet.setBand.get(i).oneBeat();
      if(currentSet.setBand.get(i).beatsStepped == 0){
        return false;
      }
    }
  }
  return true;
}
public void playAllShows() {
  if(runningAll) {
    if(totalBeats < 30)
      totalBeats++;
    else {
      totalBeats = 0;
      if(!movePlayers()) {
        String[] showList = loadStrings(show+"/"+show+"Sets.txt");
        int index = -1;
        for(int i = 0; i < showList.length; i++) {
          if((show+"/"+showList[i]).equals(currentSet.file) ) {
            index = i+1;
          }
        }
        String[] showBeats = loadStrings(show+"/"+show+"Beats.txt");
        if(index == showList.length || showBeats[index-1].equals("0.0")) {
          runningAll = false;
          currentSet = new Set(show+"/"+show+"Set1.txt");
          currentSet.loadFromFile();
        }
        else {
          currentSet = new Set(show+"/"+showList[index]);
          currentSet.loadFromFile();
          for(int i = 0; i < currentSet.setBand.size(); i++) {
            if(currentSet.setBand.get(i).ghostSet && !currentSet.setBand.get(i).paceSet){
              currentSet.setBand.get(i).setPace( currentSet.allowedBeats );
            }
          }
        }
      }
      
    }
  }
}
public void updateRecording() {
  if( key == ' ' ){
    recordingMode = false;
    justSwitchedRecording = true;
  }
  else if( keyCode == BACKSPACE ){
    if( record.length() > 0 )
      record = record.substring(0,record.length()-1);
  }
  else
    record = record + str(key);
}

public void recordingName() {
  if(recordingMode){
    Label recording = new Label( "RECORDING", floor(width/2), floor(height-(buffer/2)) );
    if(record.length() > 0)
      recording.msg = record;
    recording.p1Center = true;
    recording.p2Center = true;
    recording.c = color(0);
    recording.show();
  }
  if(justSwitchedRecording){
    for(int i = 0 ; i < currentSet.setBand.size(); i++) {
      if(currentSet.setBand.get(i).selected) {
        currentSet.setBand.get(i).setTheName( record );
        record = "";
        currentSet.setBand.get(i).selected = false;
      }
    }
    justSwitchedRecording = false;
  }
}

public void makeRecording() {
  if( mouseButton == RIGHT && !insertMode && !deleteMode && showPicked && !recordingMode && !justSwitchedRecording ){
    for( int i = 0 ; i < currentSet.setBand.size(); i++ ) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r )){
        recordingMode = true;
        currentSet.setBand.get(i).selected = true;
      }
    }
  }
}
public void recordingNewShow() {
  Label userInput = new Label( "RECORDING-FOR-NEW-SHOW", width/2, 0 );
  if(record.length() > 0)
      userInput.msg = record;
  userInput.fontSize = 36;
  userInput.p1Center = true;
  userInput.p2Top = true;
  userInput.c = color(0);
  userInput.show();
}

public void updateNewShow() {
  if( key == ' ' ){
    if(record.length() == 0) {
      exit();
    }
    else {
      recordingNewShow = false;
      updateNewShowRecording();
    }
  }
  else if( keyCode == BACKSPACE ){
    if( record.length() > 0 )
      record = record.substring(0,record.length()-1);
  }
  else
    record = record + str(key);
}

public void updateNewShowRecording() {
  PrintWriter beatsRecorder = createWriter(record+"/"+record+"Beats.txt");
  beatsRecorder.println("0.0");
  beatsRecorder.flush();
  beatsRecorder.close();
  PrintWriter oneSet = createWriter(record+"/"+record+"Set1.txt");
  oneSet.flush();
  oneSet.close();
  PrintWriter setList = createWriter(record+"/"+record+"Sets.txt");
  setList.println("1");
  setList.println(record+"Set1.txt");
  setList.flush();
  setList.close();
  String[] listOfShows = loadStrings("ShowList.txt");
  PrintWriter shows = createWriter("ShowList.txt");
  for(int i = 0; i < listOfShows.length+1; i++) {
    if(i < listOfShows.length)
      shows.println(listOfShows[i]);
    else
      shows.println(record);
  }
  shows.flush();
  shows.close();
  
  show = record;
  currentSet = new Set(show+"/"+show+"Set1.txt");
  currentSet.loadFromFile();
  
  record = "";
  showPicked = true;
}
class Set {
  String file;
  float allowedBeats;
  boolean beatsSet = false;
  ArrayList<Player> setBand = new ArrayList<Player>();
  Set( String _file ) {
    file = _file;
  }
  public void show() {
    for(int i = 0 ; i < setBand.size(); i++){
      setBand.get(i).show();
    }
  }
  public void loadFromFile() {
    String[] lines = loadStrings(file);
    for (int i = 0 ; i < lines.length; i++) {
      String[] currentLine = splitTokens(lines[i], " ");
      float xVal = Float.parseFloat(currentLine[0]);
      float yVal = Float.parseFloat(currentLine[1]);
      float ghostXVal = Float.parseFloat(currentLine[2]);
      float ghostYVal = Float.parseFloat(currentLine[3]);
      String name = currentLine[4];
      setBand.add( new Player( xVal*width, yVal*height ) );
      
      if(ghostXVal != -1.0f)
        setBand.get(i).setGhost( ghostXVal*width, ghostYVal*height );  
      if( !name.equals("noName") )
        setBand.get(i).setTheName( name );
    }
    //GetBeatsIfTheyExist
    String[] recordedBeats = loadStrings(show+"/"+show+"Beats.txt");
    String[] fileList = loadStrings(show+"/"+show+"Sets.txt");
    int index = -1;
    for(int i = 0; i < fileList.length; i++) {
      if((show+"/"+fileList[i]).equals(file)) {
        index = i-1;
      }
    }
    if(index != -1){
      if(!recordedBeats[index].equals("0.0")) {
        currentSet.allowedBeats = Float.parseFloat(recordedBeats[index]);
        currentSet.beatsSet = true;
        readyToRun = true;
        for(int i = 0; i < currentSet.setBand.size(); i++) {
          if(currentSet.setBand.get(i).ghostSet) {
            currentSet.setBand.get(i).setPace( currentSet.allowedBeats );
          }
        }
      }
    }
  }
  public void loadFromFile( String filePath ) {
    String[] lines = loadStrings(filePath);
    for (int i = 0 ; i < lines.length; i++) {
      String[] currentLine = splitTokens(lines[i], " ");
      float xVal = Float.parseFloat(currentLine[0]);
      float yVal = Float.parseFloat(currentLine[1]);
      float ghostXVal = Float.parseFloat(currentLine[2]);
      float ghostYVal = Float.parseFloat(currentLine[3]);
      String name = currentLine[4];
      if(ghostXVal != -1.0f)
        setBand.add( new Player(ghostXVal*width, ghostYVal*height));
      else
        setBand.add( new Player( xVal*width, yVal*height ) );
      
      if( !name.equals("noName") )
        setBand.get(i).setTheName( name );
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Band" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
