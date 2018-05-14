
/**
 * Write a description of class AdjListsGraphProject here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;
public class AdjListsGraph<T> //implements Graph<T>
{
    // instance variables - 
    private Vector<T> vertices;
    private Vector<LinkedList<T>> arcs;

    /**
     * Constructor for objects of class AdjListsGraphProject
     */
    public AdjListsGraph(){
        vertices = new Vector<T>();
        arcs = new Vector<LinkedList<T>>();
        
    }
    //clone method
    
    public boolean isEmpty(){
        return vertices.isEmpty(); // or this.getNumVertices() == 0
    }
    
    public int getNumVertices(){
        return vertices.size();
    }
    
    public int getNumArcs(){
        int n = 0;
        for (int i = 0; i<arcs.size(); i++){
            n += arcs.get(i).size();
        }
        return n;
    }
    
    public void addVertex (T vertex){
        //if vertex already in the graph, do not add it again
        if (vertices.contains(vertex)){return;}
        
        //add vertex
        vertices.addElement(vertex);
        //add its corresponding empty list of connections
        arcs.addElement(new LinkedList<T>());
        //System.out.println("size of vertices: " + vertices.size());
    }
    //make new courseStudent connection to add to linkedList
    /**
     * adds course connection between two students
     * insert both ways
     */
        private void addArc (T student1, T student2, String course){//n1 and n2 already subtract 1
        //if n1 or n2 are invalid
        if(!vertices.contains(student1)||!vertices.contains(student2)){
            return;
        }
        int n1 = vertices.indexOf(student1);
        StudentCourseLabel label = new StudentCourseLabel(student2, course);
        
        LinkedList<T> l = arcs.get(n1);
        //if n1 to n2 arc already exists
        if(l.contains(label)){return;}
        
        l.add(label);
    }
    /**
     * adds arc based on object indices
     */
    public void addArc (int v1, int v2, String course){
        T student1 = vertices.get(v1);
        T student2 = vertices.get(v2);
        addArc(student1, student2, course);
    }
    
    public void removeVertex (T v){
        if(!vertices.contains(v)){return;}
        int index = vertices.indexOf(v);
        vertices.remove(index);
        arcs.remove(index);
        //remove from all linked list wherever it appears
        for(int i = 0; i<arcs.size(); i++){
            LinkedList<T> ll = arcs.get(i);
            ll.remove(v);
        }
    }
    
    public static AdjListsGraph<String> AdjListsGraphFromFile(String f){
        AdjListsGraph<String> g = new AdjListsGraph<String>();
        try{
            Scanner scan = new Scanner(new File(f));
            while(!scan.next().equals("#")){//read and discard the token
                //int n = scan.nextInt();
                String s = scan.next();
                g.addVertex(s);
            }
            while(scan.hasNext()){
                int n1 = scan.nextInt();
                int n2 = scan.nextInt();
                g.addArc(n1-1, n2-1);
            }
            scan.close();
        }catch(IOException e){
            System.out.println("Cannot read from " + f);
        }
        return g;
    }
    
    public void saveToTGF(String f){
        //System.out.println("size of vertices: " + vertices.size());
        try{
            PrintWriter printer = new PrintWriter(new File(f));
            for (int i = 0; i<vertices.size(); i++){
                printer.println((i+1) + " " + vertices.get(i));
            }
            printer.println("#");
            
            //go down the arcs vector
            for (int i = 0; i<arcs.size(); i++){
                LinkedList<T> ll = arcs.get(i);
                for (int j = 0; j<ll.size(); j++){
                    T vertex = ll.get(j);
                    int to = vertices.indexOf(vertex);
                    printer.println((i+1) + " " + (to+1));
                }
            }
            printer.close();
        }catch(IOException e){
            System.out.println("Cannot write in the file " + f);
        }
    }
    
    public LinkedList<T> getSuccessors(T vertex){
        int index = vertices.indexOf(vertex);
        return arcs.get(index);
    }
    
    public LinkedList<T> getPredecessors(T vertex){
        LinkedList<T> temp = new LinkedList<T>();
        for(int i = 0; i<arcs.size(); i++){
            LinkedList<T> ll = arcs.get(i);
            if(ll.contains(vertex)){
                temp.add(vertices.get(i));
            }
        }
        return temp;
    }
    public boolean containsVertex( T vertex){
        return vertices.contains(vertex);
    }
    public LinkedList<T> dfsTraversal(T start, T end) {
        LinkedList<T> search = new LinkedList<T>();
        AdjListsGraph<T> graph = (AdjListsGraph<T>)this.clone();
        if(!graph.containsVertex(start)){
            return search;
        }
        T currentVertex;
        LinkedList<T> traversalStack = new LinkedList<T>();
        
         traversalStack.push(start);
         search.add(start);
        //return when we find end
         while (!traversalStack.isEmpty()||!search.contains(end))
         {
            currentVertex = traversalStack.peek();
            int index = graph.vertices.indexOf(currentVertex);
            LinkedList<T> connections = graph.arcs.get(index);
            if(connections.isEmpty()){
                traversalStack.pop();
            }
            else{
                T nextNode = connections.get(0);
                traversalStack.push(nextNode);
                if(!search.contains(nextNode)){
                    search.add(nextNode);
                }
                connections.pop();
            }
             
        }
        return search;
     }
    /******************************************************************
//    Returns a string representation of the graph. 
//    ******************************************************************/
  public String toString() {
    if (vertices.size() == 0) return "Graph is empty";
    
    String result = "Vertices: \n";
    result = result + vertices;
    
    result = result + "\n\nEdges: \n";
    for (int i=0; i< vertices.size(); i++)
      result = result + "from " + vertices.get(i) + ": "  + arcs.get(i) + "\n";
   
    return result;
  }
    public static void main(String[] args){
        AdjListsGraph<String> g0 = new AdjListsGraph<String>();
        //test isEmpty
        //System.out.println("Testing isEmpty. Expecting: true. Got: " + g0.isEmpty());
        //test getNumArcs
        //System.out.println("Testing getNumArcs. Expecting: 0. Got: " + g0.getNumArcs());
        //test getNumVertices
        //System.out.println("Testing getNumVertices. Expecting: 0. Got: " + g0.getNumVertices());
        
        g0.addVertex("A");
        //g0.addVertex("Z");
        //g0.addVertex("K");
        //g0.saveToTGF("test0.tgf");
        
        // AdjListsGraph<String> g = AdjListsGraphFromFile("g5.tgf");
        // //System.out.println("The number of vetices: (3)" + g.getNumVertices());
        // //System.out.println("The number of arcs: (3)" + g.getNumArcs());
        // //g.saveToTGF("gOut.tgf");
        
        // System.out.println("Predecessors of 3: [1,2,4] " + g.getPredecessors("3"));
        // System.out.println("Successors of 2: [3] " + g.getSuccessors("2"));
        // System.out.println("Predecessors of 5: [] " + g.getPredecessors("5"));
        // System.out.println("Successors of 5: [] " + g.getSuccessors("5"));
    }
}
