package system;

import java.util.LinkedList;

public class Space {

    private Leaf[] blocks;
    private LinkedList<Integer> freeBlocks;

    /**
     * Ctor - create \c size blank filesystem blocks.
     *
     * @param size
     */
    public Space(int size) {

        blocks = new Leaf[size];
        //sacrifice space for speed
        freeBlocks = new LinkedList<Integer>();

        //O(size) to initialize queue
        for(int i = 0; i < size; i++) {

            freeBlocks.add(i);

        }

    }

    /**
     * Allocate \c size blocks to store \c file.
     *
     * @param size Number of blocks, in KB, required by \c file.
     * @param file File to store.
     * @throws OutOfSpaceException
     */
    public void Alloc(int size, Leaf file) throws OutOfSpaceException {

        file.allocations = new int[size];

        //we reached this point, therefore there is enough free space
        for(int i = 0; i < size; i++) {

            file.allocations[i] = freeBlocks.poll();
            blocks[file.allocations[i]] = file;

        }

    }

    /**
     * Free any space occupied by \c file.
     *
     * @param file File to deallocate.
     */
    public void Dealloc(Leaf file) {

        for(int i = 0; i < file.allocations.length; i++) {

            blocks[file.allocations[i]] = null;
            //record new free block
            freeBlocks.add(file.allocations[i]);

        }

        file.parent.children.remove(file.name);

    }

    /**
     * Count the amount of free space.
     *
     * @return the amount of kb blocks free
     */
    public int countFreeSpace() {

        return freeBlocks.size();

    }

    /**
     * Get the file allocations
     *
     * @return an array (of leaves) of allocations
     */
    public Leaf[] getAlloc() {

        return blocks;

    }
}
