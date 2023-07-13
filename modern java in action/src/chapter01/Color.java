package chapter01;

public enum Color {
    GREEN("green"),
    RED("red");

    private final String color;

    public String getColor() {
        return color;
    }

    Color(String color) {
        this.color = color;
    }
}
