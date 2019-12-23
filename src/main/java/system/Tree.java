package system;

import java.io.Serializable;
import java.util.HashMap;

/**
 * system.Tree data structure - nodes can have arbitrarily many children.
 *
 * @author iain
 */
public class Tree extends Node implements Serializable {

    /**
     * The children of the current node
     */
    public HashMap<String, Node> children = new HashMap<String, Node>();


    /**
     * Ctor - create tree.
     *
     * @param name The name of the root element of this tree.
     */
    public Tree(String name) {

        this.name = name;

    }

    /**
     * Get a child from a \c system.Node, or create it if nonexistant.
     *
     * @param name Name of child to search for.
     * @return system.Tree found (or created).
     */
    public Tree GetChildByName(String name) {

        if (this.children.containsKey(name)) {

            return (Tree) this.children.get(name);

        }

        //not found - create

        Tree newTree = new Tree(name);
        newTree.parent = this;
        newTree.depth = newTree.parent.depth+1;

        this.children.put(name, newTree);

        return newTree;

    }

}