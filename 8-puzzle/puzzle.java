import java.util.*;

class Node {
    int[][] state;
    int g, h, f;
    Node parent;
    String move;

    public Node(int[][] state, int g, int h, Node parent, String move) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
        this.move = move;
    }
}

public class puzzle {

    static final int[][] goal = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };

    static int manhattan(int[][] state) {
        int dist = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = state[i][j];
                if (val != 0) {
                    int goalRow = (val - 1) / 3;
                    int goalCol = (val - 1) % 3;
                    dist += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }
        return dist;
    }

    static boolean isGoal(int[][] state) {
        return Arrays.deepEquals(state, goal);
    }

    static String stateToString(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state)
            for (int val : row)
                sb.append(val);
        return sb.toString();
    }

    static int[][] copyState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++)
            newState[i] = state[i].clone();
        return newState;
    }

    static List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] state = node.state;

        int x = 0, y = 0;
        // find blank tile (0)
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (state[i][j] == 0) {
                    x = i;
                    y = j;
                }

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] moves = {"Up", "Down", "Left", "Right"};

        for (int d = 0; d < 4; d++) {
            int nx = x + dirs[d][0];
            int ny = y + dirs[d][1];

            if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                int[][] newState = copyState(state);
                // swap blank
                newState[x][y] = newState[nx][ny];
                newState[nx][ny] = 0;

                int newG = node.g + 1;
                int newH = manhattan(newState);

                neighbors.add(new Node(newState, newG, newH, node, moves[d]));
            }
        }
        return neighbors;
    }

    static void printState(int[][] state) {
        for (int[] row : state) {
            for (int val : row) System.out.print(val + " ");
            System.out.println();
        }
        System.out.println();
    }

    static void solve(int[][] start) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        HashSet<String> closed = new HashSet<>();

        Node startNode = new Node(start, 0, manhattan(start), null, "Start");
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            System.out.println("Expanding Node (Move: " + current.move + ")");
            printState(current.state);
            System.out.println("g = " + current.g + ", h = " + current.h + ", f = " + current.f);
            // System.out.println("----------------------------------");

            if (isGoal(current.state)) {
                System.out.println("Goal Found!");
                printSolution(current);
                return;
            }

            closed.add(stateToString(current.state));

            for (Node neighbor : getNeighbors(current)) {
                if (!closed.contains(stateToString(neighbor.state))) {
                    open.add(neighbor);
                }
            }
        }

        System.out.println("No solution found.");
    }

    static void printSolution(Node goalNode) {
        List<Node> path = new ArrayList<>();
        Node temp = goalNode;

        while (temp != null) {
            path.add(temp);
            temp = temp.parent;
        }
        Collections.reverse(path);

        System.out.println("\nSolution Path:");
        for (Node node : path) {
            System.out.println("Move: " + node.move);
            printState(node.state);
        }

        System.out.println("Total Moves = " + (path.size() - 1));
    }

    public static void main(String[] args) {
        int[][] start = {
                {1, 2, 3},
                {4, 0, 6},
                {7, 5, 8}
        };

        solve(start);
    }
}
