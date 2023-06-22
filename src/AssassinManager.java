import java.util.List;

public class AssassinManager {
    //fields:
//aliveFront - the front of the kill ring
//graveyardFront - the front of the list of assassins who were eliminated
    private AssassinNode aliveFront;
    private AssassinNode graveyardFront;    //fields:
    //aliveFront- the aliveFront of the kill ring
    //graveyardFront- aliveFront of the list of the assassins who were eliminated

    //pre: Construct an AssassinManager object from a given list of strings
//post: Creates a linked list of assassin nodes from names in the list
//throws an exception if the list is null or empty
    public AssassinManager(List<String> names) {
        if (names == null || names.isEmpty())
            throw new IllegalArgumentException("List may not be null or empty.");

        //initialize graveyard list led by graveyardFront
        graveyardFront = null;

        //initialize alive list reading backward
        aliveFront = null;
        for (int i = names.size() - 1; i >= 0; i--) {
            aliveFront = new AssassinNode(names.get(i), aliveFront);
        }
    }

    //pre: Print the kill ring
//post: Prints the kill ring; if only one person is in the kill ring, that person wins
    public void printKillRing() {
        if (isGameOver()) {
            System.out.println("    " + aliveFront.name + " won the game!");
        } else {
            AssassinNode current = aliveFront;
            while (current != null) {
                System.out.println(current);
                if (current.next == null) {
                    System.out.println(aliveFront.name);
                }
                current = current.next;
            }
        }
    }

    //pre: Print out the graveyard
//post: Prints the graveyard along with their killer; if the graveyard is empty, nothing happens
    public void printGraveyard() {
        AssassinNode current = graveyardFront;
        while (current != null) {
            System.out.println("    " + current.name + " was killed by " + current.killer);
            current = current.next;
        }
    }

    //pre: Determine if the game is over
//post: Returns true if there is only one person in the kill ring; otherwise, returns false
    public boolean isGameOver() {
        return (aliveFront.next == null);
    }

    //helper method
//param: The name being searched for and the starting node
//to indicate whether to search the graveyard or the kill ring
//pre: Determine if one of the linked lists contains the desired name
//post: Returns true if the name is in the linked list; otherwise, returns false
    private boolean containsHelper(String name, AssassinNode start) {
        AssassinNode current = start;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    //pre: Check if a name is in the kill ring
//post: Returns true if the name is found; otherwise, returns false
    public boolean killRingContains(String name) {
        return containsHelper(name, aliveFront);
    }

    //pre: Check if a name is in the graveyard
//post: Returns true if the name is found; otherwise, returns false
    public boolean graveyardContains(String name) {
        return containsHelper(name, graveyardFront);
    }

    //pre: Check if there is a winner
//post: Returns the name in the front of the alive list if the game is over; otherwise, returns null
    public String winner() {
        if (!isGameOver())
            return null;
        return aliveFront.name;
    }

    //param: The name of the person to be killed
//pre: Kill the person with the given name
//post: Removes the name of the person from the kill list to the graveyard;
//throws an exception if the game is over or if the kill ring does not contain the given name
    public void kill(String name) {
        if (isGameOver()) {
            throw new IllegalStateException("The game is over.");
        } else if (!killRingContains(name)) {
            throw new IllegalArgumentException("This name is not in the kill ring.");
        } else {
            AssassinNode current = aliveFront;
            //if the name is in aliveFront
            if (current.name.equalsIgnoreCase(name)) {
                aliveFront = aliveFront.next;
                current.next = graveyardFront;
                graveyardFront = current;
                current = aliveFront;
                while (current.next != null) {
                    current = current.next;
                }
                graveyardFront.killer = current.name;
            }
            //if the name is in the middle
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
