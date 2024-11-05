import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartGame extends JPanel {
    private TileManager tileManager;
    private ClickMelody clickMelody;
    private Image motorcycleImage;
    private int motorcycleX = 0; // 오토바이의 X좌표
    private static final int MOTORCYCLE_Y = 400; // 빨간 선의 Y좌표 (고정)

    public StartGame() {
        setPreferredSize(new Dimension(800, 500));

        // TileManager를 생성하고 repaint()를 콜백으로 전달
        tileManager = new TileManager(800, 500, this::repaint);

        // JLayeredPane을 사용하여 레이어 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 500));
        layeredPane.setLayout(null); // 자유 배치

        // ClickMelody 객체를 생성하고 가장 뒤에 추가
        clickMelody = new ClickMelody();
        clickMelody.setBounds(0, 0, 800, 500);
        layeredPane.add(clickMelody, Integer.valueOf(0)); // 가장 뒤 레이어에 추가

        // 타일을 그리는 패널을 가장 위에 추가
        JPanel tilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                tileManager.drawTiles(g); // 타일을 그리는 메서드 호출
                drawMotorcycle(g); // 오토바이 이미지를 그리기
            }

            @Override
            public boolean contains(int x, int y) {
                // 항상 false를 반환하여 마우스 이벤트를 가로채지 않음
                return false;
            }
        };
        tilePanel.setOpaque(false); // 배경을 투명하게 설정
        tilePanel.setBounds(0, 0, 800, 500);
        layeredPane.add(tilePanel, Integer.valueOf(1)); // 가장 위 레이어에 추가

        // 마우스 움직임에 따라 오토바이 위치 업데이트
        // tilePanel이 마우스 이벤트를 가로채지 않으므로 StartGame에 추가
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                motorcycleX = e.getX(); // 마우스 X좌표를 가져옴
                repaint();
            }
        });

        // LayeredPane을 StartGame에 추가
        setLayout(new BorderLayout());
        add(layeredPane, BorderLayout.CENTER);

        // 오토바이 이미지 로드
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        motorcycleImage = toolkit.getImage("src/images/yellowMotorcycle.png");

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(motorcycleImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void drawMotorcycle(Graphics g) {
        if (motorcycleImage != null) {
            // 오토바이 이미지를 고정된 Y좌표에 그림 (X좌표는 마우스에 따라 변경)
            g.drawImage(motorcycleImage, motorcycleX, MOTORCYCLE_Y, 90, 90, this);
        }
    }
}
