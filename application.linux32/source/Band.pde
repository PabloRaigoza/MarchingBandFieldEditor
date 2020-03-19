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






void setup() {
  fullScreen();
}

void draw() {
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

void mousePressed() {
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

void mouseDragged() {
  if(mouseButton == LEFT){
    dragPlayer();
    dragLabel();
  }
}

void mouseReleased() {
  if(draggingGhost)
    createGhost();
  if(draggingLabel)
    draggingLabel = false;
}

void keyPressed() {
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
