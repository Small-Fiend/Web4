package Exeptions;

public class IncorrectLogin extends Exception {
    public IncorrectLogin() {
    }

    public IncorrectLogin(String message) {
        super("Incorrect login");
    }
}
