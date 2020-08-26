package com.rusloker.pong.engine;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Vector2DTest {

    @Test
    public void testAngleBetween() {
        System.out.println(Vector2D.angleBetween(new Vector2D(1, 0), new Vector2D(0.5f, 0.5f)));
        System.out.println(Vector2D.angleBetween(new Vector2D(1, 0), new Vector2D(-0.5f, 0.5f)));
        System.out.println(Vector2D.angleBetween(new Vector2D(-1, 0), new Vector2D(0.5f, 0.5f)));
        System.out.println(Vector2D.angleBetween(new Vector2D(-1, 0), new Vector2D(-0.5f, 0.5f)));
    }
}