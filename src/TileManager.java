import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileManager {
    private List<Tile> tiles;
    private int screenWidth;
    private int screenHeight;
    private Timer timer;  // 타일 위치 업데이트 타이머
    private Timer tileCreationTimer; // 타일 생성 타이머
    private Runnable repaintCallback;
    private int currentTileIndex = 0; // 현재 생성할 타일의 인덱스
    private final int speed = 8; // 타일의 속도 설정
    private final int initialYPosition = -50; // 타일의 초기 Y 위치 (화면 위에서 시작)
    private String countdownText = ""; // 화면에 출력할 카운트다운 텍스트


    /*
    C2	140.0   C#2	159.8   D2	180.8   D#2	203.1   E2	226.6   F2	251.6   F#2	278.1
    G2	306.1   G#2	335.8   A2	367.3   A#2	400.6   B2	435.9
    C3	473.4   C#3	513.0   D3	555.0   D#3	599.5   E3	646.6   F3	696.6   F#3	749.5
    G3	805.6   G#3	864.9   A3	927.9   A#3	994.5   B3	1065.2  C4	1140.0
    */
    // 타일 데이터 배열 (주파수, 생성 간격, 타일 길이)
    private TileData[] tileDataArray = {
            new TileData(805.6, 700, 40),
            new TileData(646.6, 1000, 40),
            new TileData(646.6, 700, 80),
            new TileData(696.6, 700, 40),
            new TileData(555.0, 1000, 40),
            new TileData(555.0, 700, 80),
            new TileData(473.4, 700, 40),
            new TileData(555.0, 700, 40),
            new TileData(646.6, 700, 40),
            new TileData(696.6, 700, 40),
            new TileData(805.6, 700, 40),
            new TileData(805.6, 1000, 40),
            new TileData(805.6, 700, 80),
    };

    public TileManager(int screenWidth, int screenHeight, Runnable repaintCallback) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.repaintCallback = repaintCallback;
        tiles = new ArrayList<>();

        // 1초 간격으로 카운트다운을 표시하는 타이머 설정
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            int countdown = 5;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdown <= 3 && countdown > 0) { // countdown이 3, 2, 1일 때만 텍스트 설정
                    countdownText = String.valueOf(countdown);
                } else if (countdown == 0) {
                    countdownText = "Start!";
                } else {
                    countdownText = ""; // 텍스트를 숨김 (countdown이 4나 5일 때)
                }

                if (countdown == -1) { // countdown이 끝나면 타이머 및 타일 생성 시작
                    ((Timer) e.getSource()).stop(); // 카운트다운 타이머 중지
                    timer.start(); // 타일 위치 업데이트 타이머 시작
                    tileCreationTimer.start(); // 타일 생성 타이머 시작
                }

                repaintCallback.run(); // 화면을 다시 그리기 위해 호출
                countdown--;
            }
        });
        countdownTimer.setRepeats(true);
        countdownTimer.start(); // 카운트다운 타이머 시작

        // 타일 생성 및 업데이트를 관리하는 타이머 설정
        timer = new Timer(33, new ActionListener() { // 33ms마다 업데이트 (약 30fps)
            @Override
            public void actionPerformed(ActionEvent e) {
                manageTiles();
                repaintCallback.run(); // 화면을 다시 그리기 위해 호출
            }
        });

        // 타일 생성용 별도 타이머 설정
        tileCreationTimer = new Timer(tileDataArray[currentTileIndex].getDelay(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTileIndex < tileDataArray.length) {
                    int x = (int) tileDataArray[currentTileIndex].getFrequency();
                    tiles.add(new Tile(x, initialYPosition, speed, tileDataArray[currentTileIndex].getLength()));
                    currentTileIndex++;

                    if (currentTileIndex < tileDataArray.length) {
                        tileCreationTimer.setDelay(tileDataArray[currentTileIndex].getDelay());
                    } else {
                        tileCreationTimer.stop();
                    }
                }
            }
        });
    }

    private void manageTiles() {
        // 모든 타일의 위치를 업데이트하고 화면 밖으로 나간 타일을 제거합니다.
        Iterator<Tile> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tile tile = iterator.next();
            tile.update(); // 타일의 y 좌표를 업데이트
            if (tile.isOutOfScreen(screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void drawTiles(Graphics g) {
        for (Tile tile : tiles) {
            tile.draw(g); // 각 타일을 화면에 그립니다.
        }

        // 화면 중앙에 카운트다운 텍스트 그리기
        if (!countdownText.isEmpty()) {
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(countdownText);
            int textHeight = fm.getAscent();
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(Color.RED);
            g.drawString(countdownText, (screenWidth - textWidth) / 2, (screenHeight + textHeight) / 2);
        }
    }
}
