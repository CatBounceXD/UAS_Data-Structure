import java.util.*;

public class Graph<T> {
    public HashMap<T, ArrayList<Edge<T>>> adjList = new HashMap<>();

    private boolean isDirected;

    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public class Edge<E> {
        public E target;
        public double weight;
        public Edge(E target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    private class NodeDistance implements Comparable<NodeDistance> {
        T node;
        double distance;
        public NodeDistance(T node, double distance) {
            this.node = node;
            this.distance = distance;
        }
        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T from, T to, double weight) {
        addVertex(from);
        addVertex(to);

        adjList.get(from).add(new Edge<>(to, weight));
        if (!isDirected) {
            adjList.get(to).add(new Edge<>(from, weight));
        }
    }

    public void dijkstra(T source) {
        HashMap<T, Double> distances = new HashMap<>();
        for (T vertex : adjList.keySet()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(source, 0.0);

        PriorityQueue<NodeDistance> heap = new PriorityQueue<>();
        heap.add(new NodeDistance(source, 0.0));

        HashSet<T> settledNodes = new HashSet<>();

        while (!heap.isEmpty()) {
            T evaluationNode = heap.poll().node;

            if (settledNodes.contains(evaluationNode)) continue;
            settledNodes.add(evaluationNode);

            ArrayList<Edge<T>> listTetangga = adjList.get(evaluationNode);
            if (listTetangga == null) continue;

            for (Edge<T> tetangga : listTetangga) {
                T neighborVertex = tetangga.target;
                double weight = tetangga.weight;
                double newDistance = distances.get(evaluationNode) + weight;

                if (newDistance < distances.get(neighborVertex)) {
                    distances.put(neighborVertex, newDistance);
                    heap.add(new NodeDistance(neighborVertex, newDistance));
                }
            }
        }

        System.out.println("\nHasil dijkstra dari [" + source + "]:");
        for (Map.Entry<T, Double> entry : distances.entrySet()) {
            System.out.print("Ke " + entry.getKey() + " -> Jarak: ");
            if (entry.getValue() == Double.POSITIVE_INFINITY) {
                System.out.println("Tidak terjangkau");
            } else {
                System.out.printf("%.0f m\n", entry.getValue());
            }
        }
    }

    public void dijkstra(T source, T target) {
        HashMap<T, Double> distances = new HashMap<>();
        HashMap<T, T> previousNodes = new HashMap<>();
        for (T vertex : adjList.keySet()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
            previousNodes.put(vertex, null);
        }
        distances.put(source, 0.0);

        PriorityQueue<NodeDistance> heap = new PriorityQueue<>();
        heap.add(new NodeDistance(source, 0.0));

        HashSet<T> settledNodes = new HashSet<>();

        while (!heap.isEmpty()) {
            T evaluationNode = heap.poll().node;

            if (evaluationNode.equals(target)) break;

            if (settledNodes.contains(evaluationNode)) continue;
            settledNodes.add(evaluationNode);

            ArrayList<Edge<T>> listTetangga = adjList.get(evaluationNode);
            if (listTetangga == null) continue;

            for (Edge<T> tetangga : listTetangga) {
                T neighborVertex = tetangga.target;
                double weight = tetangga.weight;
                double newDistance = distances.get(evaluationNode) + weight;

                if (newDistance < distances.get(neighborVertex)) {
                    distances.put(neighborVertex, newDistance);
                    previousNodes.put(neighborVertex, evaluationNode);
                    heap.add(new NodeDistance(neighborVertex, newDistance));
                }
            }
        }

        System.out.println("\n=== OUTPUT ===");
        if (distances.get(target) == Double.POSITIVE_INFINITY) {
            System.out.println("Buntu! Tidak ada rute dari " + source + " ke " + target);
        } else {
            System.out.println("Target rute: " + source + " menuju " + target);
            System.out.printf("Total Jarak Tempuh: %.0f m\n", distances.get(target));

            List<T> path = new ArrayList<>();
            T curr = target;
            while (curr != null) {
                path.add(curr);
                curr = previousNodes.get(curr);
            }
            Collections.reverse(path);

            // path.size() - 1 adalah jumlah jalan/garis yang dilewati.
            System.out.println("Total Hops (Langkah): " + (path.size() - 1) + " ATM");

            System.out.println("Rute yang harus dilewati:");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }

    public void printGraph() {
        System.out.println("\n=======================================================");
        System.out.println("=================== GRAPH ATM GTA V ===================");
        System.out.println("=======================================================");
        
        List<T> vertices = new ArrayList<>(adjList.keySet());
        vertices.sort(Comparator.comparing(Object::toString));
        
        for (T vertex : vertices) {
            System.out.println("Vertex " + vertex + " terhubung ke:");
            ArrayList<Edge<T>> listTetangga = adjList.get(vertex);
            if (listTetangga == null || listTetangga.isEmpty()) {
                System.out.println("    [Tidak memiliki tetangga/neighbor]");
            } else {
                for (Edge<T> edge : listTetangga) {
                    System.out.printf("    - %s (bobot: %.0f m)\n", edge.target, edge.weight);
                }
            }
            System.out.println();
        }
        System.out.println("=======================================================\n");
    }
}