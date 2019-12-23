package system;


public class BadFileNameException extends Exception {
    public BadFileNameException() {
        super("Invalid File Name");
    }
}