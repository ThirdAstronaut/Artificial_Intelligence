import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Node {

    private int id;
    private double xPosition;
    private double yPosition;

    public Node(int id, double xPosition, double yPosition) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Node(String[] params) {
        this.id = Integer.valueOf(params[0]);
        this.xPosition = Double.valueOf(params[1]);
        this.yPosition = Double.valueOf(params[2]);

    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance(Node node) {   //sqrt(a^2 + b^2) = c
        double dx = node.getxPosition() - xPosition;
        double dy = node.getyPosition() - yPosition;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

}
