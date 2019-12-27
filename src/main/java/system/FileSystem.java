package system;

import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;

/**
 * @author Iain Lane, <a href="mailto:ial05u@cs.nott.ac.uk">ial05u@cs.nott.ac.uk</a>
 * @version 0.6
 */

public class FileSystem {

    /**
     * Stores file->block allocations
     */
    public static Space fileStorage;
    private Tree fileSystemTree;

    /**
     * Ctor - Initialise filesystem with empty root directory and \c m KB of space
     *
     * @param m Amount, in KB, of disk space to allocate
     */
    public FileSystem(int m) {

        fileSystemTree = new Tree("root");

        fileStorage = new Space(m);

    }

    /**
     * Create an empty directory, with path provided in \c name.
     *
     * @param name String array containing path to directory to be created, as in \c file().
     */
    public void dir(String[] name) throws BadFileNameException {

        Tree workingTree = fileSystemTree;

        if (name[0] != "root" || (FileExists(name) != null)) {

            throw new BadFileNameException();

        }

        if (DirExists(name) != null) {

            return;

        }

        //loop all the way, creating as we go down if necessary
        for(int i = 0; i < name.length; i++) {

            workingTree = workingTree.GetChildByName(name[i]);

        }

    }

    /**
     * List allocation of blocks on disk.
     *
     * @return A 2D String array of block/file allocations. Each index corresponds to one disk block and the entry is either null
     * if the blocks is free or an array of strings which is the path to the file occupying that block.
     */
    public String[][] disk() {

        Leaf[] alloc = FileSystem.fileStorage.getAlloc();
        String[][] disk = new String[alloc.length][];
        int i = 0;

        for(Leaf elem : alloc) {

            if (elem == null) {

                i++;
                continue;

            } else {

                disk[i++] = elem.getPath();

            }

        }

        return disk;

    }

    /**
     * Create a \c k KB file, path provided in name.
     *
     * @param name String array, each element of which is an element in the path to file.
     *             Must start with root. Any nonexistent directories along the path will be created.
     * @param k    Size of file to create, in KB.
     * @throws BadFileNameException First directory is not root.
     * @throws OutOfSpaceException  Adding child failed; not enough free space.
     */
    public void file(String[] name, int k) throws BadFileNameException, OutOfSpaceException {

        Tree workingTree = fileSystemTree;
        String fileName = name[name.length-1];

        if (name[0] != "root") {

            throw new BadFileNameException();

        }


        if (k > FileSystem.fileStorage.countFreeSpace()) { //not enough space free

            Leaf file = FileExists(name);

            if (file == null) {

                throw new OutOfSpaceException();

            } else if (k <= (FileSystem.fileStorage.countFreeSpace()-file.allocations.length)) { //if there will be enough space free after deleting the old file, do it

                rmfile(name);

            }

        }

        //loop until level containing file
        for(int i = 0; i < name.length-1; i++) {

            workingTree = workingTree.GetChildByName(name[i]);

        }

        //will now be at same level as file, contained in workingTree
        if (workingTree.children.containsKey(fileName)) { //file exists, remove (reached this point, so file can fit)

            if (workingTree.children.get(fileName).getClass().getName() == "system.Tree") { //name of existing directory

                throw new BadFileNameException();

            }

            //enough space free, remove old file
            rmfile(name);

        }

        Leaf newLeaf = new Leaf(fileName, k);
        newLeaf.parent = workingTree;
        newLeaf.depth = newLeaf.parent.depth+1;
        workingTree.children.put(fileName, newLeaf);
    }

    /**
     * List files and subdirectories contained in name.
     *
     * @param name String array containing path to directory to list, as in \c file().
     * @return A String array containing the filename (only) of all files in the directory.
     */
    public String[] lsdir(String[] name) {

        Tree file = DirExists(name);
        String[] fileList;

        if (file == null) {

            return null;

        }

        fileList = new String[file.children.size()];
        fileList = file.children.keySet().toArray(fileList);

        //sort array - not essential, but nice!
        Arrays.sort(fileList);

        return fileList;

    }

    /**
     * Remove a file.
     *
     * @param name String array containing path to file to be removed, as in \c file().
     */
    public void rmfile(String[] name) {

        Leaf file = FileExists(name);

        if (file == null) { //file doesn't exist

            return;

        }

        FileSystem.fileStorage.Dealloc(file);

    }

    /**
     * Remove an empty directory.
     *
     * @param name String array containing path to directory to be removed, as in \c file().
     * @throws DirectoryNotEmptyException The directory is not empty.
     */
    public void rmdir(String[] name) throws DirectoryNotEmptyException {

        Tree dir = DirExists(name);

        if (dir == null) {

            return;

        }

        if (dir.children.size() > 0) {

            throw new DirectoryNotEmptyException("The directory is not empty");

        }

        dir.parent.children.remove(dir.name);

    }


    private Node PathExists(String[] name) {

        Tree workingTree = fileSystemTree;

        for(int i = 0; i < name.length-1; i++) {

            if (!workingTree.children.containsKey(name[i])) {

                return null;

            }

            workingTree = (Tree) workingTree.children.get(name[i]);

        }

        return workingTree.children.get(name[name.length-1]);

    }

    /**
     * Checks whether the specified file exists
     *
     * @param name Path to the file
     * @return File if exists, \c null otherwise
     */
    public Leaf FileExists(String[] name) {

        Node found = PathExists(name);

        if (found == null || found.getClass().getName() == "system.Node") {

            return null;

        }

        return (Leaf) found;

    }

    /**
     * Checks whether the specified directory exists
     *
     * @param name Path to directory
     * @return Directory if exists, null otherwise
     */
    public Tree DirExists(String[] name) {

        Node found = PathExists(name);

        if (found == null || found.getClass().getName() == "system.Leaf") {

            return null;

        }

        return (Tree) found;

    }
}

