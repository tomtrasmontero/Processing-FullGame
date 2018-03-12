import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.StringList;


public class MainApp extends PApplet {
    private static Screen screen;
    private boolean mainCode = false;
    private boolean startScreen = true;
    private Zombie[] zombieGroup = new Zombie[25];
    private int zombieCount = 1;
    private PFont f;
    private int timer = 1;
    private StringList scoreHistory = new StringList();
    private int screenSizeX = 800;
    private int screenSizeY = 600;
    private Button startB = new Button("Start", 325 , 300, 150,100, this);
    private Button historyB = new Button("Retry", 325, 480, 150,100, this);
    private static PImage zombie;
    private static PImage zombieL;
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

        screen = new Screen();

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

            for (int i = 0; i < zombieCount; i++) {
                zombieGroup[i].display();
                zombieGroup[i].update();
            }

            mouseCollide();

            timer += 1;

//            add additional zombieGroup every 100 mili second max 25
            if(timer % 100 == 0 && zombieGroup.length <= 25) {
                zombieGroup[zombieCount] = createZombie();

                zombieCount++;
            }

        }
    }

//    create additional class
    private Zombie createZombie() {
        return new Zombie(parseInt((int) random(50, 51)), parseInt((int) random(280, 281)),
                parseInt((int)(random(2,8))), parseInt((int)(random(2,8))),
                parseInt((int) random(60,100)), this);
    }


    public void mousePressed() {
//        Start and Show button

        if (startB.MouseIsOver() && !mainCode) {
            mainCode = true;
            startScreen = false;
            zombieGroup[0] = createZombie();

        }
        if (historyB.MouseIsOver()  && !startScreen) {
            loop();
            reset();
            zombieGroup[0] = createZombie();
        }
    }


    private void reset() {
        //        timer
        f = createFont("Arial",16,true);
        this.zombieGroup = new Zombie[25];
        this.timer = 1;
        zombieCount = 1;
    }

    private void mouseCollide() {
        for(int i = 0; i < zombieCount; i++ ) {
            if(zombieGroup[i].pointInEllipse(mouseX, mouseY)) {
                zombieGroup[i].pop();
            }
        }
    }

    static PImage getZombie() {
        return zombie;
    }

    static PImage getZombieL() {
        return zombieL;
    }

    static Screen getScreen() {
        return screen;
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
}
