package system;

/**
 * system.Leaf - cannot have children.
 *
 * @author iain
 */
public class Leaf extends Node {

    /**
     * Size (in KB) of system.Leaf
     */
    public int size;
    /**
     * Array of blocks containing system.Leaf data
     */
    public int[] allocations;

    /**
     * Ctor - create leaf.
     *
     * @param name Name of the leaf.
     * @param size Size, in KB, of the leaf.
     * @throws OutOfSpaceException Allocating space failed.
     */
    public Leaf(String name, int size) throws OutOfSpaceException {

        this.name = name;

        allocateSpace(size);

    }

    private void allocateSpace(int size) throws OutOfSpaceException {

        FileSystem.fileStorage.Alloc(size, this);

    }

}