
public class TileData {
    private double frequency; // 주파수 (x 좌표를 결정하는 데 사용)
    private int delay;        // 타일 생성 간격 (밀리초 단위)
    private int length;       // 타일 길이

    public TileData(double frequency, int delay, int length) {
        this.frequency = frequency;
        this.delay = delay;
        this.length = length;
    }

    public double getFrequency() {
        return frequency;
    }

    public int getDelay() {
        return delay;
    }

    public int getLength() {
        return length;
    }
}
