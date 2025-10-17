package com.mycompany.computergraphics2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {
    private final int xmin = 15, xmax = 65, ymin = 10, ymax = 45;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(Constants.WIDTH, Constants.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawGrid(gc);
        drawFrame(gc);

        canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                GraphicsContext ngc = canvas.getGraphicsContext2D();
                ngc.setFill(javafx.scene.paint.Color.WHITE);
                ngc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawGrid(ngc);
                drawFrame(ngc);
                Random rand = new Random();
                int x = rand.nextInt(80);
                int y = rand.nextInt(60);
                Point add = new Point(x, y);
                drawStarBresenham(ngc, add);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, Constants.WIDTH, Constants.HEIGHT);
        primaryStage.setTitle("Компьютерная графика. Лабораторная работа №2");
        primaryStage.setScene(scene);
        primaryStage.show();
        canvas.requestFocus(); // Устанавливаем фокус на canvas для обработки нажатий клавиш
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(javafx.scene.paint.Color.LIGHTGRAY);
        for (int x = 0; x <= Constants.WIDTH; x += Constants.SIZE_OF_CELL) {
            gc.strokeLine(x, 0, x, Constants.HEIGHT);
        }
        for (int y = 0; y <= Constants.HEIGHT; y += Constants.SIZE_OF_CELL) {
            gc.strokeLine(0, y, Constants.WIDTH, y);
        }
    }

    private void pseudoPixel(GraphicsContext gc, int x, int y, javafx.scene.paint.Color color) {
        x *= Constants.SIZE_OF_CELL;
        y *= Constants.SIZE_OF_CELL;
        gc.setFill(color);
        gc.fillRect(x, y, Constants.SIZE_OF_CELL, Constants.SIZE_OF_CELL);
    }

    private void bresenhamLine(GraphicsContext gc, float x1, float y1, float x2, float y2, javafx.scene.paint.Color color) {
        float Px = Math.abs(x2 - x1), sx = x1 < x2 ? 1 : -1;
        float Py = -Math.abs(y2 - y1), sy = y1 < y2 ? 1 : -1;
        float err = Px + Py, e2;

        while (true) {
            pseudoPixel(gc, (int) x1, (int) y1, color);
            if (x1 == x2 && y1 == y2) break;
            e2 = 2 * err;
            if (e2 >= Py) {
                err += Py;
                x1 += sx;
            }
            if (e2 <= Px) {
                err += Px;
                y1 += sy;
            }
        }
    }

    private void drawFrame(GraphicsContext gc) {
        javafx.scene.paint.Color color = javafx.scene.paint.Color.BLACK;
        bresenhamLine(gc, xmin, ymin, xmax, ymin, color);
        bresenhamLine(gc, xmax, ymin, xmax, ymax, color);
        bresenhamLine(gc, xmax, ymax, xmin, ymax, color);
        bresenhamLine(gc, xmin, ymax, xmin, ymin, color);
    }

    private void drawStarBresenham(GraphicsContext gc, Point add) {
        javafx.scene.paint.Color color;
        Clipping clipping = new Clipping(xmin, xmax, ymin, ymax);
        
        if (20 + add.y >= ymin && 0 + add.y <= ymax + 1 && 0 + add.x <= xmax + 1 && 18 + add.x >= xmin - 1) {
            color = javafx.scene.paint.Color.RED;
            clipping.makeClipping(gc, new Point(3 + add.x, 20 + add.y), new Point(9 + add.x, 0 + add.y), color);
            clipping.makeClipping(gc, new Point(9 + add.x, 0 + add.y), new Point(15 + add.x, 20 + add.y), color);
            clipping.makeClipping(gc, new Point(15 + add.x, 20 + add.y), new Point(0 + add.x, 7 + add.y), color);
            clipping.makeClipping(gc, new Point(0 + add.x, 7 + add.y), new Point(18 + add.x, 7 + add.y), color);
            clipping.makeClipping(gc, new Point(18 + add.x, 7 + add.y), new Point(3 + add.x, 20 + add.y), color);
        } else {
            color = javafx.scene.paint.Color.LIGHTGRAY;
            bresenhamLine(gc, 3 + add.x, 20 + add.y, 9 + add.x, 0 + add.y, color);
            bresenhamLine(gc, 9 + add.x, 0 + add.y, 15 + add.x, 20 + add.y, color);
            bresenhamLine(gc, 15 + add.x, 20 + add.y, 0 + add.x, 7 + add.y, color);
            bresenhamLine(gc, 0 + add.x, 7 + add.y, 18 + add.x, 7 + add.y, color);
            bresenhamLine(gc, 18 + add.x, 7 + add.y, 3 + add.x, 20 + add.y, color);
        }
        
        drawFrame(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
