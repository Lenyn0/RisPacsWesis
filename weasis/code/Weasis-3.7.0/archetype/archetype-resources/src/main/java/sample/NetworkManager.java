package sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NetworkManager class is responsible for managing network related tasks.
 * It uses a single thread executor to handle tasks.
 */
public class NetworkManager {
    private final ExecutorService executorService;

    /**
     * Constructor for NetworkManager.
     * Initializes the executor service with a single thread executor.
     */
    public NetworkManager() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Handles the given action in a separate thread.
     *
     * @param flag  the flag indicating the type of the action
     * @param action the action to be executed
     */
    public void handleAction(String flag, Runnable action) {
        executorService.execute(action);
    }
}