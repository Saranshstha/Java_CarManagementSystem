import view.LoginGUI;
import controller.Car;

public class Main {
    public static void main(String[] args) {
        Car.loadFromFile();
        new LoginGUI();
    }
}
