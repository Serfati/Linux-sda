package system;

public class SimpleTest {

    public static void main(String[] args) {

        FileSystem fs = new FileSystem(10);
// this creates a file system with 10 disk blocks.

        String[] rootname = {"root"};
        String[] namef1 = {"root", "file1"};
        String[] named1 = {"root", "dir1"};

// Let us try to create a directory

        System.out.println("*****************************************");

        System.out.println("1. Creating a directory root/dir1");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        try {
            fs.dir(named1);
        } catch(Exception e) {
            System.out.println(e.toString());
        }


        System.out.println("*****************************************");

        System.out.println("2. Calling lsdir(root)");
        System.out.println("Expected outcome: dir1");
        System.out.print("What we get in fact: ");
        String[] dir = fs.lsdir(rootname);
        for(int i = 0; i < dir.length; i++) {
            System.out.print(dir[i]+" ");
        }
        System.out.println();

        System.out.println("*****************************************");

        String[] named2 = {"root", "dir1", "dir2"};
        System.out.println("3. Creating a directory root/dir1/dir2");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        try {
            fs.dir(named2);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("*****************************************");

        System.out.println("4. Calling lsdir(dir1)");
        System.out.println("Expected outcome: dir2");
        System.out.print("What we get in fact: ");
        dir = fs.lsdir(named1);
        for(int i = 0; i < dir.length; i++) {
            System.out.print(dir[i]+" ");
        }
        System.out.println();

        System.out.println("*****************************************");

        // can we create two directories in one go?

        String[] named3 = {"root", "dir3"};
        String[] named4 = {"root", "dir3", "dir4"};

        System.out.println("5. Creating a directory root/dir3/dir4");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        try {
            fs.dir(named4);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("*****************************************");

        System.out.println("6. Calling lsdir(dir3)");
        System.out.println("Expected outcome: dir4");
        System.out.print("What we get in fact: ");
        dir = fs.lsdir(named3);
        for(int i = 0; i < dir.length; i++) {
            System.out.print(dir[i]+" ");
        }
        System.out.println();

        System.out.println("*****************************************");

        System.out.println("7. Calling lsdir(root)");
        System.out.println("Expected outcome: dir1 dir3");
        System.out.print("What we get in fact: ");
        dir = fs.lsdir(rootname);
        for(int i = 0; i < dir.length; i++) {
            System.out.print(dir[i]+" ");
        }
        System.out.println();

        System.out.println("*****************************************");


        // removing directories

        System.out.println("8. Calling rmdir(dir3)");
        System.out.println("Expected outcome: DirectoryNotEmptyException");
        System.out.println("What we get in fact:");
        try {
            fs.rmdir(named3);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("*****************************************");

        System.out.println("9. Calling rmdir(dir4)");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        try {
            fs.rmdir(named4);
        } catch(Exception e) {
            System.out.println(e.toString());
        }


        System.out.println("*****************************************");

        System.out.println("10. Calling lsdir(dir3)");
        System.out.println("Expected outcome: nothing");
        System.out.print("What we get in fact: ");
        dir = fs.lsdir(named3);
        for(int i = 0; i < dir.length; i++) {
            System.out.println(dir[i]+" ");
        }
        System.out.println();

        System.out.println("*****************************************");

// Let us try to create some files

        System.out.println("11. Creating a file root/file1");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        try {
            fs.file(namef1, 5);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("*****************************************");

        System.out.println("12. Calling lsdir(root)");
        System.out.println("Expected outcome: dir1  dir3  file1");
        System.out.print("What we get in fact: ");
        dir = fs.lsdir(rootname);
        for(int i = 0; i < dir.length; i++) {
            System.out.print(dir[i]+" ");
        }
        System.out.println();


        System.out.println("*****************************************");


        System.out.println("13. Calling disk()");
        System.out.println("Expected outcome: 5 disk blocks occupied");
        System.out.println("What we get in fact:");
        String[][] disk = fs.disk();
        for(int i = 0; i < disk.length; i++) {
            System.out.print(i);
            if (disk[i] == null) {
                System.out.println(" null");
            } else {
                for(int j = 0; j < disk[i].length; j++) {
                    System.out.print(" "+disk[i][j]+" ");
                }
                System.out.println();
            }
        }

        System.out.println("*****************************************");

        System.out.println("14. Creating file2 which requires 6 KB");
        System.out.println("Expected outcome: OutOfSpaceException");
        System.out.println("What we get in fact:");
        String[] namef2 = {"root", "file2"};
        try {
            fs.file(namef2, 6);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        System.out.println("*****************************************");

        System.out.println("15. removing file1");
        System.out.println("Expected outcome: nothing");
        System.out.println("What we get in fact:");
        fs.rmfile(namef1);

        System.out.println("*****************************************");

        System.out.println("16. Calling disk()");
        System.out.println("Expected outcome: no blocks occupied");
        System.out.println("What we get in fact:");
        disk = fs.disk();
        for(int i = 0; i < disk.length; i++) {
            System.out.print(i);
            if (disk[i] == null) {
                System.out.println(" null");
            } else {
                for(int j = 0; j < disk[i].length; j++) {
                    System.out.print(" "+disk[i][j]+" ");
                }
                System.out.println();
            }
        }
    }
}
