import javax.swing.*;
import java.awt.*;

public class StartGame extends JPanel {
    private TileManager tileManager;
    private ClickMelody clickMelody;

    public StartGame() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.BLACK);

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
            }
        };
        tilePanel.setOpaque(false); // 배경을 투명하게 설정
        tilePanel.setBounds(0, 0, 800, 500);
        layeredPane.add(tilePanel, Integer.valueOf(1)); // 가장 위 레이어에 추가

        // LayeredPane을 StartGame에 추가
        setLayout(new BorderLayout());
        add(layeredPane, BorderLayout.CENTER);
    }
}
