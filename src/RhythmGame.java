import MainScreen.GaragePanel;
import MainScreen.RhythmGameScreen;
import MainScreen.StageSelectPanel;

import javax.swing.*;
import java.awt.*;

public class RhythmGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public RhythmGame() {
        // CardLayout 초기화
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 메인 화면 패널 추가
        mainPanel.add(new RhythmGameScreen(), "MainMenu");
        // 단계 선택 패널 추가
        mainPanel.add(createStageSelectPanel(), "StageSelect");
        // 차고지 선택 패널 추가
        mainPanel.add(createGaragePanel(), "GarageSelect");
        // StartGame 패널 추가 (TileManager 및 ClickMelody 포함)
        mainPanel.add(new StartGame(), "StartGame");

        add(mainPanel);
        setTitle("Rhythm Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setVisible(true);

        // 기본 화면을 메인 메뉴로 설정
        cardLayout.show(mainPanel, "MainMenu");
    }

    private JPanel createStageSelectPanel() {
        return new StageSelectPanel(cardLayout, mainPanel);
    }

    private JPanel createGaragePanel() {
        return new GaragePanel(cardLayout, mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RhythmGame::new);
    }
}
