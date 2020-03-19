class Button {
  String msg;
  int x,y,w,h;
  int fontSize = 12;
  color c = color(255,255,255);
  color textColor = color(0);
  Button( String _msg, int _x, int _y, int _w, int _h ) {
    msg = _msg;
    x = _x;
    y = _y;
    w = _w;
    h = _h;
  }
  
  void show() {
    fill(c);
    rect(x,y,w,h);
    fill(textColor);
    textSize(fontSize);
    textAlign(CENTER, CENTER);
    text(msg, x+w/2, y+h/2);
  }
  
  boolean clicked() {
    return mouseX < x+w && mouseX > x && mouseY < y+h && mouseY > y && mousePressed; 
  }
  boolean hovering() {
    return mouseX < x+w && mouseX > x && mouseY < y+h && mouseY > y;
  }
}
