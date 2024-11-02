import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

// 게임 화면 클래스
class GameScreen extends JPanel {
    private static final int LINE_Y = 100;
    private static final int LINE_WIDTH = 800;
    private static final int LINE_HEIGHT = 10;
    private static final int SAMPLE_RATE = 44100;
    private static final double C2_FREQUENCY = 130.81;
    private static final double C4_FREQUENCY = 523.25;
    private double currentFrequency;
    private boolean isSliding = false;
    private SourceDataLine sdl;
    private Thread soundThread;

    private static final double[] FREQUENCIES = {
            130.81, 138.59, 146.83, 155.56, 164.81, 174.61, 185.00, 196.00, 207.65, 220.00,
            233.08, 246.94, 261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 369.99, 392.00,
            415.30, 440.00, 466.16, 493.88, 523.25
    };

    public GameScreen() {
        setPreferredSize(new Dimension(800, 800));
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        currentFrequency = C4_FREQUENCY;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isSliding = true;
                updateFrequencyFromPosition(e.getX());  // 클릭 시 주파수 업데이트
                startSoundThread();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isSliding = false;
                stopPlaying();
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isSliding) {
                    updateFrequencyFromPosition(e.getX());
                }
            }
        });
    }

    public GameScreen(int stage) {
//        dispose(); // Close the selection window

        JFrame gameFrame = new JFrame(""+stage);
        GameScreen gameScreen = new GameScreen();
        gameFrame.add(gameScreen);
        gameFrame.pack(); // Packs the frame based on preferred sizes of components
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    private void updateFrequencyFromPosition(int mouseX) {
        double range = C4_FREQUENCY - C2_FREQUENCY;
        currentFrequency = C2_FREQUENCY + (range * mouseX / LINE_WIDTH);
    }

    private void startSoundThread() {
        soundThread = new Thread(() -> {
            while (isSliding) {
                playSound(currentFrequency);
//                try {
////                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        soundThread.start();
    }

    private void playSound(double frequency) {
        int duration = 20;
        byte[] buf = new byte[1];
        Random random = new Random();

        for (int i = 0; i < SAMPLE_RATE * duration / 1000; i++) {
            double variedFrequency = frequency * (1 + 0.005 * (random.nextDouble() - 0.5));
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / variedFrequency);
            double soundWave = 0.7 * Math.sin(angle) + 0.2 * Math.sin(2 * angle)
                    + 0.1 * Math.sin(3 * angle) + 0.05 * (random.nextDouble() - 0.5);

            buf[0] = (byte) Math.max(-128, Math.min(127, soundWave * 127));
            sdl.write(buf, 0, 1);
        }
    }

    private void stopPlaying() {
        sdl.flush();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(0, LINE_Y, LINE_WIDTH, LINE_HEIGHT);

        g.setColor(Color.BLACK);
        String[] notes = {"C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2",
                "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4"};
        for (int i = 0; i < FREQUENCIES.length; i++) {
            double range = C4_FREQUENCY - C2_FREQUENCY;
            int x = (int) ((FREQUENCIES[i] - C2_FREQUENCY) / range * LINE_WIDTH);
            g.drawLine(x, LINE_Y - 20, x, LINE_Y + LINE_HEIGHT + 20);
            g.drawString(notes[i], x - 10, LINE_Y - 30);
        }
    }

}
