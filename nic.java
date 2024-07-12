import java.util.*;

class Shape {
    int length;
    int breadth;

    public Shape(int length, int breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    public void area() {
        System.out.println(length + " " + breadth);
    }
}

class Rectangle extends Shape {

    public Rectangle(int length, int breadth) {
        super(length, breadth);
    }

    @Override
    public void area() {
        System.out.println(length * breadth);
    }
}

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        int breadth = scanner.nextInt();
        Rectangle rectangle = new Rectangle(length, breadth);
        rectangle.area();
        scanner.close();
    }
}
