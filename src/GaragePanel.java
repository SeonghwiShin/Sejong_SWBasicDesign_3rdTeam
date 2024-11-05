import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GaragePanel extends JPanel {

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
