
public class TileData {
    private double frequency; // 주파수 (x 좌표를 결정하는 데 사용)
    private int time;        // 타일 생성 시간 (한 박=1000)
    private int length;       // 타일 길이

    public TileData(double frequency, int time, int length) {
        this.frequency = frequency;
        this.time = time;
        this.length = length;
    }

    public double getFrequency() {
        return frequency;
    }

    public int getTime() {
        return time;
    }

    public int getLength() {
        return length;
    }
}
