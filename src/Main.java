import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        // (false = Undirected, true = Directed)
        Graph<Node> gtaGraph = new Graph<>(false);
        
        // HashMap lokal pembantu untuk melacak kecocokan ID String dari CSV ke objek Node
        HashMap<String, Node> itemMap = new HashMap<>();

        // 1. MEMBACA FILE VERTEX (ATM)
        System.out.println("Reading Coordinates...");
        try (BufferedReader br = new BufferedReader(new FileReader("ATM_with_IDs.csv"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0].trim();
                String name = data[1].trim();
                double x = Double.parseDouble(data[2].trim());
                double y = Double.parseDouble(data[3].trim());
                double z = Double.parseDouble(data[4].trim());
                
                Node newAtm = new Node(id, name, x, y, z);
                itemMap.put(id, newAtm); // Masukkan ke peta pencarian lokal
                gtaGraph.addVertex(newAtm); // Daftarkan objek Node ke struktur graf generic
            }
            System.out.println("Coordinates loaded to Generic Graph");
        } catch (Exception e) {
            System.out.println("Error reading ATM_with_IDs.csv: " + e.getMessage());
        }

        // 2. MEMBACA FILE EDGES BESERTA BOBOT MANUALNYA
        System.out.println("Reading Edges...");
        try (BufferedReader br = new BufferedReader(new FileReader("ATM_Edges3.csv"))) {
            String line;
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String fromId = data[0].trim();
                    String toId = data[1].trim();
                    
                    // Tarik objek Node asli berdasarkan string ID yang tertulis di CSV Edges
                    Node fromNode = itemMap.get(fromId);
                    Node toNode = itemMap.get(toId);
                    
                    if (fromNode != null && toNode != null) {
                        try {
                            double weight = Double.parseDouble(data[3].trim());
                            gtaGraph.addEdge(fromNode, toNode, weight);
                        } catch (NumberFormatException e) {
                            // Abaikan baris jika bobot di Excel belum terisi (kosong)
                        }
                    }
                }
            }
            System.out.println("Edges loaded successfuly");
        } catch (Exception e) {
            System.out.println("Error reading ATM_Edges3.csv: " + e.getMessage());
        }

        
        gtaGraph.printGraph();
        
        // Ambil objek dari memori (Biarkan saja dua baris ini tetap hidup)
        Node startNode = itemMap.get("ATM_001");
        Node targetNode = itemMap.get("ATM_072"); 
        
        if (startNode != null && targetNode != null) {
            gtaGraph.dijkstra(startNode);
            // gtaGraph.dijkstra(startNode, targetNode);
        } else {
            System.out.println("Error: The first ATM or target ATM not found in CSV!");
        }
    }
}