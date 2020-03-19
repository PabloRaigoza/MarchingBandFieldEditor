class Player {
  float x,y;
  float r = 8;
  color c = color(0);
  Label name;
  boolean labelSelected = false;
  boolean selected = false;
  boolean nameSet = false;
  
  float ghostX = -1.0;
  float ghostY;
  boolean ghostSet = false;
  color ghostColor = color(200,200,200);
  
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
  void show() {
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
  void setTheName( String title ) {
    name = new Label(title,floor(x),floor(y-(r*2)));
    name.p1Center = true;
    name.p2Center = true;
    nameSet = true;
  }
  void setGhost( float _x, float _y ) {
    ghostX = _x;
    ghostY = _y;
    ghostSet = true;
  }
  void setPace( float beats ) {
    float xIntervals = (ghostX-x)/beats;
    float yIntervals = (ghostY-y)/beats;
    for(int i = 0; i < beats+1; i++) {
      pace.add( new Position( i*xIntervals+x, i*yIntervals+y ) );
    }
    paceSet = true;
    beatsStepped = 0;
  }
  void oneBeat() {
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
  void resetPace() {
    pace.clear();
    beatsStepped = 0;
    tX = x;
    tY = y;
    paceSet = false;
  }
}
