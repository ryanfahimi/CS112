import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A LavaLamp application that displays a JPanel with a continually changing
 * background color.
 * The color change can be toggled on and off by clicking a button.
 */
public class LavaLamp extends JPanel {

    // Constants for color change range, panel dimensions, timer delay, initial
    // color values, button text, and frame title
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

    /**
     * The main method that creates and displays the LavaLamp JFrame.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(FRAME_TITLE);
            configureFrame(frame);
        });
    }

    /**
     * Configures the given JFrame with the LavaLamp JPanel.
     *
     * @param frame the JFrame to configure
     */
    private static void configureFrame(JFrame frame) {
        LavaLamp lavaLamp = new LavaLamp();
        frame.add(lavaLamp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(IS_VISIBLE);
    }

    /**
     * Constructs a new LavaLamp object.
     */
    public LavaLamp() {
        initComponents();
    }

    /**
     * Initializes the LavaLamp components.
     */
    private void initComponents() {
        configurePanel();
        initTimer();
        initButton();
    }

    /**
     * Configures the LavaLamp JPanel.
     */
    private void configurePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        initBackgroundColor();
    }

    /**
     * Initializes the background color of the LavaLamp JPanel.
     */
    private void initBackgroundColor() {
        backgroundColor = INITIAL_COLOR;
        setBackground(backgroundColor);
        isColorChanging = true;
    }

    /**
     * Initializes the timer responsible for changing the background color.
     */
    private void initTimer() {
        timer = new Timer(TIMER_DELAY, this::changeBackgroundColor);
        timer.start();
    }

    /**
     * Changes the background color of the LavaLamp JPanel.
     *
     * @param e the ActionEvent that triggered the color change
     */
    private void changeBackgroundColor(ActionEvent e) {
        if (isColorChanging) {
            backgroundColor = getRandomColor(backgroundColor);
            setBackground(backgroundColor);
        }
    }

    /**
     * Generates a random color based on the current color.
     *
     * @param currentColor the current background color
     * @return a new Color object representing the randomly generated color
     */
    private Color getRandomColor(Color currentColor) {
        int red = clamp(currentColor.getRed()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));
        int green = clamp(currentColor.getGreen()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));
        int blue = clamp(currentColor.getBlue()
                + ThreadLocalRandom.current().nextInt(-COLOR_CHANGE_RANGE, COLOR_CHANGE_RANGE + 1));

        return new Color(red, green, blue);
    }

    /**
     * Clamps the given value between MIN_COLOR_VALUE and MAX_COLOR_VALUE.
     *
     * @param value the value to be clamped
     * @return the clamped value
     */
    private static int clamp(int value) {
        return Math.min(MAX_COLOR_VALUE, Math.max(MIN_COLOR_VALUE, value));
    }

    /**
     * Initializes the button for toggling color changing.
     */
    private void initButton() {
        button = new JButton(BUTTON_TEXT);
        button.addActionListener(e -> toggleColorChanging());
        add(button);
    }

    /**
     * Toggles the color changing state of the LavaLamp JPanel.
     * If the color changing is stopped, the current color values are printed to the
     * console.
     */
    private void toggleColorChanging() {
        isColorChanging = !isColorChanging;
        if (isColorChanging) {
            timer.start();
        } else {
            timer.stop();
            System.out.printf("%d,%d,%d%n",
                    backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
        }
    }

}