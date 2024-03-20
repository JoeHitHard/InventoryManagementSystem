package aad.project.InventoryManagementSystem.config.scurity.exceptions;

public class InvalidAuthRequest extends Exception {
    private final String message;

    public InvalidAuthRequest(String message) {
        super(message);
        this.message = message;
    }
}
