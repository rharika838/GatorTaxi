import java.io.*;
import java.util.ArrayList;

// Node Class for Red Black Tree
class Node {
    int rideNumber;
    int tripCost;
    int tripDuration;
    Node parent; //Parent Pointer
    Node left; // Left Child Pointer
    Node right; // Right Child Pointer
    int color; // Black = 0, Red = 1
    HeapNode heapRef;   // stores the node's pointer to the Heap

    public Node(int rideNumber, int tripCost, int tripDuration) {
        this.rideNumber = rideNumber;
        this.tripCost = tripCost;
        this.tripDuration = tripDuration;
    }

}

// Node class for the heap.
class HeapNode {
    int rideNumber;
    int tripCost;
    int tripDuration;
    Node rbTreference;  // stores the node's pointer to RB tree
    int myIndexInHeap;

    //Construtor of the HeapNode.
    public HeapNode(int rideNumber, int tripCost, int tripDuration) {
        this.rideNumber = rideNumber;
        this.tripCost = tripCost;
        this.tripDuration = tripDuration;
    }

}

// This class implements the functions in Red Black Tree
class RedBlackTree {
    public Node root;
    public Node TNULL;  //Represents a NULL node.

    // Constructor for the Red Black Tree.
    public RedBlackTree() {
        TNULL = new Node(0, 0, 0);
        TNULL.color = 0;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    // Helps find the ROOT of the Red Black Tree.
    public Node getRoot() {
        return this.root;
    }

    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    // Helps return a node with the minimum key.
    public Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }
    
    // Helps return a node with the maximum key.
    public Node maximum(Node node) {
        while (node.right != TNULL) {
            node = node.right;
        }
        return node;
    }

    // Helps return the predecessor of any node.
    public Node predecessor(Node x) {
        // if the left subtree is not null,
        // the predecessor is the rightmost node in the
        // left subtree
        if (x.left != TNULL) {
            return maximum(x.left);
        }

        Node y = x.parent;
        while (y != TNULL && x == y.left) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    // Helps return the successor of any node.
    public Node successor(Node x) {
        // if the right subtree is not null,
        // the successor is the leftmost node in the
        // right subtree
        if (x.right != TNULL) {
            return minimum(x.right);
        }

        // else it is the lowest ancestor of x whose
        // left child is also an ancestor of x.
        Node y = x.parent;
        while (y != TNULL && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    // Helps rotate a node at LEFT.
    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Helps rotate a node at RIGHT.
    public void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Inserts node into the required position and fixes the tree according to Red Black Tree Properties.
    public Node insertIntoRBTree(Node newNode) {
        // Ordinary Binary Search Insertion
        Node node = new Node(newNode.rideNumber, newNode.tripCost, newNode.tripDuration);
        node.parent = null;
        // node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = 1; // new node must be red

        Node y = null;
        Node x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.rideNumber < x.rideNumber) {
                x = x.left;
            } 
            else {
                x = x.right;
            }
        }

        // y is parent of x
        node.parent = y;
        if (y == null) {
            root = node;
        } 
        else if (node.rideNumber < y.rideNumber) {
            y.left = node;
        } 
        else {
            y.right = node;
        }

        // if new node is a root node, simply return
        if (node.parent == null) {
            node.color = 0;
            return node;
        }

        // if the grandparent is null, simply return
        if (node.parent.parent == null) {
            return node;
        }

        // Fix the tree
        adjustInsert(node);
        return node;
    }

    // Helps update the Red Black Tree based on its properties.
    private void adjustInsert(Node k) {
        Node u;
        while (k.parent.color == 1) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // uncle
                if (u.color == 1) {
                    // case 3.1
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        // case 3.2.2
                        k = k.parent;
                        rightRotate(k);
                    }
                    // case 3.2.1
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right; // uncle

                if (u.color == 1) {
                    // mirror case 3.1
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        // mirror case 3.2.2
                        k = k.parent;
                        leftRotate(k);
                    }
                    // mirror case 3.2.1
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

    // Helps delete a node from the tree.
    public HeapNode deleteNodeFromRBTree(int data) {
        return deleteNodeHelper(this.root, data);
    }

    //Helps the deleteNode() function.
    private HeapNode deleteNodeHelper(Node node, int key) {
        // find the node containing key
        Node z = TNULL;
        Node x, y;
        while (node != TNULL) {
            if (node.rideNumber == key) {
                z = node;
            }

            if (node.rideNumber <= key) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            System.out.println("Couldn't find key in the tree");
            return null;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            adjustDelete(x);
        }
        return z.heapRef;
    }

    // Helps update the Red Black Tree after deletiion is done.
    private void adjustDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == 0 && s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        // case 3.3
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == 0 && s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        // case 3.3
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }

    //Helps search a ride in the Red Black Tree using Binary Search Tree Property. 
    public Node searchSingleRide(int rideNumber) {
        // Traversing the Binary Search Tree
        Node currNode = root;
        while (currNode != null) { 
            if (currNode.rideNumber == rideNumber) {
                return currNode;
            }
            if(currNode.rideNumber<rideNumber)
                currNode = currNode.right;  // SEARCH RIGHT SUBTREE IF CURRENT NODE'S VALUE IS LESSER THAN THE REQUIRED KEY
            else
                currNode = currNode.left;   // SEARCH LEFT SUBTREE IF CURRENT NODE'S VALUE IS GREATER THAN THE REQUIRED KEY
        }
        return TNULL;    // RETURN NULL IF THE KEY IS NOT FOUND
    }

    //Performs a IN-ORDER Traversal in Red Black Tree, within the required range low-high.
    public void searchRidesInRange(int low, int high, Node root, ArrayList<Node> res){
    
    if(root==null)  // BASE CASE RETURN NULL IF THE ROOT ITSELF IS NULL
        return;
    
    /*
        IF CURRENT NODE'S VALUE IS BETWEEN THE CURRENT RANGE, PRINT IT. 
        ALSO RECURSIVELY KEEP SEARCHING LEFT AND RIGHT SUBTREE FOR MORE NODES WITHIN THAT RANGE
    */
    if(root.rideNumber>=low && root.rideNumber<=high){   
        searchRidesInRange(low, high, root.left, res);
        res.add(root);
        searchRidesInRange(low, high, root.right, res);
    }
    else if(root.rideNumber<low)   // SEARCH RIGHT SUBTREE IF CURRENT NODE'S VALUE IS LESSER THAN THE LOWER LIMIT
        searchRidesInRange(low, high, root.right, res); 
    else                    // SEARCH LEFT SUBTREE IF CURRENT NODE'S VALUE IS GREATER THAN THE HIGHEST LIMIT
        searchRidesInRange(low, high, root.left, res);   
}

}

class Heap {
    // Class member variables
    public HeapNode[] Heap;
    public int size;
    public int maxsize;
    // Initializing front as static with unity
    private static final int FRONT = 1;
    
    // Constructor of this class
    public Heap(int maxsize) {

        // This keyword refers to current object itself
        this.maxsize = maxsize;
        this.size = 0;

        Heap = new HeapNode[this.maxsize + 1];
        HeapNode defaultNode = new HeapNode(0, 0, 0);
        Heap[0] = defaultNode;
    }
    
    //Helps find ROOT of the Heap.
    public HeapNode getRoot(){
        return size>=1?Heap[1]:null; 
    }

    // Get PARENT position of any node.
    private int parentPosition(int pos) {
        return pos / 2;
    }

    // Get position of the RIGHT CHILD of any node.
    private int rChildPosition(int pos) {
        return (2 * pos) + 1;
    }

    // Get position of the LEFT CHILD of any node.
    private int lChildPosition(int pos) {
        return (2 * pos);
    }

    // Results in TRUE if a node is LEAF node.
    private boolean checkForLeaf(int pos) {

        if (pos > (size / 2)) {
            return true;
        }

        return false;
    }

    // Heapifies from node 'pos'.
    private void minHeapify(int pos) {
        if (!checkForLeaf(pos)) {
            int swapPos = pos;
            // swap with the minimum of the two children
            // to check if right child exists. Otherwise default value will be '0'
            // and that will be swapped with parent node.
            if (rChildPosition(pos) <= size){
                if(Heap[lChildPosition(pos)].tripCost == Heap[rChildPosition(pos)].tripCost){
                    swapPos = Heap[lChildPosition(pos)].tripDuration < Heap[rChildPosition(pos)].tripDuration ? lChildPosition(pos)
                    : rChildPosition(pos);
                }else{
                    swapPos = Heap[lChildPosition(pos)].tripCost < Heap[rChildPosition(pos)].tripCost ? lChildPosition(pos)
                    : rChildPosition(pos);
                }
            }
                
            else
                swapPos = lChildPosition(pos);

            if(Heap[pos].tripCost==Heap[lChildPosition(pos)].tripCost || Heap[pos].tripCost==Heap[rChildPosition(pos)].tripCost){
                if (Heap[pos].tripDuration > Heap[lChildPosition(pos)].tripDuration
                    || Heap[pos].tripDuration > Heap[rChildPosition(pos)].tripDuration){
                        swap(pos,swapPos);
                        minHeapify(swapPos);
                    }
            }else{
                if (Heap[pos].tripCost > Heap[lChildPosition(pos)].tripCost
                    || Heap[pos].tripCost > Heap[rChildPosition(pos)].tripCost) {
                swap(pos, swapPos);
                minHeapify(swapPos);
            }
        }
        }
    }

    // Helps SWAP two nodes in the HEAP. Also updates individual index references.
    private void swap(int fpos, int spos) {
        HeapNode tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
        // SWAP INDEXES FOR O(1) ACCESS
        int tempIndex = Heap[fpos].myIndexInHeap;
        Heap[fpos].myIndexInHeap = Heap[spos].myIndexInHeap;
        Heap[spos].myIndexInHeap = tempIndex;
    }

    // To insert a node into the heap
    public void insert(HeapNode element) {

        if (size >= maxsize) {
            return;
        }
        Heap[++size] = element;
        element.myIndexInHeap = size;
        int current = size;
        // while (Heap[current].tripCost < Heap[parent(current)].tripCost) {
        while (compareRides(Heap[current], Heap[parentPosition(current)])<0) {
            swap(current, parentPosition(current));
            current = parentPosition(current);
        }
    }

    //Helps remove the MINIMUM node from the Heap.
    public HeapNode removeMin() {
        if(size==0)
            return null;
        HeapNode popped = Heap[FRONT];
        deleteRandomNode(popped);
        return popped;
    }

    //Deletes a RANDOM Node from the Heap using Red Black Tree Reference.
    public void deleteRandomNode(HeapNode heapNodeRefOfDeletedNode) {

        int indexOfNodeToDelete = heapNodeRefOfDeletedNode.myIndexInHeap;
        if(indexOfNodeToDelete==size){
            size--;
        }
        else{
            swap(indexOfNodeToDelete, size);
            size--;
            minHeapify(FRONT);
        }

    }

    // Prints all the elements of a heap.
    public void print() {
        System.out.println("\n\nsize = " + size);
        for (int i = 1; i <= size / 2; i++) {

            int parent = Heap[i].rideNumber;
            int left = -1, right = -1;
            if (Heap[2 * i] != null)
                left = Heap[2 * i].rideNumber;

            if (Heap[2 * i + 1] != null)
                right = Heap[2 * i + 1].rideNumber;

            // Printing the parent and both childrens
            System.out.print("Parent = " + parent +" at i= "+i+" & myIndex = "+Heap[i].myIndexInHeap);
            if (left != -1)
                System.out.print("Left = " + left);
            else
                System.out.print("Left = NULL");

            if (right != -1)
                System.out.print("Right = " + right);
            else
                System.out.print("Right = NULL");
            System.out.println();
        }
    }

    //Helps print and validate the connection between Heap and Red Black Tree using Red Black Tree Reference.
    public void printConnection() {
        System.out.println("\n\n");
        for (int i = 1; i <= size; i++) {
            if (Heap[i] != null) {
                System.out.println(
                        Heap[i].rideNumber + " , " +
                        Heap[i].rbTreference.tripCost + " , " +
                        Heap[i].rbTreference.tripDuration +" , "+
                        " i= "+i+" & myIndex = "+Heap[i].myIndexInHeap
                        );
            }

        }

    }
    
    //This is the custom Comparator based on TripCost and TripDuration.
    private int compareRides(HeapNode h1,HeapNode h2){
        int costCompare = Integer.compare(h1.tripCost, h2.tripCost);
        return costCompare!=0? costCompare:Integer.compare(h1.tripDuration, h2.tripDuration);
    }

    //Helps Heapify after any changes in the Heap.
    public void fixUpdate(){
        minHeapify(FRONT);
    }

}

// ----------------------------------- MAIN FUNCTION -----------------------------------
// -------------------------------------------------------------------------------------



public class gatorTaxi {

    /* 
        Accept a list of RB nodes and formats the list of nodes in a string format 
        It attaches brackets and handles commas. Then returns the formatted string back to the
        function call in main.
    */
    static String helperUtility(ArrayList<Node> result){
        StringBuilder formattedAns = new StringBuilder();
        for(int i=0;i<result.size();i++){
            StringBuilder ansString = new StringBuilder();
            Node curr = result.get(i);
            int rideNumber = curr.rideNumber;
            int tripCost = curr.tripCost;
            int tripDuration = curr.tripDuration;
            ansString.append("(").append(rideNumber).append(",")
                        .append(tripCost).append(",").append(tripDuration).append(")");
            
            if(i!=result.size()-1)
                ansString.append(",");
            formattedAns.append(ansString);
        }
        return formattedAns.toString();
    }

    //Main function of gatorTaxi.
    public static void main(String[] args) throws IOException {
        FileReader freader = null;  // Helps read from the file.
        FileWriter fWriter = null;  // Helps write into the file.

        RedBlackTree rbT = new RedBlackTree();  //Red Black Tree Class Object.
        Heap myHeap = new Heap(2000);   //Heap Class Object with a MAXSIZE = 2000.

        // File Reading-Writing Utilities
        try{
            String fileName = args[0];
            freader = new FileReader(fileName);
            fWriter = new FileWriter("output_file.txt");

        }catch(Exception e){    // If incase the file read throws an exception.
            e.printStackTrace();
        }
        // FILE READING UTILITIES
        BufferedReader br = new BufferedReader(freader);
        String line;
        while((line = br.readLine())!=null){    // TRAVERSE ENTIRE FILE TO RETRIEVE QUERIES

            line = line.trim();
            
            String[] arr = line.split("\\(");
                    
            switch(arr[0]){
                case "Insert":
                    /*
                        INSERT NODE 
                    */
                    if(arr[1].contains(",")){
                        // arr[1] = rideNumber, rideCost, tripDuration)
                        String[] subarr = arr[1].split(",");
                        // subarr = [ridenumber:rideCost:tripDuration)]
                        int rideNumber = Integer.parseInt(subarr[0].trim());

                        Node alreadyExistNodeInTree = rbT.searchSingleRide(rideNumber);
                        if(alreadyExistNodeInTree!=rbT.TNULL){
                            fWriter.write("Duplicate Ride Number\n");
                        }
                        else{
                            int tripCost = Integer.parseInt(subarr[1].trim());
                            int tripDuration = Integer.parseInt(subarr[2].substring(0,subarr[2].length()-1).trim());
    
                            // INSERT FUNCTIONS
                            Node newRBNode = new Node(rideNumber,tripCost,tripDuration);
                            HeapNode newHeapNode = new HeapNode(rideNumber,tripCost,tripDuration);
    
                            Node insertedRBNode = rbT.insertIntoRBTree(newRBNode);
                            myHeap.insert(newHeapNode);
    
                            // Connect both new nodes to maintain reference pointers.
                            newHeapNode.rbTreference = insertedRBNode;
                            insertedRBNode.heapRef = newHeapNode;
                        }
                    }
                    break;
                case "GetNextRide":
                    /*
                        GET THE NEXT RIDE AND REMOVE FROM BOTH DATA STRUCTURES.
                    */
                    myHeap.printConnection();
                    HeapNode bestRide = myHeap.removeMin();
                    if(bestRide!=null){
                        StringBuilder sb = new StringBuilder();
                        sb.append("(").append(bestRide.rideNumber).append(",").append(bestRide.tripCost).append(",").append(bestRide.tripDuration).append(")\n");
                        fWriter.write(sb.toString());
                        rbT.deleteNodeFromRBTree(bestRide.rideNumber);
                    }
                    else{
                        fWriter.write("No active ride requests\n");
                    }
                    break;
                case "Print":  
                    /*
                            -----------SEARCH QUERY IN A RANGE--------------
                            1. RETRIEVE BOUNDARIES LOW AND HIGH.
                            2. RECURSIVELY SEARCH ALL NODES IN THAT RANGE.
                            3. FORMAT THE OUTPUT AS DESIRED.
                            4. WRITE TO FILE.
                    */
                    if(arr[1].contains(",")){
                        String[] subarr = arr[1].split(",");
                        int from = Integer.parseInt(subarr[0]);
                        int to =  Integer.parseInt(subarr[1].substring(0,subarr[1].length()-1));
                        
                        ArrayList<Node> foundNodesArray = new ArrayList<>();
                        rbT.searchRidesInRange(from,to,rbT.root,foundNodesArray);

                        String formattedAnswer = helperUtility(foundNodesArray);
                        
                        if(foundNodesArray.size()==0)
                            fWriter.write("(0,0,0)"+"\n");
                        else
                            fWriter.write(formattedAnswer+"\n");
                        
                    }
                    else{
                        int rideNumberToSearch = Integer.parseInt(arr[1].substring(0,arr[1].length()-1));
                        Node searchedNode = rbT.searchSingleRide(rideNumberToSearch);
                        if(searchedNode!=rbT.TNULL){
                            String toWrite = "("+searchedNode.rideNumber+","+searchedNode.tripCost+","+searchedNode.tripDuration+")";
                            fWriter.write(toWrite+"\n");
                        }
                        else{
                            String toWrite = "(0,0,0)";    //Case when NO Ride is found.
                            fWriter.write(toWrite+"\n");
                        }
                    }
                    break;
                case "CancelRide":
                    //It retrives the ride number to cancel and deletes from both the data structures.
                    int cancelRideNumber = Integer.parseInt(arr[1].substring(0,arr[1].length()-1).trim());
                    System.out.println("Cancelling ride ID: "+ cancelRideNumber);

                    HeapNode heapNodeRefOfDeletedNode = rbT.deleteNodeFromRBTree(cancelRideNumber);
                    if(heapNodeRefOfDeletedNode!=null){ // valid node to delete
                        myHeap.deleteRandomNode(heapNodeRefOfDeletedNode);
                    }
                    break;
                case "UpdateTrip":
                    if(arr[1].contains(",")){
                        String[] subarr = arr[1].split(",");
                        int rideNumberToChange = Integer.parseInt(subarr[0]);
                        int newTripDuration =  Integer.parseInt(subarr[1].substring(0,subarr[1].length()-1));
                        
                        Node nodeToUpdate = rbT.searchSingleRide(rideNumberToChange);
 
                        if(nodeToUpdate!=rbT.TNULL){
                            /*
                                CASE 1: oldTripDuration < newTripDuration <= 2*(oldTripDuration):- 
                                        Updates:
                                        --TripCost = TripCost + 10;
                                        --oldTripDuration = newTripDuration;
                            */
                            if (newTripDuration > nodeToUpdate.tripDuration &&  newTripDuration <= (2*(nodeToUpdate.tripDuration))){
                                nodeToUpdate.tripDuration = newTripDuration;
                                nodeToUpdate.tripCost += 10;
                                nodeToUpdate.heapRef.tripCost += 10;
                                nodeToUpdate.heapRef.tripDuration = newTripDuration;
                                myHeap.fixUpdate();
                            }
                            /*
                                CASE 2: newTripDuration > 2*(oldTripDuration):- 
                                        Deletes the Ride from BOTH the data structures.
                            */
                            else if (newTripDuration > (2*(nodeToUpdate.tripDuration))){
                                HeapNode heapNodeRefOfUpdateNode = rbT.deleteNodeFromRBTree(rideNumberToChange);
                                if(heapNodeRefOfUpdateNode!=null){ // valid node to delete
                                myHeap.deleteRandomNode(heapNodeRefOfUpdateNode);
                                }
                            }
                            /*
                                CASE 3: newTripDuration <= oldTripDuration:- 
                                        Updates the oldTripDuration to NewTripDuration in BOTH the data structures.
                            */
                            else if(newTripDuration <= nodeToUpdate.tripDuration){
                                nodeToUpdate.tripDuration = newTripDuration;
                                nodeToUpdate.heapRef.tripDuration = newTripDuration;
                                myHeap.fixUpdate();

                            }
                        }
                        break;
                    }
                default:
                    System.out.println("something went wrong" + arr[0]);    //Default Case if nothing matches.
            }
        }   
        freader.close(); //Closing the file reader.
        fWriter.close(); //Closing the file writer.
    }
}