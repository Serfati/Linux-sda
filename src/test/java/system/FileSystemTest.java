package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class FileSystemTest {
    FileSystem fs;
    FileSystem fs2;
    Tree root;

    @Before
    public void setUp() {
        fs = new FileSystem(10);
        fs2 = new FileSystem(100);
        root = new Tree("root");
    }

    //Disk
    @Test
    public void diskFunctionReturnsNullEateriesWhenWhereEmpty() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "file"};
        try {
            fileSystem.file(mockPathNames, 3);
            String[][] disk = fileSystem.disk();
            IntStream.range(0, 3).forEach(i -> assertArrayEquals(mockPathNames, disk[i]));
            Assert.assertNull(disk[3]);
        } catch(BadFileNameException | OutOfSpaceException e) {
            Assert.fail();
        }
    }
    // end disk

    // --- rmdir --- //
    //Deleting a directory which is not empty
    @Test(expected = DirectoryNotEmptyException.class)
    public void testrmdir1() throws Exception {
        String[] dir = {"root", "dumb"};
        String[] file1 = {"root", "dumb", "file1"};
        fs.dir(dir);
        fs.file(file1, 1);
        fs.rmdir(dir);
    }

    @Test
    public void testrmdir2() {
        String[] dir = {"root", "dumb", "dumb2"};
        String[] file1 = {"root", "dumb", "file1"};
        try {
            fs.dir(dir);
            fs.file(file1, 1);
            fs.rmdir(dir);
            assertNull(fs.DirExists(dir));
            assertNotNull(fs.FileExists(file1));
        } catch(Exception e) {
            fail();
        }
    }

    // --- rmfile --- //
    @Test
    public void testrmfile() {
        try {
            String[] file1 = {"root", "dumb", "file1"};
            String[] file2 = {"root", "dumb", "file2"};
            fs.file(file1, 6);
            fs.file(file2, 3);
            fs.rmfile(file1);
            assertNull(fs.FileExists(file1)); //checking that file is not in directory
        } catch(Exception e) {
            fail();
        }
    }

    // --- dir --- //
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
            fs.dir(dir1);
        } catch(Exception e) {
            fail();
        }
        Tree res = fs.DirExists(dir1);
        assertEquals(res.name, "bc");
        assertEquals(res.parent.name, "aviv");
        assertEquals(res.parent.parent.name, "root");
    }

    // --- file --- //
    @Test
    public void fileWithLargeK() {
        try {
            int space = FileSystem.fileStorage.countFreeSpace();
            space = space * space;
            String[] file = {"root", "file"};
            fs2.file(file, space);
            fail();
        } catch(Exception e) {
        }
    }

    @Test
    public void fileWithFileAndDirTheSameName() {
        try {
            int space = FileSystem.fileStorage.countFreeSpace();
            int original = space / 4;
            String[] file = {"root", "file"};
            fs2.dir(file);
            fs2.file(file, original);
            fail();
        } catch(Exception e) {
        }
    }

    @Test
    public void fileCantAdd() {
        try {
            fs2.file(new String[]{null}, 10);
        } catch(BadFileNameException | OutOfSpaceException e) {
            e.printStackTrace();
        }
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

    @Test
    public void fileWithNoramlK() {
        try {
            int space = FileSystem.fileStorage.countFreeSpace();
            int original = space / 2;
            String[] file = {"root", "file"};
            fs2.file(file, original);
            int result = FileSystem.fileStorage.countFreeSpace();
            assertEquals(space, original+result);
            Leaf leaf = fs2.FileExists(file);
            assertEquals(2, leaf.depth);
            assertEquals(original, leaf.allocations.length);
        } catch(Exception e) {
            fail();
        }
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
        assertEquals(FileSystem.fileStorage.countFreeSpace(), 94);
        try {
            Leaf child = fs.FileExists(path);
            assertEquals(child.name, "av");
            assertEquals(child.parent.parent.name, "root");
        } catch(Exception e) {
            fail();
        }
    }

    // --- ls --- //
    @Test
    public void tesdtlsdir2() {
        Assert.assertNull(fs.lsdir(new String[]{"nonExistingDir"}));
        String[] file1 = {"root", "what", "file1"};
        String[] file2 = {"root", "what", "file2"};
        String[] expected = {"file1", "file2"};
        try {
            fs.dir(new String[]{"root", "what"});
            fs.file(file1, 3);
            fs.file(file2, 3);
            assertArrayEquals(fs.lsdir(new String[]{"root", "what"}), expected);
        } catch(Exception e) {
            fail();
        }
    }
}