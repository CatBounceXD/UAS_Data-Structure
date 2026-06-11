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

    // Trik cerdas: saat objek Node dipanggil di cetakan Generic, Java otomatis mencetak ID-nya
    @Override
    public String toString() {
        return this.id;
    }
}