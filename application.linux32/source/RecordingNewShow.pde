void recordingNewShow() {
  Label userInput = new Label( "RECORDING-FOR-NEW-SHOW", width/2, 0 );
  if(record.length() > 0)
      userInput.msg = record;
  userInput.fontSize = 36;
  userInput.p1Center = true;
  userInput.p2Top = true;
  userInput.c = color(0);
  userInput.show();
}

void updateNewShow() {
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

void updateNewShowRecording() {
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
