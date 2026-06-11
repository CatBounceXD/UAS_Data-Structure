public class Node {
    public String id;
    public String name;
    public double x, y, z;

    public Node(String id, String name, double x, double y, double z) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return this.id;
    }
}