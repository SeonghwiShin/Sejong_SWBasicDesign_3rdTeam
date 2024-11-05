package MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class RhythmGameScreen extends JPanel {
    private static Image backgroundImage;
    private final JButton startButton;
    private final JButton garageButton;
    private final JButton exitButton; // EXIT 버튼 추가

    public RhythmGameScreen() {
        // 이미지 로드
        backgroundImage = new ImageIcon("src/images/introBackGround.jpg").getImage();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout()); // GridBagLayout 사용

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // 수평 방향으로 채우기
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 간격 설정

        // 'Game Start' 버튼 중앙에 배치
        startButton = createButton("Game Start", new Font("Segoe UI", Font.BOLD, 45));
        gbc.gridx = 0; // x 위치
        gbc.gridy = 0; // y 위치
        add(startButton, gbc);

        // 'Garage' 버튼 중앙에 배치
        garageButton = createButton("Garage", new Font("Segoe UI", Font.PLAIN, 30));
        gbc.gridy = 1; // y 위치 변경
        add(garageButton, gbc);

        // 'EXIT' 버튼 중앙에 배치
        exitButton = createButton("EXIT", new Font("Segoe UI", Font.PLAIN, 30));
        gbc.gridy = 2; // y 위치 변경
        add(exitButton, gbc);
    }

    // 버튼 생성 메서드
    private JButton createButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false); // 테두리 제거
        button.setContentAreaFilled(false); // 배경 투명 설정

        // MouseListener 추가
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 99, 71)); // 토마토색
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE); // 기본 색상
            }
        });

        // 버튼 클릭 이벤트
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("EXIT")) {
                    System.exit(0); // 프로그램 종료
                } else if (text.equals("Game Start")) {
                    CardLayout cl = (CardLayout) getParent().getLayout();
                    cl.show(getParent(), "StageSelect");
                } else if (text.equals("Garage")) {
                    CardLayout cl = (CardLayout) getParent().getLayout();
                    cl.show(getParent(), "GarageSelect");
                }
            }
        });

        return button;
    }



    // 배경 이미지를 가져오는 메서드
    public static Image getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
