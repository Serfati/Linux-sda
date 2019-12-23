package system;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NodeTest.class,
        LeafTest.class,
        TreeTest.class,
        SpaceTest.class,
        FileSystemTest.class
})

public class SDAtestSuite {


    public static void main(String[] args) {
        final int memorySize = 10240;
        ArrayList<String> prompt = new ArrayList<>();
        prompt.add("root");
        String shell = "";
        FileSystem sda = new FileSystem(memorySize);
        Scanner sc = new Scanner(System.in);
        while(!shell.equalsIgnoreCase("exit")) {
            System.out.print("serfati@Ubuntu: /"+formatPrompt(prompt)+"$  ");
            shell = sc.next();
            switch(shell) {
                case "mkdir":
                    try {
                        String folderName = sc.next();
                        if (folderName.contains("/")) {
                            System.out.println(
                                    "mkdir: cannot create directory '"+folderName+"': No such file or Directory");
                            break;
                        }
                        sda.dir(formatPath(formatPrompt(prompt)+"/"+folderName));
                    } catch(BadFileNameException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "cd":
                    try {
                        String folderName = sc.next();
                        if (folderName.contains("src/main")) {
                            if (prompt.size() > 1)
                                prompt.remove(prompt.size()-1);
                            break;
                        }
                        Tree tree = sda.DirExists(formatPath(formatPrompt(prompt)+"/"+folderName));
                        if (tree != null) {
                            prompt.addAll(Arrays.asList(folderName.split("/")));
                        } else {
                            System.out.println("cd: "+folderName+": No such Directory");
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "rmdir":
                    try {
                        String folderName = sc.next();
                        sda.rmdir(formatPath(formatPrompt(prompt)+"/"+folderName));
                    } catch(DirectoryNotEmptyException e) {
                        e.printStackTrace();
                    }

                    break;
                case "touch":
                    try {
                        String fileName = sc.next();
                        int fileSize = Integer.parseInt(sc.next());
                        if (fileName.matches("[a-z1-9 ]+") && fileSize < memorySize)
                            sda.file(formatPath(formatPrompt(prompt)+"/"+fileName), fileSize);
                        else
                            System.out.println("touch: cannot create file '"+fileName+"': No such file or Directory");
                    } catch(BadFileNameException | OutOfSpaceException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "rm":
                    String rm_flag = sc.next();
                    if ("-r".equals(rm_flag.toLowerCase())) {

                    } else
                        sda.rmfile(formatPath(formatPrompt(prompt)+"/"+rm_flag));
                    break;
                case "ls":
                    String[] list = sda.lsdir(formatPath(formatPrompt(prompt)));
                    printStringArray(list);
                    break;
                case "pwd":
                    System.out.println(formatPrompt(prompt));
                case "isfile":
                    try {
                        String fileName = sc.next();
                        if (sda.DirExists(formatPath(formatPrompt(prompt)+"/"+fileName)) != null)
                            System.out.println("Yes");
                        else
                            System.out.println("No");
                    } catch(Exception e) {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "isdir":
                    try {
                        String dirName = sc.next();
                        if (sda.DirExists(formatPath(formatPrompt(prompt)+"/"+dirName)) != null)
                            System.out.println("Yes");
                        else
                            System.out.println("No");
                    } catch(Exception e) {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "fileexists":
                    try {
                        Leaf fpath = sda.FileExists(formatPath(formatPrompt(prompt)));
                        String[] parray = fpath.getPath();
                        StringBuilder p = new StringBuilder("File exists at:");
                        for(int i = 0; i < parray.length-1; i++) p.append(parray[i]).append("/");
                        System.out.println(p);
                    } catch(Exception e) {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "disk":
                    try {
                        FileOutputStream fos = new FileOutputStream("linuxSda.ser");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(sda);
                        oos.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "exit":
                    break;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println("Command not found");
                    printHelp();
                    break;
            }
        }
        sc.close();
    }

    private static String formatPrompt(ArrayList<String> prompt) {
        StringBuilder promptText = new StringBuilder();
        for(int i = 0; i < prompt.size(); i++) {
            promptText.append(prompt.get(i));
            if (i < prompt.size()-1)
                promptText.append("/");
        }
        return promptText.toString();
    }

    private static void printStringArray(String[] lsdir) {
        if (lsdir == null)
            return;
        for(String s : lsdir)
            System.out.print(s+"  ");
        System.out.println();
    }

    private static void printHelp() {
        System.out.println("Command                            Details");
        System.out.println("=======================            ===========================================");
        System.out.println("mkdir <Directory Name>             Create a new Directory");
        System.out.println("pwd                                Displays the current working Directory");
        System.out.println("cd <Directory Name>                Change Directory, .. to go to parent Directory");
        System.out.println("rmdir <Directory Name>             removes Empty repository");
        System.out.println("touch <File name> <File Size>      Creates a file with size <File Size>");
        System.out.println("rm <File Name>                     Delete the file");
        System.out.println("isfile <File Name>                 Check if current path a file");
        System.out.println("isdir <Directory Name>             Check if current path a directory");
        System.out.println("ls                                 List all files and directories in current directory");
        System.out.println("help                               Displays all the commands available");
        System.out.println("exit                               exits the file simulator");
    }

    public static String[] formatPath(String path) {
        return path.split("/");
    }
}