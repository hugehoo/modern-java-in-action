package chapter01;



public class Apple {
    private Color color;

    @Override
    public String toString() {
        return "{" +
            "color=" + color +
            ", weight=" + weight +
            ", quality='" + quality + '\'' +
            '}';
    }

    private double weight;
    private String quality;

    public Apple(Color color, double weight, String quality) {
        this.color = color;
        this.weight = weight;
        this.quality = quality;
    }

    public Color getColor() {
        return color;
    }

    public Double getWeight() {
        return weight;
    }

    public String getQuality() {
        return quality;
    }
}
