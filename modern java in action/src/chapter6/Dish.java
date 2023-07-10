package chapter6;


public class Dish {
    private String type;
    private String name;
    private int caloric;

    @Override
    public String toString() {
        return name;
    }

    public Dish(String type, String name, int caloric) {
        this.type = type;
        this.name = name;
        this.caloric = caloric;
    }

    public String getName() {
        return name;
    }

    public int getCaloric() {
        return caloric;
    }

    public String getType() {
        return type;
    }
}


