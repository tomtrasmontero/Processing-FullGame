import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.StringList;


public class MainApp extends PApplet {
    private boolean mainCode = false;
    private boolean startScreen = true;
    private Screen screen = new Screen();
    private Ball[] balls = new Ball[25];
    private int ballCount = 1;
    private PFont f;
    private int timer = 1;
    private StringList scoreHistory = new StringList();
    private int screenSizeX = 800;
    private int screenSizeY = 600;
    private Button startB = new Button("Start", 325 , 300, 150,100);
    private Button historyB = new Button("Retry", 325, 480, 150,100);
    private PImage zombie;
    private PImage zombieL;
    private PImage splashBackground;
    private PImage background;
    private PImage gameOver;
    private PImage hero;

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

//        load assets
        zombie = loadImage("assets/zombie_move.png");
        zombieL = loadImage("assets/zombie_move_left.png");
        splashBackground = loadImage("assets/splashBackground.jpg");
        splashBackground.resize(screenSizeX, screenSizeY);
        background = loadImage("assets/background.png");
        background.resize(screenSizeX, screenSizeY);
        gameOver = loadImage("assets/gameOver1.jpg");
        gameOver.resize(screenSizeX, screenSizeY);

//        load cursor
        hero = loadImage("assets/hero.png");
        hero.resize(50,50);

    }


    //    loops, this is where you can put the logic
    public void draw() {

        if (!mainCode) {
//        splash screen
            background(splashBackground);
            screen.displayStart();

        } else {
            background(background);
//            set cursor to hero
            cursor(hero);
            //      create ball and move

            for (int i = 0; i < ballCount; i++) {
                balls[i].display();
                balls[i].update();
            }

            mouseCollide();

            timer += 1;

//            add additional balls every 100 mili second max 25
            if(timer % 100 == 0 && balls.length <= 25) {
                balls[ballCount] = createBall();

                ballCount++;
            }

        }
    }

//    create additional class
    private Ball createBall() {
        return new Ball(parseInt((int) random(50, 51)), parseInt((int) random(280, 281)),
                parseInt((int)(random(2,8))), parseInt((int)(random(2,8))),
                parseInt((int) random(60,100)));
    }


    public void mousePressed() {
//        Start and Show button

        if (startB.MouseIsOver() && !mainCode) {
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


    private void reset() {
        //        timer
        f = createFont("Arial",16,true);
        this.balls = new Ball[25];
        this.timer = 1;
        ballCount = 1;

    }

    private void mouseCollide() {
        for(int i = 0; i < ballCount; i++ ) {
            if(balls[i].pointInEllipse(mouseX, mouseY)) {
                balls[i].pop();
            }
        }
    }

    public class Ball {
        private int y = 100;
        private int x = 100;
        private int diffX;
        private int diffY;
        private int size;
        private PImage zombieWalk = zombie;

        Ball(int x, int y, int diffX, int diffY, int size) {
            this.x = x;
            this.y = y;
            this.diffY = diffY;
            this.diffX = diffX;
            this.size = size;
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
            imageMode(CENTER);
            image(zombieWalk, x, y, size  , size);

        }

        boolean isCollidingVertical() {
            if( getX() + (getSize()/2) > width || getX() - (getSize()/2) < 0) {
                if (zombieWalk == zombie) {
                    zombieWalk = zombieL;
                } else {
                    zombieWalk = zombie;
                }
                return true;
            }
            return false;
        }

        boolean isCollidingHorizontal() {
            return getY() + (getSize() / 2) > height || getY() - (getSize() / 2) < 0;
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
            background(0);
            screen.displayScore();
            noLoop();
        }
    }

    public class Screen {
        private String title = "Zombie Chaser";
        private int positionTopScore = 240;

        void displayStart() {
            fill(0, 102, 153);
            textSize(100);
            textAlign(CENTER, TOP);
            text(title,screenSizeX / 2, 100   );
            startB.Draw();
        }

        public void displayScore() {
            background(gameOver);
            cursor(ARROW);

            scoreHistory.append(String.valueOf(timer));

            fill(190, 80, 253);
            textSize(100);
            textAlign(CENTER, TOP);
            text("Your Score:" + timer,screenSizeX / 2, 100 );
            scoreHistory();
            historyB.Draw();
        }

        void scoreHistory() {
            fill(190, 80, 253);
            textSize(50);
            textAlign(CENTER);
            text("Previous Scores:",screenSizeX / 2, positionTopScore );
            for (int i = 0; i < scoreHistory.size(); i++ ) {
//                y position of the previous score;
                int positionY = i == 0 ? positionTopScore + 50 : positionTopScore + 50 + (50 * i);
                fill(190, 80, 253);
                textSize(40);
                textAlign(CENTER);
                text(scoreHistory.get(scoreHistory.size() - 1 - i ),screenSizeX / 2, positionY );
            }
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
            return mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h);
        }
    }

}
