import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private int selectedStage;
int a;
    public Main() {
        setTitle("타이틀");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // 패널 생성 및 BoxLayout으로 세로 배치 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("노래선택");
        label.setAlignmentX(CENTER_ALIGNMENT); // 중앙 정렬
        panel.add(label);

        // 스테이지 1~5 버튼 생성 및 추가
        for (int i = 1; i <= 5; i++) {
            JButton stageButton = new JButton("" + i);
            int stageNumber = i;
            stageButton.setAlignmentX(CENTER_ALIGNMENT); // 중앙 정렬
            stageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedStage = stageNumber;
                    startGame(selectedStage);  // 선택된 스테이지로 게임 시작
                }
            });
            panel.add(stageButton);
            panel.add(Box.createVerticalStrut(10)); // 버튼 간격 추가
        }

        add(panel);
        setLocationRelativeTo(null);  // 화면 중앙에 표시
        setVisible(true);
    }

    // 게임 화면을 시작하는 메서드
    private void startGame(int stage) {
        dispose(); // 현재 창 닫기
        new GameScreen(stage); // 게임 화면 생성
    }

    public static void main(String[] args) {
        new Main();
    }
}
