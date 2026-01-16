package it.uninafoodlab.gui;

public interface MessageView {
	
	void showError(String message);

    void showSuccess(String message);

    default void showInfo(String message) {
        // opzionale
    }
}
