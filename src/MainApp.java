import processing.core.PApplet;
import processing.core.PFont;

public class MainApp extends PApplet {
    private boolean mainCode = false;
    private boolean startScreen = true;
    private Screen screen = new Screen();
    private Ball[] balls = new Ball[20];
    private int ballCount = 1;
    private PFont f;
    private int timer = 1;
    private int screenSizeX = 800;
    private int screenSizeY = 600;
    private Button startB = new Button("Start", 325 , 300, 150,100);
    private Button historyB = new Button("Scores", 325, 300, 150,100);
    private boolean rectOver = false;

    public static void main(String[] args) {
        PApplet.main("MainApp", args);
    }
    //set size here run once
    public void settings() {
        size(screenSizeX,screenSizeY);
    }

    //    runs once in the beginning of the video
    public void setup() {
//        timer
        f = createFont("Arial",16,true);
        background(155);

    }


    //    loops, this is where you can put the logic
    public void draw() {
        background(0);
//        int score = showScore();

        if (!mainCode) {
//        splash screen
            screen.displayStart();

        } else {
            background(255);
            //      create ball and move
            for (int i = 0; i < ballCount; i++) {
                balls[i].display();
                balls[i].update();
            }

            mouseCollide();

            textFont(f);
            textAlign(RIGHT);
            text("Score: " + timer, 100, 30);
            timer += 1;

//            add additional balls every 100 mili second max 20
            if(timer % 100 == 0 && balls.length <= 20) {
                balls[ballCount] = createBall();

                ballCount++;
            }

        }

    }

    int showScore() {
        return this.timer / 100;
    }

//    create additional class
    public Ball createBall() {
        Ball ball = new Ball(parseInt((int) random(100, 101)), parseInt((int) random(100, 101)),
                parseInt((int)(random(2,8))), parseInt((int)(random(2,8))),
                parseInt((int) random(10,100)));

        return ball;
    }


    public void mousePressed() {
//        Start and Show button

        if (startB.MouseIsOver() && !mainCode) {
            // start mainCode to start the game
            print("Clicked: Start");
            mainCode = true;
            startScreen = false;
            balls[0] = createBall();


        }
        if (historyB.MouseIsOver()  && !startScreen) {
            loop();
            reset();
            balls[0] = createBall();
        }
    }


    void reset() {
        //        timer
        f = createFont("Arial",16,true);
        this.balls = new Ball[20];
        this.timer = 1;
        ballCount = 1;

    }

    void mouseCollide() {
        for(int i = 0; i < ballCount; i++ ) {
            if(balls[i].pointInEllipse(mouseX, mouseY)) {
                balls[i].pop();
            }
        }
    }


    public class Ball {
        private int y = 100;
        private int x = 100;
        private int diffX = 0;
        private int diffY = 5;
        private int size = 50;

        public Ball(int x, int y, int diffX, int diffY, int size) {
            this.x = x;
            this.y = y;
            this.diffY = diffY;
            this.diffX = diffX;
            this.size = size;
        }

        public void move() {
            y += diffY;
            x += diffX;
        }

        //        We need to be able to change dx and dy
        public void setdy(int dy) {
            diffY = dy;
        }

        public void setdx(int dx) {
            diffX = dx;
        }
        //        We need to see where the ball is
        public int getdy() {
            return this.diffY;
        }

        public int getdx() {
            return this.diffX;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }

        public void display() {
//        x, y, width, height
            ellipse(x,y,size ,size);
        }

        public boolean isCollidingVertical() {
            if( getX() + (getSize()/2) > width || getX() - (getSize()/2) < 0) {
                return true;
            }
            return false;
        }

        public boolean isCollidingHorizontal() {
            if( getY() + (getSize()/2) > height || getY() - (getSize()/2) < 0) {
                return true;
            }
            return false;
        }

        public void checkCollisions() {
            if( isCollidingHorizontal()) {
                setdy( getdy() * -1);
            }
            if( isCollidingVertical()) {
                setdx( getdx() * -1);
            }
        }

        public void update() {
            move();
            checkCollisions();
        }

        public boolean pointInEllipse(int x, int y) {
            double distance = Math.sqrt(Math.pow((x - getX()), 2) + Math.pow((y - getY()), 2));
            if (distance < getSize()/2) {
                return true;
            }
            return false;
        }

        public void pop() {
//            show point and history
            background(0);
            screen.displayScore();
            noLoop();
        }

    }

    public class Screen {
        String title = "Asteroid II";
        String score = "Latest Score";

        public void displayStart() {
            fill(0, 102, 153);
            textSize(100);
            textAlign(CENTER, TOP);
            text(title,screenSizeX / 2, 100   );
            startB.Draw();
        }

        public void displayScore() {
            fill(0, 102, 153);
            textSize(100);
            textAlign(CENTER, TOP);
            text(score,screenSizeX / 2, 100   );
            historyB.Draw();
        }


    }

    class Button {
        String label;
        float x;    // top left corner x position
        float y;    // top left corner y position
        float w;    // width of button
        float h;    // height of button

        Button(String labelB, float xpos, float ypos, float widthB, float heightB) {
            label = labelB;
            x = xpos;
            y = ypos;
            w = widthB;
            h = heightB;
        }

        void Draw() {
            fill(218);
            stroke(141);
            rect(x, y, w, h, 10);
            textAlign(CENTER, CENTER);
            fill(0);
            textSize(25);
            text(label, x + (w / 2), y + (h / 2));
        }

        boolean MouseIsOver() {
            if (mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)) {
                return true;
            }
            return false;
        }
    }
}
