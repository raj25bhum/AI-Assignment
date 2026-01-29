import java.util.*;

class Node {
    int[][] state;
    int g, h;
    Node parent;

    Node(int[][] state, int g, int h, Node parent) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    int f() { return g + h; }

    String key() {
        StringBuilder sb = new StringBuilder();
        for (int[] r : state)
            for (int c : r)
                sb.append(c);
        return sb.toString();
    }
}

public class EightPuzzleAStar {

    static int[][] goal = {
        {1,2,3},
        {4,5,6},
        {7,8,0}
    };

    static int manhattan(int[][] s){
        int d=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                int val=s[i][j];
                if(val==0) continue;
                int gi=(val-1)/3;
                int gj=(val-1)%3;
                d+=Math.abs(i-gi)+Math.abs(j-gj);
            }
        }
        return d;
    }

    static List<int[][]> neighbors(int[][] s){
        List<int[][]> list=new ArrayList<>();
        int zx=0,zy=0;

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(s[i][j]==0){zx=i;zy=j;}

        int[][] dir={{1,0},{-1,0},{0,1},{0,-1}};

        for(int[] d:dir){
            int nx=zx+d[0], ny=zy+d[1];
            if(nx>=0&&ny>=0&&nx<3&&ny<3){
                int[][] ns=new int[3][3];
                for(int i=0;i<3;i++)
                    ns[i]=s[i].clone();
                ns[zx][zy]=ns[nx][ny];
                ns[nx][ny]=0;
                list.add(ns);
            }
        }
        return list;
    }

    static void printPath(Node n){
        List<Node> path=new ArrayList<>();
        while(n!=null){path.add(n); n=n.parent;}
        Collections.reverse(path);

        for(Node p:path){
            for(int[] r:p.state){
                for(int c:r) System.out.print(c+" ");
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args){

        int[][] start={
            {1,2,3},
            {4,0,6},
            {7,5,8}
        };

        PriorityQueue<Node> open=new PriorityQueue<>(Comparator.comparingInt(Node::f));
        Set<String> closed=new HashSet<>();

        Node root=new Node(start,0,manhattan(start),null);
        open.add(root);

        while(!open.isEmpty()){
            Node cur=open.poll();

            if(Arrays.deepEquals(cur.state,goal)){
                printPath(cur);
                return;
            }

            closed.add(cur.key());

            for(int[][] nb:neighbors(cur.state)){
                Node child=new Node(nb,cur.g+1,manhattan(nb),cur);
                if(!closed.contains(child.key()))
                    open.add(child);
            }
        }
    }
}
