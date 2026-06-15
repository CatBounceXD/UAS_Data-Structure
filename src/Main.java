import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // (false = Undirected, true = Directed)
        Graph<Node> gtaGraph = new Graph<>(false);
        
        HashMap<String, Node> itemMap = new HashMap<>();

        // VERTEX
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
                itemMap.put(id, newAtm);
                gtaGraph.addVertex(newAtm);
            }
            System.out.println("Coordinates loaded to Generic Graph");
        } catch (Exception e) {
            System.out.println("Error reading ATM_with_IDs.csv: " + e.getMessage());
        }

        // EDGES
        System.out.println("Reading Edges...");
        try (BufferedReader br = new BufferedReader(new FileReader("ATM_Edges2.csv"))) {
            String line;
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String fromId = data[0].trim();
                    String toId = data[1].trim();

                    Node fromNode = itemMap.get(fromId);
                    Node toNode = itemMap.get(toId);
                    
                    if (fromNode != null && toNode != null) {
                        try {
                            double weight = Double.parseDouble(data[2].trim());
                            gtaGraph.addEdge(fromNode, toNode, weight);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
            System.out.println("Edges loaded successfuly");
        } catch (Exception e) {
            System.out.println("Error reading ATM_Edges2.csv: " + e.getMessage());
        }
        
        // gtaGraph.printGraph();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n========================================");
            System.out.println("====== SISTEM DIJKSTRA ATM GTA V ======");
            System.out.println("========================================");
            
            String inputAsal = "";
            Node startNode = null;
            
            while (true) {
                System.out.print("Enter the initial ATM_ID (or type '0' for exit): ");
                inputAsal = scanner.nextLine().trim();
                
                if (inputAsal.equalsIgnoreCase("0")) {
                    break;
                }
                
                // dengan regex
                if (!inputAsal.matches("ATM_\\d{3}")) {
                    System.out.println("[ERROR] Invalid format! Must be 'ATM_' followed by exactly 3 digits (e.g., ATM_001). Try again.\n");
                    continue;
                }
                
                startNode = itemMap.get(inputAsal);
                if (startNode == null) {
                    System.out.println("[ERROR] initial ATM_ID '" + inputAsal + "' not found in database! Try again.\n");
                    continue;
                }
                
                break;
            }
            
            if (inputAsal.equalsIgnoreCase("0")) {
                System.out.println("Exiting The Program...");
                break;
            }

            String inputTujuan = "";
            Node targetNode = null;
            
            while (true) {
                System.out.print("Enter the targeted ATM_ID (just enter for scanning whole ATM): ");
                inputTujuan = scanner.nextLine().trim();
                
                if (inputTujuan.isEmpty()) {
                    break;
                }
                
                if (!inputTujuan.matches("ATM_\\d{3}")) {
                    System.out.println("[ERROR] Invalid format! Must be 'ATM_' followed by exactly 3 digits (e.g., ATM_001). Try again.\n");
                    continue;
                }
                
                targetNode = itemMap.get(inputTujuan);
                if (targetNode == null) {
                    System.out.println("[ERROR] Targeted ATM_ID '" + inputTujuan + "' not found in database! Try again.\n");
                    continue;
                }
                
                break;
            }
            
            // --- EKSEKUSI DIJKSTRA ---
            if (inputTujuan.isEmpty()) {
                System.out.println("\n>> Scanning the whole ATM...");
                gtaGraph.dijkstra(startNode);
            } else {
                System.out.println("\n>> Scanning the route...");
                gtaGraph.dijkstra(startNode, targetNode);
            }
        }
        
        scanner.close();
    }
}