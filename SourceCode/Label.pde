class Label {
  String msg;
  int x,y;
  int fontSize = 12;
  color c = color(0);
  boolean p1Left, p1Right, p1Center, p2Top, p2Center, p2Bottom;
  Label( String _msg, int _x, int _y ) {
    msg = _msg;
    x = _x;
    y = _y;
  }
  void show() {
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
