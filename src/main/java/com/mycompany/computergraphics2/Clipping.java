/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.computergraphics2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author maryf
 */
public class Clipping {
    // Константы для кодов
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;

    private int xmin, xmax, ymin, ymax;

    public Clipping(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    // Метод для проверки положения точки относительно границ
    int checkPoint(Point p) {
        int code = 0;
        if (p.x < xmin) code = LEFT;
        else if (p.x > xmax) code = RIGHT;
        if (p.y < ymin) code = BOTTOM;
        else if (p.y > ymax) code = TOP;
        return code;
    }

    int makeClipping(GraphicsContext gc, Point a, Point b, Color color) {
	int code_a, code_b, code; /* код концов отрезка */
	Point c; /* одна из точек */

	code_a = checkPoint(a);
	code_b = checkPoint(b);
	
	/* пока одна из точек отрезка вне прямоугольника */
	while (code_a != 0 || code_b != 0) {
		/* если обе точки с одной стороны прямоугольника, то отрезок не пересекает прямоугольник */
		if (code_a != 0 && code_b != 0)
			return -1;

		/* выбираем точку с ненулевым кодом */
		if (code_a > 0) {
			code = code_a;
			c = a;
		} else {
			code = code_b;
			c = b;
		}

            
            switch (code) {
                /* если c левее прямоугольника, то передвигаем c на прямую x = xmin
                если c правее прямоугольника, то передвигаем c на прямую x = xmax */
                case LEFT:
                    c.y += (a.y - b.y) * (xmin - c.x) / (a.x - b.x);
                    c.x = xmin;
                    break;
                case RIGHT:
                    c.y += (a.y - b.y) * (xmax - c.x) / (a.x - b.x);
                    c.x = xmax;
                    break;
                /* если c ниже прямоугольника, то передвигаем c на прямую y = ymin
                если c выше прямоугольника, то передвигаем c на прямую y = ymax */
                case BOTTOM:
                    c.x += (a.x - b.x) * (ymin - c.y) / (a.y - b.y);
                    c.y = ymin;
                    break;
                case TOP:
                    c.x += (a.x - b.x) * (ymax - c.y) / (a.y - b.y);
                    c.y = ymax;
                    break;
                default:
                    break;
            }

            /* обновляем код */
            if (code == code_a) {
                    /* a = c */
                    code_a = checkPoint(a);
            } else {
                    /* b = c */
                    code_b = checkPoint(b);
            }
	}

	/* оба кода равны 0, следовательно обе точки в прямоугольнике */
        // Рисуем линию с помощью метода Брезенхема
        bresenhamLine(gc, a.x, a.y, b.x, b.y, color);
	return 0;
    }
    
    // Функция для закрашивания псевдопикселя
    private void pseudoPixel(GraphicsContext gc, int x, int y, javafx.scene.paint.Color color) {
        x *= Constants.SIZE_OF_CELL;
        y *= Constants.SIZE_OF_CELL;
        gc.setFill(color);
        gc.fillRect(x, y, Constants.SIZE_OF_CELL, Constants.SIZE_OF_CELL);
    }
    
    // Функция для рисования линии с использованием алгоритма Брезенхама
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
}
