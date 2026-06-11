import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

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
        
        // --- FITUR LIVE INPUT (TERMINAL INTERAKTIF) ---
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n========================================");
            System.out.println("====== SISTEM DIJKSTRA ATM GTA V ======");
            System.out.println("========================================");
            
            System.out.print("Enter the initial ATM_ID (or type '0' for exit): ");
            String inputAsal = scanner.nextLine().trim();
            
            // Fitur untuk menghentikan program
            if (inputAsal.equalsIgnoreCase("0")) {
                System.out.println("Exiting The Program...");
                break;
            }
            
            System.out.print("Enter the targeted ATM_ID (just enter for scanning whole ATM): ");
            String inputTujuan = scanner.nextLine().trim();
            
            // Mencari objek dari memori Map
            Node startNode = itemMap.get(inputAsal);
            
            // Error Catcher jika ATM Asal salah ketik
            if (startNode == null) {
                System.out.println("[ERROR] initial ATM_ID '" + inputAsal + "' not found in database!");
                continue; // Mengulang loop ke atas
            }
            
            // Logika Pintar Method Overloading
            if (inputTujuan.isEmpty()) {
                // Jika tujuan dikosongkan, panggil Method 1 Parameter
                System.out.println("\n>> Scanning the whole ATM...");
                gtaGraph.dijkstra(startNode);
            } else {
                // Jika tujuan diisi, panggil Method 2 Parameter
                Node targetNode = itemMap.get(inputTujuan);
                
                if (targetNode == null) {
                    System.out.println("[ERROR] Targeted ATM_ID '" + inputTujuan + "' not found in database!");
                } else {
                    System.out.println("\n>> Scanning the route...");
                    gtaGraph.dijkstra(startNode, targetNode);
                }
            }
        }
        
        scanner.close();
    }
}