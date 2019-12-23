package system;

public abstract class Node {

    /**
     * Every node must have a parent. For \c root this will be null
     */
    public Tree parent;
    /**
     * Every node must have a name
     */
    String name;
    /**
     * Store the depth
     */
    int depth = 0;

    /**
     * Get the path to the current node
     *
     * @return Path to the current node
     */
    public String[] getPath() {

        String[] path = new String[this.depth];
        Node workingNode = this;

        for(int i = this.depth-1; i >= 0; i--) {

            path[i] = workingNode.name;
            workingNode = workingNode.parent;

        }

        return path;

    }

}
