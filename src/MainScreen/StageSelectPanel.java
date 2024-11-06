package MainScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StageSelectPanel extends JPanel {
    private final Image backgroundImage; // 공유된 배경 이미지
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel imageLabel; // 이미지 라벨
    private int currentStage = 1; // 현재 스테이지
    private final Image[] stageImages; // 스테이지 이미지 배열

    public StageSelectPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.backgroundImage = RhythmGameScreen.getBackgroundImage(); // 배경 이미지 가져오기

        // 스테이지 이미지 초기화
        stageImages = new Image[]{
                new ImageIcon("src/images/full_moon.jpg").getImage(),
                new ImageIcon("src/images/marshmello_festival.jpg").getImage(),
                new ImageIcon("src/images/retrowave_car.jpg").getImage()
        };

        setLayout(new BorderLayout());

        // 중앙에 이미지와 화살표 버튼을 담을 패널 (GridBagLayout 사용)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // 배경 투명 설정
        GridBagConstraints gbc = new GridBagConstraints();

        // 왼쪽 화살표 이미지 버튼
        ImageIcon leftIcon = new ImageIcon("src/images/left_Arrow.jpg");
        Image leftImage = leftIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // 크기 조정
        JButton leftButton = new JButton(new ImageIcon(leftImage));
        leftButton.setContentAreaFilled(false);
        leftButton.setBorderPainted(false);
        leftButton.setFocusPainted(false);
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStage > 1) {
                    currentStage--;
                } else {
                    currentStage = 3;
                }
                updateStageImage();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        centerPanel.add(leftButton, gbc);

        // 가운데 이미지 라벨 설정
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 이미지에 입체감 효과 추가
                int x = (getWidth() - getIcon().getIconWidth()) / 2;
                int y = (getHeight() - getIcon().getIconHeight()) / 2;

                // 그림자 효과
                g2.setColor(new Color(0, 0, 0, 100)); // 반투명 검정색
                g2.fillRoundRect(x + 5, y + 5, getIcon().getIconWidth(), getIcon().getIconHeight(), 20, 20);

                // 테두리 효과
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(x, y, getIcon().getIconWidth(), getIcon().getIconHeight(), 20, 20);
            }
        };
        updateStageImage();
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(imageLabel, gbc);

        // 이미지 클릭 이벤트 추가 (화면 전환)
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentStage == 1) { // 스테이지 1일 때만 화면 전환
                    cardLayout.show(mainPanel, "StartGame"); // StartGame 화면으로 전환
                }
            }
        });

        // 오른쪽 화살표 이미지 버튼
        ImageIcon rightIcon = new ImageIcon("src/images/right_Arrow.jpg");
        Image rightImage = rightIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton rightButton = new JButton(new ImageIcon(rightImage));
        rightButton.setContentAreaFilled(false);
        rightButton.setBorderPainted(false);
        rightButton.setFocusPainted(false);
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStage < 3) {
                    currentStage++;
                } else {
                    currentStage = 1;
                }
                updateStageImage();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        centerPanel.add(rightButton, gbc);

        // 중앙 패널을 추가
        add(centerPanel, BorderLayout.CENTER);

        // 하단에 커스텀 Back 버튼 추가
        JButton backButton = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                if (isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gradientPaint = new GradientPaint(
                            0, 0, new Color(255, 105, 97),
                            getWidth(), getHeight(), new Color(255, 161, 97)
                    );
                    g2.setPaint(gradientPaint);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                }
                super.paintComponent(g);
            }
        };
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(120, 40));

        // 마우스 이벤트로 색상 변화 추가
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(new Color(255, 99, 71));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "MainMenu");
            }
        });

        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateStageImage();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 그리기
    }

    private void updateStageImage() {
        Image originalImage = stageImages[currentStage - 1];
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        if (width > 0 && height > 0) {
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }
}
