import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.time.LocalTime;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ugr02 {
    int width = 800;
    int height = 600;

    long window;
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;

    void spusti() {
        try {
            init();
            GL.createCapabilities();
            loop();

            GLFW.glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            GLFW.glfwTerminate();
            errorCallback.free();
        }
    }

    void init() {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Chyba pri inicializacii GLFW!!!");

        window = GLFW.glfwCreateWindow(width, height, "UGR1", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Chyba pri vytvoreni GLFW okna!!!");

        GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                    GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(0);
        GLFW.glfwShowWindow(window);

    }

    void vykresliGL() {
        glPointSize(3);
        GL11.glBegin(GL11.GL_POINTS);
//        glColor3f(0.f,0.f,1.f);
//        vysliKruh(180, 160, 60);
//        glColor3f(0.f,0.f,0.f);
//        vysliKruh(320, 160, 60);
//        glColor3f(1.f,0.f,0.f);
//        vysliKruh(460, 160, 60);
//        glColor3f(1.f,1.f,0.f);
//        vysliKruh(250, 220, 60);
//        glColor3f(0.f,1.f,0.f);
//        vysliKruh(390, 220, 60);
        glColor3f(1.0f, 1.0f, 1.0f);
        vysliKruh(400, 300, 200);
        int sx = 400, sy = 300, r = 100;
        int r2 = 200;

        for (int i = 0; i < 360; i++) {

            if (i%6==0){
                int posunka=20;
                if (i%5==0) posunka=40;
                glColor3f(1.0f, 1.0f, 1.0f);
                bresenham(sx + (int) ((r2 - posunka) * Math.cos(Math.toRadians(i))), sy + (int) ((r2 - posunka) * Math.sin(Math.toRadians(i))), sx + (int) (r2 * Math.cos(Math.toRadians(i))), sy + (int) (r2 * Math.sin(Math.toRadians(i))));
            }

//            glColor3f(0.0f, 0.0f, 0.0f);
//            bresenham(sx, sy, sx + (int) ((r2 - 20) * Math.cos(Math.toRadians(i))), sy + (int) ((r2 - 20) * Math.sin(Math.toRadians(i))));

        }
        glColor3f(0.0f, 0.0f, 1.0f);
        double uhol = LocalTime.now().getSecond() * ((double) 360 / 60) - 90;

        bresenham(sx, sy, sx + (int) ((r + 85) * Math.cos(Math.toRadians(uhol))), sy + (int) ((r + 85) * Math.sin(Math.toRadians(uhol))));
        glColor3f(0.0f, 1.0f, 0.0f);
        double uhol2 = LocalTime.now().getMinute() * ((double) 360 / 60) + 90 + 90 + 90;
        bresenham(sx, sy, sx + (int) ((r + 45) * Math.cos(Math.toRadians(uhol2))), sy + (int) ((r + 45) * Math.sin(Math.toRadians(uhol2))));
        glColor3f(1.0f, 0.0f, 0.0f);
        double uhol3 = LocalTime.now().getHour() * ((double) 360 / 12) - 90;
        bresenham(sx, sy, sx + (int) (r * Math.cos(Math.toRadians(uhol3))), sy + (int) (r * Math.sin(Math.toRadians(uhol3))));


        GL11.glEnd();
    }

    void bresenham(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            glVertex2i(x1, y1);

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    void vysliKruh(int sx, int sy, int radius) {
        int x = 0, y = radius, d = 1 - radius;
        while (x <= y) {
            GL11.glVertex2i(sx + x, sy + y);
            GL11.glVertex2i(sx - x, sy + y);
            GL11.glVertex2i(sx + x, sy - y);
            GL11.glVertex2i(sx - x, sy - y);
            GL11.glVertex2i(sx + y, sy + x);
            GL11.glVertex2i(sx - y, sy + x);
            GL11.glVertex2i(sx + y, sy - x);
            GL11.glVertex2i(sx - y, sy - x);

            if (d < 0) {
                d += (2 * x) + 3;
            } else {
                d += (2 * (x - y) + 5);
                y--;
            }
            x++;
        }
    }


    void loop() {
        GL11.glViewport(0, 0, width, height);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-0.5, width - 0.5, height - 0.5, -0.5, 0, 1);

        GL11.glShadeModel(GL11.GL_FLAT);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // rgb (0,0,0) je cierne pozadie
        GL11.glClearColor(0.f, 0.f, 0.f, 1.f); // Initialize clear color

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            vykresliGL();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new ugr02().spusti();
    }
}