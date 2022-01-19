package Controllers;

import Views.Stage;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class HomeController extends JFrame {
    Stage stage;

    public static final double PERIOD = 20.0;

    public HomeController() throws IOException{
        setTitle("Husky Fishing");
        setSize(1050,750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stage = new Stage();
        stage.setFocusable(true);
        stage.requestFocusInWindow();


        stage.addMouseListener(new MouseAdapter() {
                                   @Override
                                   public void mouseClicked(MouseEvent e) {
                                       super.mouseClicked(e);
                                       int x = e.getX(), y = e.getY();
                                       // start game
                                       if(stage.stageState==Stage.StageState.HOME) {
                                           if(x>369 && x<681 && y>525 && y<650) {
                                               stage.start();
                                               }
                                           }
                                       // music control pause and resume
                                       if(x>970 && x<1025 && y>23 && y<69) {
                                           stage.musicControl();
                                        }
                                       // leaderboard
//                                       if(x>970 && x<1025 && y>81 && y<144) {
//
//                                       }
                                       }
                                   }

        );

        stage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        stage.hook.launch();
                        break;
                    case KeyEvent.VK_P:
                        stage.pause();
                        break;

                }
            }
        });
        add(stage);
//        stage.stageState = Views.Stage.StageState.HOME;
    }
}