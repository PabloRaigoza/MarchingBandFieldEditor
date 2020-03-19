void toggleRecordBeatsMode() {
  if(recordBeats)
    recordBeats = false;
  else{
    recordBeats = true;
  }
}

void updateRecordBeats() {
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

void recordingBeats() {
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

void updateBeats() {
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

void moveAllPlayers() {
  for(int i = 0; i < currentSet.setBand.size(); i++) {
    if(currentSet.setBand.get(i).paceSet){
      currentSet.setBand.get(i).oneBeat();
      if(currentSet.setBand.get(i).beatsStepped == 0){
        runShow = false;
      }
    }
  }
}

void playShow() {
  if(runShow){
    if(totalBeats < 30)
      totalBeats++;
    else {
      totalBeats = 0;
      moveAllPlayers();
    }
  }
}

boolean movePlayers() {
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
void playAllShows() {
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
