class Set {
  String file;
  float allowedBeats;
  boolean beatsSet = false;
  ArrayList<Player> setBand = new ArrayList<Player>();
  Set( String _file ) {
    file = _file;
  }
  void show() {
    for(int i = 0 ; i < setBand.size(); i++){
      setBand.get(i).show();
    }
  }
  void loadFromFile() {
    String[] lines = loadStrings(file);
    for (int i = 0 ; i < lines.length; i++) {
      String[] currentLine = splitTokens(lines[i], " ");
      float xVal = Float.parseFloat(currentLine[0]);
      float yVal = Float.parseFloat(currentLine[1]);
      float ghostXVal = Float.parseFloat(currentLine[2]);
      float ghostYVal = Float.parseFloat(currentLine[3]);
      String name = currentLine[4];
      setBand.add( new Player( xVal*width, yVal*height ) );
      
      if(ghostXVal != -1.0)
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
  void loadFromFile( String filePath ) {
    String[] lines = loadStrings(filePath);
    for (int i = 0 ; i < lines.length; i++) {
      String[] currentLine = splitTokens(lines[i], " ");
      float xVal = Float.parseFloat(currentLine[0]);
      float yVal = Float.parseFloat(currentLine[1]);
      float ghostXVal = Float.parseFloat(currentLine[2]);
      float ghostYVal = Float.parseFloat(currentLine[3]);
      String name = currentLine[4];
      if(ghostXVal != -1.0)
        setBand.add( new Player(ghostXVal*width, ghostYVal*height));
      else
        setBand.add( new Player( xVal*width, yVal*height ) );
      
      if( !name.equals("noName") )
        setBand.get(i).setTheName( name );
    }
  }
}
