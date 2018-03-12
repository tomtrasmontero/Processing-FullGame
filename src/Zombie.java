import processing.core.PApplet;
import processing.core.PImage;

public class Zombie {
    PApplet parent;
    private int y = 100;
    private int x = 100;
    private int diffX;
    private int diffY;
    private int size;
    private PImage zR = MainApp.getZombie();
    private PImage zL = MainApp.getZombieL();
    private MainApp.Screen screen = MainApp.getScreen();
    private PImage zombieWalk = zR;

    Zombie(int x, int y, int diffX, int diffY, int size, PApplet p) {
        this.x = x;
        this.y = y;
        this.diffY = diffY;
        this.diffX = diffX;
        this.size = size;
        parent = p;
    }

    void move() {
        y += diffY;
        x += diffX;
    }

    //        We need to be able to change dx and dy
    void setdy(int dy) {
        diffY = dy;
    }

    void setdx(int dx) {
        diffX = dx;
    }
    //        We need to see where the ball is
    int getdy() {
        return this.diffY;
    }

    int getdx() {
        return this.diffX;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public void display() {
//        x, y, width, height
        parent.imageMode(parent.CENTER);
        parent.image(zombieWalk, x, y, size  , size);

    }

    boolean isCollidingVertical() {
        if( getX() + (getSize()/2) > parent.width || getX() - (getSize()/2) < 0) {
            if (zombieWalk == zR) {
                zombieWalk = zL;
            } else {
                zombieWalk = zR;
            }
            return true;
        }
        return false;
    }

    boolean isCollidingHorizontal() {
        return getY() + (getSize() / 2) > parent.height || getY() - (getSize() / 2) < 0;
    }

    void checkCollisions() {
        if( isCollidingHorizontal()) {
            setdy( getdy() * -1);
        }
        if( isCollidingVertical()) {
            setdx( getdx() * -1);
        }
    }

    void update() {
        move();
        checkCollisions();
    }

    boolean pointInEllipse(int x, int y) {
        double distance = Math.sqrt(Math.pow((x - getX()), 2) + Math.pow((y - getY()), 2));
        return distance < getSize() / 2;
    }

    void pop() {
//            show point and save history
        parent.background(0);
        screen.displayScore();
        parent.noLoop();
    }
}
