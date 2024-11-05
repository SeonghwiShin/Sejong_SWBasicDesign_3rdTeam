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
    private Timer timer;
    private Runnable repaintCallback;

    // 타일 데이터 배열 (주파수, 생성 간격, 타일 길이)
    private TileData[] tileDataArray = {
            new TileData(532, 700, 40),
            new TileData(405, 1000, 40),
            new TileData(405, 700, 80),
            new TileData(445, 700, 40),
            new TileData(331, 1000, 40),
            new TileData(331, 700, 80),
            new TileData(266, 700, 40),
            new TileData(331, 700, 40),
            new TileData(405, 700, 40),
            new TileData(445, 700, 40),
            new TileData(532, 700, 40),
            new TileData(532, 1000, 40),
            new TileData(532, 700, 80),
    };

    private int currentTileIndex = 0; // 현재 생성할 타일의 인덱스
    private final int speed = 8; // 타일의 속도 설정
    private final int initialYPosition = -50; // 타일의 초기 Y 위치 (화면 위에서 시작)
    private Timer tileCreationTimer;

    public TileManager(int screenWidth, int screenHeight, Runnable repaintCallback) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.repaintCallback = repaintCallback;
        tiles = new ArrayList<>();

        // 타일 생성 및 업데이트를 관리하는 타이머 설정
        timer = new Timer(33, new ActionListener() { // 33ms마다 업데이트 (약 30fps)
            @Override
            public void actionPerformed(ActionEvent e) {
                manageTiles();
                repaintCallback.run(); // 화면을 다시 그리기 위해 호출
            }
        });
        timer.start();

        // 타일 생성용 별도 타이머 설정
        tileCreationTimer = new Timer(tileDataArray[currentTileIndex].getDelay(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTileIndex < tileDataArray.length) {
                    int x = (int) tileDataArray[currentTileIndex].getFrequency(); // 주파수를 x 좌표로 변환
                    tiles.add(new Tile(x, initialYPosition, speed, tileDataArray[currentTileIndex].getLength())); // 새로운 타일을 리스트에 추가
                    currentTileIndex++; // 다음 타일 인덱스 업데이트

                    // 다음 타일의 생성 간격을 설정
                    if (currentTileIndex < tileDataArray.length) {
                        tileCreationTimer.setDelay(tileDataArray[currentTileIndex].getDelay()); // 다음 타일의 생성 간격 업데이트
                    } else {
                        tileCreationTimer.stop(); // 모든 타일이 생성되면 타이머 중지
                    }
                }
            }
        });
        tileCreationTimer.start(); // 타일 생성 타이머 시작
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
    }

    public static class GaragePanel extends JPanel {

        public GaragePanel(CardLayout cardLayout, JPanel mainPanel) {
            // 뒤로 가기 버튼
            JButton backButton = new JButton("Back");
            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            backButton.setMaximumSize(new Dimension(100, 30));
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "MainMenu");
                }
            });
            add(backButton);

            // 하단 여백
            add(Box.createVerticalStrut(20));
        }
    }
}
