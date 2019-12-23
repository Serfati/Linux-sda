package system;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;

import static org.junit.Assert.*;

public class FileSystemTest {
    FileSystem fs;
    Tree root;

    @Before
    public void setUp() {
        fs = new FileSystem(10);
        root = new Tree("root");
    }


    //Path that does not exists :
    @Test
    public void testPathExists() {
        String[] path = {"root", "K"};
        assertNull(fs.FileExists(path));
    }

    //Path with file and not directory :
    @Test
    public void testDirExists() {
        String[] path = {"root", "K"};
        try {
            fs.file(path, 4);
        } catch(Exception e) {
            fail();
        }
        assertNull(fs.DirExists(path));
    }

    //adds a dir without root as 0 index
    @Test(expected = BadFileNameException.class)
    public void testdir1() throws BadFileNameException {
        String[] falseDir = {"NotRoot"};
        fs.dir(falseDir);
    }


    //adds dir which already exists as a leaf
    @Test(expected = BadFileNameException.class)
    public void testdir2() throws Exception {
        String[] dirLeaf = {"root", "leaf"};
        fs.file(dirLeaf, 4);
        fs.dir(dirLeaf);
    }


    //Checks main functionality :
    @Test
    public void testdir4() {

        String[] dir1 = {"root", "aviv", "bc"};
        try {
            fs.dir(dir1); //Adds the ddir
        } catch(Exception e) {
            fail();
        }
        Tree res = fs.DirExists(dir1);
        assertEquals(res.name, "bc");
        assertEquals(res.parent.name, "aviv");
        assertEquals(res.parent.parent.name, "root");
    }


    //Path without root at index 0
    @Test(expected = BadFileNameException.class)
    public void testFile1() throws Exception {
        String[] path = {"NotRoot", "av"};
        fs.file(path, 2);
    }


    //File too big :
    @Test(expected = OutOfSpaceException.class)
    public void testFile2() throws Exception {
        String[] path = {"root", "av"};
        fs.file(path, 11);
    }

    //Simply adding a file :
    @Test
    public void testFile3() {
        String[] path = {"root", "av"};
        try {
            fs.file(path, 6);
        } catch(Exception e) {
            fail();
        }
        //checking that space was taken :
        assertEquals(FileSystem.fileStorage.countFreeSpace(), 4);
        //checking that path is correct :
        try {
            Leaf child = fs.FileExists(path);
            assertEquals(child.name, "av");
            assertEquals(child.parent.parent.name, "root");
        } catch(Exception e) {
            fail();
        }
    }

    //adding a file with the name of a dir :
    @Test(expected = BadFileNameException.class)
    public void testFile4() throws Exception {
        String[] path = {"root", "root2", "root3"};
        fs.dir(path);
        fs.file(path, 1);
    }


    //File too big but can delete older version of the file:
    @Test
    public void testFile5() {
        String[] path = {"root", "av"};
        try {
            fs.file(path, 6);
        } catch(Exception e) {
            fail();
        }
        try {
            fs.file(path, 10);
            assertEquals(FileSystem.fileStorage.countFreeSpace(), 0);
            Leaf child = fs.FileExists(path);
            assertEquals(child.name, "av");
            assertEquals(child.parent.parent.name, "root");
        } catch(Exception e) {
            fail();
        }
    }


    //non existent dir
    @Test
    public void testlsdir1() {
        String[] whatwhat = {"root", "WHATHWATHWAHTWHATWHAT"};
        assertNull(fs.lsdir(whatwhat));
    }

    //Testing main functionality :
    @Test
    public void tesdtlsdir2() {
        String[] path = {"root", "what"};
        String[] file1 = {"root", "what", "file1"};
        String[] file2 = {"root", "what", "file2"};
        String[] expected = {"file1", "file2"};
        try {
            fs.dir(path);
            fs.file(file1, 3);
            fs.file(file2, 3);
            assertArrayEquals(fs.lsdir(path), expected);
        } catch(Exception e) {
            fail();
        }
    }


    @Test
    public void testdisk1() {
        String[] file1 = {"root", "aviv", "file1"};
        String[] file2 = {"root", "aviv", "file2"};
        String[] file3 = {"root", "aviv", "file3"};
        String[] dir = {"root", "aviv", "dir"};
        String[] file4 = {"root", "aviv", "dir", "file4"};
        String[] file5 = {"root", "file5"};
        String[] file6 = {"root", "file6"};
        String[][] expected1 = {file1, file1, file2, file3, file3, file3, file4, file4, file5, null};
        String[][] expected2 = {file1, file1, file2, file3, file3, file3, null, null, file5, null};
        String[][] expected3 = {file1, file1, file2, file3, file3, file3, file6, file6, file5, file6};
        try {
            fs.file(file1, 2);
            fs.file(file2, 1);
            fs.file(file3, 3);
            fs.dir(dir);
            fs.file(file4, 2);
            fs.file(file5, 1);
            assertArrayEquals(fs.disk(), expected1);

            fs.rmfile(file4);
            assertArrayEquals(fs.disk(), expected2);

            fs.file(file6, 3);
            assertArrayEquals(fs.disk(), expected3);

            fs.rmfile(file1);
            fs.rmfile(file2);
            fs.rmfile(file3);
            fs.rmfile(file4);
            fs.rmfile(file5);
            fs.rmfile(file6);
            assertArrayEquals(fs.disk(), new String[10]);
        } catch(Exception e) {
            fail();
        }

    }

    //Checks empty disk :
    @Test
    public void testdisk2() {
        String[] expected = new String[10];
        assertArrayEquals(fs.disk(), expected);
    }


    //Typical test case of removing a file :
    @Test
    public void testrmfile() {
        try {
            String[] file1 = {"root", "dumb", "file1"};
            String[] file2 = {"root", "dumb", "file2"};
            fs.file(file1, 6); //Adding the file
            fs.file(file2, 3); //Adding the file
            fs.rmfile(file1);
            assertNull(fs.FileExists(file1)); //checking that file is not in directory
            assertEquals(FileSystem.fileStorage.countFreeSpace(), 7); //checking the space was freed
        } catch(Exception e) {
            fail();
        }
    }


    //Deleting a directory which is not empty
    @Test(expected = DirectoryNotEmptyException.class)
    public void testrmdir1() throws Exception {
        String[] dir = {"root", "dumb"};
        String[] file1 = {"root", "dumb", "file1"};
        fs.dir(dir);
        fs.file(file1, 1);
        fs.rmdir(dir);
    }

    //Typical test case of removing a dir :
    @Test
    public void testrmdir2() {
        String[] dir = {"root", "dumb", "dumb2"};
        String[] file1 = {"root", "dumb", "file1"};
        try {
            fs.dir(dir);
            fs.file(file1, 1);
            fs.rmdir(dir);
            assertNull(fs.DirExists(dir)); //Checking that dir was deleted
            assertNotNull(fs.FileExists(file1));
        } catch(Exception e) {
            fail();
        }
    }

    ///deletin directory which exists :
    @Test
    public void testrmdir3() {
        String[] dir = {"root", "dumb", "dumb2"};
        try {
            fs.rmdir(dir);
        } catch(Exception e) {
            fail();
        }
    }

    @Test
    public void fileWithCantAdd() {
        try {
            FileSystem fileSystem2 = new FileSystem(50) {
                @Override
                public Leaf FileExists(String[] name) {
                    Leaf leaf = null;
                    try {
                        leaf = new Leaf("file1", 0);
                        FileSystem.fileStorage = new Space(26);
                    } catch(Exception e) {
                    }
                    return leaf;
                }
            };
            String[] file1 = {"root", "file1"};
            fileSystem2.file(file1, 25);
            String[] file2 = {"root", "file2"};
            fileSystem2.file(file2, 26);
            fail();
        } catch(NullPointerException e) {

        } catch(Exception e) {
            fail();
        }
    }
}