void updateRecording() {
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

void recordingName() {
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

void makeRecording() {
  if( mouseButton == RIGHT && !insertMode && !deleteMode && showPicked && !recordingMode && !justSwitchedRecording ){
    for( int i = 0 ; i < currentSet.setBand.size(); i++ ) {
      if(insideCircle( mouseX, mouseY, currentSet.setBand.get(i).x, currentSet.setBand.get(i).y, currentSet.setBand.get(i).r )){
        recordingMode = true;
        currentSet.setBand.get(i).selected = true;
      }
    }
  }
}
