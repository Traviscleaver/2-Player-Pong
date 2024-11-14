package com.ponggame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class Pong extends Application {
    private Rectangle p1Base;
    private Rectangle p2Base;
    private Text scoreboard;
    private Text instructions;
    private int score = 0;
    private Rectangle floor;
    private Rectangle roof;
    private int xVelocity = 3;
    private int yVelocity = 5;
    private Circle ball;
    private moveAnimation animation;
    private boolean p1GoLeft = false;
    private boolean p1GoRight = false;
    private boolean p2GoLeft = false;
    private boolean p2GoRight = false;
    private boolean startGame = false;
    private int timer = 120;

    public class keyPressed implements EventHandler<KeyEvent> {

        @Override
        public void handle (KeyEvent e) {
            if (e.getCode() == KeyCode.RIGHT) {
                p1GoRight = true;
            }
            else if (e.getCode() == KeyCode.LEFT) {
                p1GoLeft = true;
            }
            else if (e.getCode() == KeyCode.A) {
                p2GoLeft = true;
            }
            else if (e.getCode() == KeyCode.D) {
                p2GoRight = true;
            }
        }
    }



    public class stopMovingHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent e) {
            if (e.getCode() == KeyCode.RIGHT) {
                p1GoRight = false;
            }
            else if (e.getCode() == KeyCode.LEFT) {
                p1GoLeft = false;
            }
            else if (e.getCode() == KeyCode.A) {
                p2GoLeft = false;
            }
            else if (e.getCode() == KeyCode.D) {
                p2GoRight = false;
            }
        }
    }

    private boolean checkBaseCollision() {

        boolean collision = ball.getBoundsInParent().intersects(p1Base.getBoundsInParent());

        if (collision) {
            Random r = new Random();
            ball.setFill(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
        return collision;
    }

    private boolean checkBase2Collision() {
        boolean collision = ball.getBoundsInParent().intersects(p2Base.getBoundsInParent());
        if (collision) {
            Random r = new Random();
            ball.setFill(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
        return collision;
    }

    private boolean checkFloorCollision() {
        return ball.getBoundsInParent().intersects(floor.getBoundsInParent());
    }
    private boolean checkRoofCollosion() {
        return ball.getBoundsInParent().intersects(roof.getBoundsInParent());
    }

    private void resetGame() {
        animation.stop();
        startGame = false;
        animation.start();
        score = 0;
        scoreboard.setX(205);
        ball.setCenterX(350);
        ball.setCenterY(500);
        instructions.setVisible(true);
    }
    private class moveAnimation extends AnimationTimer {
        @Override
     public void handle (long now) {
            if (p1GoLeft || p1GoRight || p2GoLeft || p2GoRight) {
                scoreboard.setX(339);
                scoreboard.setText("" + score);
                startGame = true;
                instructions.setVisible(false);
            }

            if (startGame) {
                double p1Location = p1Base.getX();
                double p2Location = p2Base.getX();

                if (checkFloorCollision()) {
                    scoreboard.setText("PLAYER 1 WINS");
                    resetGame();
                }
                else if (checkRoofCollosion()) {
                    scoreboard.setText("PLAYER 2 WINS");
                    resetGame();
                }

                if (p1GoLeft && p1Location > 5) {
                    p1Location -= 5;
                    p1Base.setX(p1Location);
                }
                if (p1GoRight && p1Location < 545) {
                    p1Location += 5;
                    p1Base.setX(p1Location);
                }
                if (p2GoLeft && p2Location > 5) {
                    p2Location -= 5;
                    p2Base.setX(p2Location);
                }
                if (p2GoRight && p2Location < 545) {
                    p2Location += 5;
                    p2Base.setX(p2Location);
                }

                double x2 = ball.getCenterX();
                double y2 = ball.getCenterY();

                if (x2 + xVelocity > 690 || x2 + xVelocity < 0) {
                    xVelocity *= -1;
                }


                if (checkBaseCollision()) {
                    if (y2 + yVelocity > 930 || y2 + yVelocity < 0) {
                        yVelocity *= -1;
                    }
                }
                else {
                    if (y2 + yVelocity > 990 || y2 + yVelocity < 0) {
                        yVelocity *= -1;
                    }
                }

                if (checkBase2Collision()) {
                    if (y2 + yVelocity > 930 || y2 + yVelocity < 70) {
                        yVelocity *= -1;
                    }
                }
                else {
                    if (y2 + yVelocity > 990 || y2 + yVelocity < 0) {
                        yVelocity *= -1;
                    }
                }

                if (timer > 0)
                    timer --;

                if (timer == 0 && checkBaseCollision()) {
                    score++;
                    scoreboard.setText("" + score);
                    timer = 120;
                }
                if (timer == 0 && checkBase2Collision()) {
                    score++;
                    scoreboard.setText("" + score);
                    timer = 120;
                }

                x2 += xVelocity;
                y2 += yVelocity;

                ball.setCenterX(x2);
                ball.setCenterY(y2);
            }
        }
    }

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        scoreboard = new Text(339,400,"" + score);
        instructions = new Text(100,600, """
                Player 1
                    A & D to move

                Player 2
                   LEFT & RIGHT to move""");
        instructions.setStyle("-fx-font: 30 arial;");
        scoreboard.setFill(Color.BLACK);
        scoreboard.setStyle("-fx-font: 40 arial;");
        ball = new Circle(350, 500, 10);
        p1Base = new Rectangle(275,930, 150,10);
        p2Base = new Rectangle(275,70, 150,10);
        roof = new Rectangle(0,0, 700,10);
        roof.setFill(Color.RED);
        floor = new Rectangle(0,990, 700,10);
        floor.setFill(Color.RED);

        root.getChildren().addAll(p1Base, roof, floor, ball, scoreboard, p2Base, instructions);

        animation = new moveAnimation();
        animation.start();

        Scene scene = new Scene(root, 700, 1000);
        scene.setOnKeyPressed(new keyPressed());
        scene.setOnKeyReleased(new stopMovingHandler());
        stage.setResizable(false);
        stage.setTitle("Pong!");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}