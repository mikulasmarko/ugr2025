import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class ugr01 {
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

            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }

    void init() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Chyba pri inicializacii GLFW!!!");

        window = glfwCreateWindow(width, height, "UGR1", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Chyba pri vytvoreni GLFW okna!!!");

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        glfwShowWindow(window);

    }


    void bresenham(int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            int pomocx = x1;
            x1 = x2;
            x2 = pomocx;
        }
        if (y2 < y1) {
            int pomocy = y1;
            y1 = y2;
            y2 = pomocy;
        }
        int krok = 1;
        if (y1 < y2) krok = -1;
        int dx = x2 - x1;
        int dy = Math.abs(y2 - y1);
        int y = y1;
        int k1 = 2 * dy;
        int k2 = 2 * dy - 2 * dx;
        int d = 2 * dy - dx;
        for (int x = x1; x <= x2; x++) {
            glVertex2i(x, y);
            if (d < 0)
                d = d + k1;
            else {
                d = d + k2;
                y = y + krok;
            }
        }
    }

    void vykresliGL() {
        glPointSize(5);
        glBegin(GL_POINTS);
//        glVertex2i(10, 10);
//        glVertex2i(0, 0);
//        glVertex2i(width - 1, height - 1);
//        bresenham(400, 300, 600, 350);
        for (int i = 0; i < height; i += 20) {
            bresenham(width / 2, height / 2, width, i);
            bresenham(width / 2, height / 2, 0, height - i);
        }
        glEnd();
    }

    void loop() {
        glViewport(0, 0, width, height);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-0.5, width - 0.5, height - 0.5, -0.5, 0, 1);

        glShadeModel(GL_FLAT);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // rgb (0,0,0) je cierne pozadie
        glClearColor(0.f, 0.f, 0.f, 1.f); // Initialize clear color

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            vykresliGL();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new ugr01().spusti();
    }
}