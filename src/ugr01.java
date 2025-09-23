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

    void vykresliGL() {
        glBegin(GL_POINTS);
        glVertex2i(10, 10);
        glVertex2i(0, 0);
        glVertex2i(width-1, height-1);
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