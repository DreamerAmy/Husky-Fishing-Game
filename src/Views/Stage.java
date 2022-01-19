package Views;

import Models.*;
import Models.Fish1;
import Models.Fish2;
import Models.Fish3;
import Models.Fish4;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Stage extends JPanel {

    //game dimension
    public double width = 1050;
    public double height = 750;
    //timer
    private int lifetime;
    //fish list
    public List<Fish> fishList = new ArrayList<Fish>();
    public int fish1count = 5; //5 fish in fish 1
    public int fish2count = 5;
    public int fish3count = 4;
    public int fish4count = 3;
    //home, playing, pause, game_over 4 stages
    public enum StageState {HOME, PLAYING, PAUSE, GAME_OVER;}
    public StageState stageState; //define a stageState
    //score for each play
    public int score;
    // hook
    public Hook hook;
    //ranking data
    public RankingData ranking = new RankingData();
    // timer running
    private Timer timer;
    // load music
    private Views.SoundPlayer sound = new Views.SoundPlayer("res/sounds/background.wav");

    //load pics
    private Image homePic = Toolkit.getDefaultToolkit().createImage("res/images/home.png");
    private Image bgPic = Toolkit.getDefaultToolkit().createImage("res/images/background.png");
    private Image gameoverPic = Toolkit.getDefaultToolkit().createImage("res/images/gameover.png");
    private Image leadingBoardPic = Toolkit.getDefaultToolkit().createImage("res/images/rankinglist.png");

    // music control
    //Amy's code
    boolean isPaused = false;
    public Thread playSound;

    //constructor
    //initialize all variable
    public Stage() throws IOException {
        this.stageState = StageState.HOME; //initial page is home
        this.lifetime = 600; //initial timer is 60 second
        this.score = 0;   //initial score is 0
        //start to play music
        playSound = new Thread(sound);
        this.playSound.start();
        //mouse focus on the pic
        this.requestFocus();
    }

    // control music pause and resume
    //Amy's code
    public void musicControl() {
        if (!isPaused) {
            this.playSound.suspend();
            this.isPaused = true;
        }
        else {
            this.playSound.resume();
            this.isPaused = false;
        }
    }

    //pause and play (press p to control)
    public void pause() {
        if (stageState == StageState.PLAYING) {
            stageState = StageState.PAUSE;
        } else if (stageState == StageState.PAUSE) {
            stageState = StageState.PLAYING;
        }
    }

    //Game over only last 5 secs, and then come back to home page
    public void gameOver() {
        lifetime = 50;
        ranking.addScore(score);
        stageState = StageState.GAME_OVER;
    }

    //clear previous player data(score, timer)
    public void home() {
        score = 0;
        timer.cancel();
        stageState = StageState.HOME;
    }

    //start a new game
    public void start() {
        lifetime = 600;
        stageState = StageState.PLAYING;
        hook = new Hook(295, 295);
        fishList.clear();
        //positon, moving speed, each fish's value, initialize moving direction
        for (int i=0; i<fish1count; i++) {
            fishList.add(new Fish1((i*200)%width, 400+(i*25)%100, 20, 20, 1, 1, 5));
        }
        for (int i=0; i<fish2count; i++) {
            fishList.add(new Fish2((i*300)%width, 500+(i*25)%50, 20, 50, 1, 1, 10));
        }
        for (int i=0; i<fish3count; i++) {
            fishList.add(new Fish3((i*400)%width, 600+(i*25)%50, 20, 100, 1, -1, 15));
        }
        for (int i=0; i<fish4count; i++) {
            fishList.add(new Fish4((i*500)%width, 650+(i*25)%50, 20, 300, 1, -1, 20));
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100); //timer changes every 0.1 sec
    }

    //every 0.1 sec timer call refresh function
    public void refresh() throws IOException {
        if (stageState == StageState.PLAYING) {
            //game over after 60 secs
            if (lifetime <= 0) {
                gameOver();
            }
            lifetime--;
            //call model's function(yuanqiao)
            // refresh(hook change position function) /fish chang position function
            hook.refresh(this);
            for (Fish i : fishList) {
                i.runFish();
            }
            //paint new position
            repaint();
        } else if(stageState == StageState.GAME_OVER) {
            //game over, back to home
            if (lifetime <= 0) {
                home();
            }
            lifetime--;
            repaint();
        }
    }

    //draw the position
    @Override
    public void paint(Graphics g) {
        //clear previous draw
        g.clearRect(0, 0, (int)width, (int)height);
        switch (stageState) {
            case PLAYING:
            case PAUSE:
                //draw bgPic
                g.drawImage(bgPic, 0, 0, (int)width, (int)height, this);
                g.setFont(new Font("Comic Sans MS", Font.BOLD, 45)); //change later for the position
                g.setColor(Color.black);
                //draw score
                g.drawString(""+score,90,60);//change later for the position
                //draw Timer
                g.drawString(""+(int)lifetime/10, 500,60);//change later for the position
                try {
                    hook.paint(g);
                } catch (IOException e) {}
                for (Fish i : fishList) {
                    i.paint(g);
                }
                break;
            case HOME: //home background
                g.drawImage(homePic, 0, 0, (int)width, (int)height, this);
                break;
            case GAME_OVER: //game over background
                g.drawImage(bgPic, 0, 0, (int)width, (int)height, this);
                    g.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
                g.setColor(Color.black);
                g.drawString(""+score,90,60);
                g.drawString("0", 500,60);
                try {
                    hook.paint(g);
                } catch (IOException e) {}
                for (Fish i : fishList) {
                    i.paint(g);
                }
                //game over code is similar with playing except below
                g.drawImage(gameoverPic, 350, 350, 300, 200, this);
                g.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
                g.setColor(Color.red);
                g.drawImage(leadingBoardPic, 350, 120, 300, 200, this);
                List list = ranking.getTopThreeScore();
                for (int i =0; i<list.size(); i++) {
                    if (i == 0) {
                        g.drawString("1st: "+list.get(i), 435,175);
                    } else if (i == 1) {
                        g.drawString("2nd: "+list.get(i), 435,225);
                    } else if (i == 2) {
                        g.drawString("3rd: "+list.get(i), 435,275);
                    } else {
                        throw new IllegalStateException();
                    }
                }
                break;
        }
    }
}
