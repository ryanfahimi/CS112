import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ThreadLocalRandom;

public class LavaLamp extends JPanel {

    private static final int COLOR_CHANGE_RANGE = 4;
    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_HEIGHT = 300;
    private static final int TIMER_DELAY = 100;
    private static final int INITIAL_RED = 128;
    private static final int INITIAL_GREEN = 128;
    private static final int INITIAL_BLUE = 128;
    private static final int MAX_COLOR_VALUE = 255;
    private static final int MIN_COLOR_VALUE = 0;
    private static final String BUTTON_TEXT = "Toggle Color Changing";
    private static final String FRAME_TITLE = "Lava Lamp";
    private static final boolean IS_VISIBLE = true;
    private static final Color INITIAL_COLOR = new Color(INITIAL_RED, INITIAL_GREEN, INITIAL_BLUE);

    private JButton button;
    private Timer timer;
    private boolean isColorChanging;
    private Color backgroundColor;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(FRAME_TITLE);
            configureFrame(frame);
        });
    }

    private static void configureFrame(JFrame frame) {
        LavaLamp lavaLamp = new LavaLamp();
        frame.add(lavaLamp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(IS_VISIBLE);
    }

    public LavaLamp() {
        initComponents();
    }

    private void initComponents() {
        initPanel();
        initTimer();
        initButton();
    }

    private void initPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        initBackgroundColor();
    }

    private void initBackgroundColor() {
        backgroundColor = INITIAL_COLOR;
        setBackground(backgroundColor);
        isColorChanging = true;
    }

    private void initTimer() {
        timer = new Timer(TIMER_DELAY, this::changeBackgroundColor);
        timer.start();
    }

    private void changeBackgroundColor(ActionEvent e) {
        if (isColorChanging) {
            backgroundColor = getRandomColor(backgroundColor);
            setBackground(backgroundColor);
        }
    }

    private Color getRandomColor(Color currentColor) {
        int red = clamp(currentColor.getRed()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));
        int green = clamp(currentColor.getGreen()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));
        int blue = clamp(currentColor.getBlue()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));

        return new Color(red, green, blue);
    }

    private static int clamp(int value) {
        return Math.min(MAX_COLOR_VALUE, Math.max(MIN_COLOR_VALUE, value));
    }

    private void initButton() {
        button = new JButton(BUTTON_TEXT);
        button.addActionListener(e -> toggleColorChanging());
        add(button);
    }

    private void toggleColorChanging() {
        isColorChanging = !isColorChanging;
        if (isColorChanging) {
            timer.start();
        } else {
            timer.stop();
            System.out.printf("%d,%d,%d\n",
                    backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
        }
    }

}
