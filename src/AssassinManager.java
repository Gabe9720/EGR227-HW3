import java.util.List;

public class AssassinManager {
    //fields:
    //aliveFront- the aliveFront of the kill ring
    //graveyardFront- aliveFront of the list of the assassins who were eliminated
    private AssassinNode aliveFront;
    private AssassinNode graveyardFront;

    //pre: construct an AssassinManager object from a given list of strings
    //post: creates a linkedlist of assassin nodes from names in the list
    //throws exception if the list is null or empty
    public AssassinManager(List<String> names){
        if(names == null || names.isEmpty())
            throw new IllegalArgumentException("List may not be null or empty.");

        //initialize graveyard list lead by graveyardFront
        graveyardFront = null;

        //initialize alive list reading backward
        aliveFront = null;
        for (int i = names.size() - 1 ; i >= 0 ; i--) {
            aliveFront = new AssassinNode(names.get(i), aliveFront);
        }
    }

    //pre: print the kill ring
    //post: prints kill ring; if only one person is in the kill ring, that person wins
    public void printKillRing(){
        if(isGameOver()){
            System.out.println("    " + aliveFront.name + " won the game!");
        }
        else{
            AssassinNode current = aliveFront;
            while(current != null){
                System.out.println(current);
                if(current.next == null) {
                    System.out.println(aliveFront.name);
                }
                current = current.next;
            }
        }
    }

    //pre: print out graveyard
    //post: prints graveyard along with their killer; if graveyard is empty nothing happens
    public void printGraveyard(){
        AssassinNode current = graveyardFront;
        while(current != null){
            System.out.println("    " + current.name + " was killed by " + current.killer);
            current = current.next;
        }
    }

    //pre: determine if game is over
    //post: returns true if there is only one person in the kill ring; otherwise false
    public boolean isGameOver(){
        return (aliveFront.next == null);
    }

    //helper method
    //param: name being searched for and give the starting node
    //to indicate to search the graveyard or kill ring
    //pre: determine if one of the linkedlists contains the desired name
    //post: returns true if name is in the linkedlist; otherwise false
    private boolean containsHelper(String name, AssassinNode start){
        AssassinNode current = start;
        while(current != null){
            if(current.name.equalsIgnoreCase(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    //pre: check if a name is in the kill ring
    //post: returns true if name is found; otherwise false
    public boolean killRingContains(String name){
        return containsHelper(name, aliveFront);
    }

    //pre: check if a name is in the graveyard
    //post: returns true if name is found; otherwise false
    public boolean graveyardContains(String name){
        return containsHelper(name, graveyardFront);
    }

    //pre: check if there is a winner
    //post: returns the name in the aliveFront of the linked list if the game is over; else false
    public String winner(){
        if(!isGameOver())
            return null;
        return aliveFront.name;
    }

    //param: name of person to be killed
    //pre: kill the name of the person passed
    //post: removes name of the person from the kill list to the graveyard;
    //exception is thrown if the game is over or if the kill ring does not contain the given name
    public void kill(String name){
        if(isGameOver()){
            throw new IllegalStateException("The game is over.");
        }
        else if(!killRingContains(name)){
            throw new IllegalArgumentException("This name is not in the kill ring.");
        }
        else{
            AssassinNode current = aliveFront;
            //if name is in aliveFront
            if(current.name.equalsIgnoreCase(name)){
                aliveFront = aliveFront.next;
                current.next = graveyardFront;
                graveyardFront = current;
                current = aliveFront;
                while(current.next != null){
                    current = current.next;
                }
                graveyardFront.killer = current.name;
            }
            //if name is in the middle
            else {
                while (current.next != null) {
                    if (current.next.name.equalsIgnoreCase(name)) {
                        AssassinNode temp = current.next.next;
                        current.next.next = graveyardFront;
                        graveyardFront = current.next;
                        graveyardFront.killer = current.name;
                        current.next = temp;
                        break;
                    }
                    current = current.next;
                }
            }
        }
    }
    //////// DO NOT MODIFY AssassinNode.  You will lose points if you do. ////////
    /**
     * Each AssassinNode object represents a single node in a linked list
     * for a game of Assassin.
     */
    private static class AssassinNode {
        public final String name;  // this person's name
        public String killer;      // name of who killed this person (null if alive)
        public AssassinNode next;  // next node in the list (null if none)

        /**
         * Constructs a new node to store the given name and no next node.
         */
        public AssassinNode(String name) {
            this(name, null);
        }

        /**
         * Constructs a new node to store the given name and a reference
         * to the given next node.
         */
        public AssassinNode(String name, AssassinNode next) {
            this.name = name;
            this.killer = null;
            this.next = next;
        }

        @Override
        public String toString() {
            String victim = next != null ? next.name : "";
            return "    " + name + " is stalking " + victim;
        }

    }
}