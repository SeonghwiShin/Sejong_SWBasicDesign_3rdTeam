import java.awt.Graphics;

public class Tile {
    private int x; // X 좌표
    private int y; // Y 좌표
    private int speed; // 이동 속도
    private int length; // 타일의 길이

    public Tile(int x, int y, int speed, int length) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.length = length;
    }

    public void update() {
        y += speed; // y 좌표를 speed만큼 증가시켜 타일을 아래로 이동
    }

    public void draw(Graphics g) {
        g.fillRect(x, y, 20, length); // 타일을 사각형으로 그리기
    }

    public boolean isOutOfScreen(int screenHeight) {
        return y > screenHeight; // 타일이 화면 아래로 나갔는지 확인
    }
}
