import processing.core.PApplet;

public class Button {
    PApplet parent;
    String label;
    float x;    // top left corner x position
    float y;    // top left corner y position
    float w;    // width of button
    float h;    // height of button


    Button (String labelB, float xpos, float ypos, float widthB, float heightB, PApplet p) {
        label = labelB;
        x = xpos;
        y = ypos;
        w = widthB;
        h = heightB;
        parent = p;
    }

    void Draw() {
        parent.fill(218);
        parent.stroke(141);
        parent.rect(x, y, w, h, 10);
        parent.textAlign(parent.CENTER, parent.CENTER);
        parent.fill(0);
        parent.textSize(25);
        parent.text(label, x + (w / 2), y + (h / 2));
    }

    boolean MouseIsOver() {
        return parent.mouseX > x && parent.mouseX < (x + w) && parent.mouseY > y && parent.mouseY < (y + h);
    }
}
