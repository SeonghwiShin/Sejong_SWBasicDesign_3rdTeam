import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ClickMelody extends JPanel {
    // 시각적 선을 그리기 위한 상수들
    private static final int LINE_Y = 595;         // 선의 Y 좌표
    private static final int LINE_WIDTH = 1000;     // 선의 너비
    private static final int LINE_HEIGHT = 10;     // 선의 높이
    private static final int LINE_X = (1280 - LINE_WIDTH) / 2; // 선의 X 좌표 (중앙으로 조정)

    // 오디오 설정을 위한 상수들
    private static final int SAMPLE_RATE = 44100;  // 오디오 샘플링 레이트 (Hz)
    private static final double C2_FREQ = 130.81;  // C2 음의 주파수 (Hz)
    private static final double C4_FREQ = 523.25;  // C4 음의 주파수 (Hz)

    // 다양한 음을 생성하기 위한 주파수 배열
    private static final double[] FREQUENCIES = {
            130.81, 138.59, 146.83, 155.56, 164.81, 174.61, 185.00, 196.00,
            207.65, 220.00, 233.08, 246.94, 261.63, 277.18, 293.66, 311.13,
            329.63, 349.23, 369.99, 392.00, 415.30, 440.00, 466.16, 493.88, 523.25
    };

    // FREQUENCIES 배열에 해당하는 음표 레이블
    private static final String[] NOTES = {
            "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2",
            "A#2", "B2", "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3",
            "G3", "G#3", "A3", "A#3", "B3", "C4"
    };

    // 오디오 재생을 위한 구성 요소들
    private SourceDataLine audioLine;    // 오디오 데이터를 출력하는 라인
    private Thread soundThread;          // 소리를 재생하는 별도의 스레드
    private double currentFreq = C4_FREQ; // 현재 재생 중인 주파수
    private boolean isPlaying = false;    // 소리가 재생 중인지 여부

    // 소리의 변화를 위해 사용하는 랜덤 생성기
    private final Random random = new Random();

    public ClickMelody() {
        // 오디오 시스템을 초기화합니다.
        initAudio();

        // 사용자 입력을 처리하기 위한 마우스 리스너를 추가합니다.
        addMouseListeners();
    }

    private void initAudio() {
        // 오디오 포맷 정의: 샘플링 레이트, 비트 깊이, 채널 수, 부호화 여부, 엔디안
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
        try {
            // 오디오 출력 라인을 가져옵니다.
            audioLine = AudioSystem.getSourceDataLine(format);
            audioLine.open(format); // 라인을 열고 포맷을 설정합니다.
            audioLine.start();      // 오디오 라인을 시작하여 데이터를 받을 준비를 합니다.
        } catch (LineUnavailableException e) {
            // 오디오 라인을 열 수 없는 경우 예외를 출력합니다.
            e.printStackTrace();
        }
    }

    private void addMouseListeners() {
        // 마우스 클릭 및 해제 이벤트를 처리하기 위한 리스너 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 마우스 버튼이 눌리면 소리를 재생 시작
                isPlaying = true;
                updateFrequency(e.getX()); // 클릭 위치에 따라 주파수를 업데이트
                startSound();               // 소리 재생을 시작
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // 마우스 버튼이 해제되면 소리를 중지
                isPlaying = false;
                stopSound(); // 소리 재생을 중지
            }
        });

        // 마우스 드래그 이벤트를 처리하여 소리 재생 중 주파수를 지속적으로 업데이트
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPlaying) {
                    updateFrequency(e.getX()); // 드래그 위치에 따라 주파수를 업데이트
                }
            }
        });
    }

    private void updateFrequency(int mouseX) {
        // 주파수 범위를 계산 (C4_FREQ - C2_FREQ)
        double range = C4_FREQ - C2_FREQ;
        // 마우스 X 좌표를 선의 너비로 나누어 비율을 구하고, 이를 주파수 범위에 적용
        currentFreq = C2_FREQ + (range * (mouseX - LINE_X) / LINE_WIDTH);
    }

    private void startSound() {
        // 소리 재생을 위한 새로운 스레드 생성
        soundThread = new Thread(() -> {
            while (isPlaying) {
                playTone(currentFreq); // 현재 주파수로 톤을 재생
                try {
                    // 짧은 시간 동안 대기하여 CPU 사용률을 낮춤
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // 스레드가 인터럽트되면 현재 스레드를 인터럽트 상태로 설정
                    Thread.currentThread().interrupt();
                }
            }
        });
        soundThread.start(); // 스레드 시작
    }

    private void playTone(double frequency) {
        int durationMs = 20;        // 톤의 지속 시간 (밀리초)
        byte[] buffer = new byte[1]; // 오디오 데이터 버퍼

        // 주어진 지속 시간 동안 오디오 데이터를 생성
        for (int i = 0; i < SAMPLE_RATE * durationMs / 1000; i++) {
            // 주파수를 약간 변동시켜 자연스러운 소리를 생성
            double variedFreq = frequency * (1 + 0.005 * (random.nextDouble() - 0.5));
            // 각도를 계산하여 사인파를 생성
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / variedFreq);
            // 여러 주파수의 사인파를 합성하여 복잡한 파형 생성
            double wave = 0.7 * Math.sin(angle)
                    + 0.2 * Math.sin(2 * angle)
                    + 0.1 * Math.sin(3 * angle);

            // 파형을 8비트 PCM 값으로 변환 (-128 ~ 127)
            buffer[0] = (byte) Math.max(-128, Math.min(127, wave * 127));
            // 오디오 라인에 데이터를 씁니다.
            audioLine.write(buffer, 0, 1);
        }
    }

    private void stopSound() {
        audioLine.flush(); // 오디오 라인의 버퍼를 비웁니다.
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 메인 선을 빨간색으로 그립니다.
        g.setColor(Color.RED);
        g.fillRect(LINE_X, LINE_Y, LINE_WIDTH, LINE_HEIGHT); // X 좌표를 중앙으로 설정

        // 주파수 마커 및 레이블을 검은색으로 그립니다.
        g.setColor(Color.BLACK);
        double range = C4_FREQ - C2_FREQ; // 주파수 범위 계산

        // 모든 주파수에 대해 마커와 레이블을 그립니다.
        for (int i = 0; i < FREQUENCIES.length; i++) {
            // 주파수에 해당하는 X 좌표 계산
            int x = LINE_X + (int) ((FREQUENCIES[i] - C2_FREQ) / range * LINE_WIDTH);
            // 주파수 마커 선 그리기
            g.drawLine(x, LINE_Y - 20, x, LINE_Y + LINE_HEIGHT + 20);
            // 주파수 레이블 그리기 (음표 이름)
            g.drawString(NOTES[i], x - 10, LINE_Y - 30);
        }
    }
}
