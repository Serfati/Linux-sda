package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryNotEmptyException;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class FileSystemTest {
    FileSystem fs;
    Tree root;

    @Before
    public void setUp() {
        fs = new FileSystem(100);
        root = new Tree("root");
    }

    //Disk
    @Test
    public void diskNull() {
        try {
            fs.file(new String[]{"root", "file"}, 4);
            IntStream.range(0, 4).forEach(i -> assertArrayEquals(new String[]{"root", "file"}, fs.disk()[i]));
            Assert.assertNull(fs.disk()[4]);
        } catch(BadFileNameException | OutOfSpaceException e) {
            fail();
        }
    }
    // end disk

    // --- rmdir --- //
    @Test(expected = DirectoryNotEmptyException.class)
    public void rmdirOK() throws Exception {
        fs.dir(new String[]{"root", "bgu"});
        fs.file(new String[]{"root", "bgu", "QA"}, 5);
        fs.rmdir(new String[]{"root", "bgu"});
    }

    @Test
    public void rmdirNull() {
        String[] file = {"root", "dor", "qa"};
        try {
            fs.dir(new String[]{"root", "bgu", "QA"});
            fs.file(file, 5);
            fs.rmdir(new String[]{"root", "bgu", "QA"});
            assertNotNull(fs.FileExists(file));
            assertNull(fs.DirExists(new String[]{"root", "bgu", "QA"}));
        } catch(Exception e) {
            fail();
        }
    }

    // --- rmfile --- //
    @Test
    public void rmfileNull() {
        try {
            fs.file(new String[]{"root", "bgu", "QA"}, 6);
            fs.rmfile(new String[]{"root", "bgu", "QA"});
            assertNull(fs.FileExists(new String[]{"root", "bgu", "QA"}));
        } catch(Exception e) {
            fail();
        }
    }

    // --- dir --- //
    //adds dir which already exists as a leaf
    @Test(expected = BadFileNameException.class)
    public void dirFail() throws Exception {
        fs.file(new String[]{"root", "exists"}, 10);
        fs.dir(new String[]{"root", "exists"});
    }

    @Test
    public void dirMain() {
        try {
            fs.dir(new String[]{"root", "serfati", "dor"});
            Tree res = fs.DirExists(new String[]{"root", "serfati", "dor"});
            assertEquals(res.parent.parent.name, "root");
            assertEquals(res.parent.name, "serfati");
            assertEquals(res.name, "dor");
        } catch(Exception e) {
            fail();
        }
    }

    // --- file --- //
    @Test
    public void fileMain() throws OutOfSpaceException, BadFileNameException {
        fs.file(new String[]{"root", "bgu"}, 10);
        fs.file(new String[]{"root", "bgu"}, 10);
        assertEquals(FileSystem.fileStorage.countFreeSpace(), 90);
        Leaf child = fs.FileExists(new String[]{"root", "bgu"});
        assertEquals(child.name, "bgu");
        assertEquals(child.parent.parent.name, "root");
    }

    @Test(expected = BadFileNameException.class)
    public void fileExp() throws Exception {
        fs.file(new String[]{"noRoot", "se!"}, 10);
    }

    @Test
    public void fileAdd() {
        try {
            fs.file(new String[]{"root", "fil!e"}, FileSystem.fileStorage.countFreeSpace() * FileSystem.fileStorage.countFreeSpace());
            fail();
        } catch(BadFileNameException | OutOfSpaceException e) {
        }
    }

    @Test
    public void fileCantAdd() {
        try {
            FileSystem fileSystem2 = new FileSystem(50) {
                @Override
                public Leaf FileExists(String[] name) {
                    try {
                        Leaf leaf = new Leaf("file1", 0);
                        FileSystem.fileStorage = new Space(26);
                        return leaf;
                    } catch(Exception e) {
                    }
                    return null;
                }
            };
            try {
                fileSystem2.file(new String[]{"root", "BGU", "IR"}, 25);
                fileSystem2.file(new String[]{"root", "BGU", "QA"}, 26);
                fail();
            } catch(NullPointerException e) {
            }

            String[] file = {"root", "file"};
            fs.dir(file);
            fs.file(file, FileSystem.fileStorage.countFreeSpace() / 4);
        } catch(BadFileNameException | OutOfSpaceException e) {
        }
    }

    @Test(expected = BadFileNameException.class)
    public void fileDirFail() throws Exception {
        fs.dir(new String[]{"root", "BGU"});
        fs.file(new String[]{"root", "BGU"}, FileSystem.fileStorage.countFreeSpace() / 4);
    }

    // --- ls --- //
    @Test
    public void lsMain() {
        assertNull(fs.lsdir(new String[]{"root", "unknown"}));
        try {
            fs.dir(new String[]{"root", "BGU"});
            fs.file(new String[]{"root", "IR"}, 3);
            fs.file(new String[]{"root", "QA"}, 3);
            fs.file(new String[]{"root", "ADSS"}, 3);
            fs.file(new String[]{"root", "AI"}, 3);
            Assert.assertArrayEquals(fs.lsdir(new String[]{"root"}), new String[]{"ADSS", "AI", "BGU", "IR", "QA"});
            fs.rmfile(new String[]{"root", "AI"});
            Assert.assertArrayEquals(fs.lsdir(new String[]{"root"}), new String[]{"ADSS", "BGU", "IR", "QA"});
        } catch(Exception e) {
            fail();
        }
    }

    // --------------- disk ---------------//
    @Test
    public void diskMain() {
        String[] bgu = {"root", "BGU"};
        String[] ir = {"root", "BGU", "IR"};
        String[] qa = {"root", "BGU", "QA"};
        String[] ai = {"root", "BGU", "AI"};
        String[] sise = {"root", "BGU", "SISE"};
        String[] adss = {"root", "BGU", "SISE", "ADSS"};
        String[] dor = {"root", "dor"};

        try {
            fs = new FileSystem(10);
            fs.dir(bgu);
            fs.dir(new String[]{"root", "music"});
            fs.file(ir, 2);
            fs.file(qa, 1);
            fs.dir(new String[]{"root", "pics"});
            fs.file(ai, 3);
            fs.dir(sise);
            fs.file(adss, 2);
            fs.dir(new String[]{"root", "docs"});
            fs.file(dor, 1);
            String[][] out1 = {ir, ir, qa, ai, ai, ai, adss, adss, dor, null};
            assertArrayEquals(fs.disk(), out1);

            fs.rmfile(adss);
            assertArrayEquals(fs.disk(), new String[][]{ir, ir, qa, ai, ai, ai, null, null, dor, null});

            String[] avihai = {"root", "avihai"};
            String[][] out3 = {ir, ir, qa, ai, ai, ai, avihai, avihai, dor, avihai};
            fs.file(avihai, 3);
            assertArrayEquals(fs.disk(), out3);

            fs.rmdir(sise);
            String[][] out4 = {ir, ir, qa, ai, ai, ai, avihai, avihai, dor, avihai};
            assertArrayEquals(fs.disk(), out4);
            assertArrayEquals(fs.lsdir(new String[]{"root", "BGU", "SISE"}), null);

            fs.rmfile(dor);
            fs.rmfile(avihai);
            String[][] out5 = {ir, ir, qa, ai, ai, ai, null, null, null, null};
            assertArrayEquals(fs.disk(), out5);
            fs.rmfile(ir);
            fs.rmfile(qa);
            fs.rmfile(ai);
        } catch(Exception e) {
            fail();
        }

    }
}