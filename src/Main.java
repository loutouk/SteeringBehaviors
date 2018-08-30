import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    // create the GUI explicitly on the Swing event thread
    private static void createAndShowGui() {
        Display mainPanel = new Display(false, false, 300, 0.2, 200);
        //Display mainPanel = new Display(true, false, 1000, 0.2, 400, 100, SLOWING_PACE.SLOW);
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);

        // 45 fps (assuming computer can keep up)
        final int DELAY = 1000 / 45;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            boolean wait = false;

            @Override
            public void run() {


                if (wait) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                wait = false;

                mainPanel.update(DELAY / 1000.0);
                frame.repaint();

            }
        }, 0, DELAY);
    }
}
