import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The KeyValueStoreRMIServer class registers the KeyValueStoreOperations
 * remote object to the RMI registry, allowing clients to call its methods remotely.
 */
public class RMIServer {

  /**
   * Logs a message with a timestamp in milliseconds.
   *
   * @param message The message to log.
   */
  private static void log(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String timestamp = sdf.format(new Date());
    String fullThreadName = Thread.currentThread().getName();
    System.out.println("[" + timestamp + "] [" + fullThreadName + "] " + message);
  }


  /**
   * Main method to start the RMI server with a user-defined port.
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      log("Missing arguments. Please use this format: java KeyValueStoreRMIServer <port>");
      return;
    }

    int port;
    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try {
      KeyValueStoreOperations keyValueStore = new KeyValueStoreOperations();
      Registry registry = LocateRegistry.createRegistry(port);
      log("RMI registry created on port " + port);

      registry.rebind("KeyValueStore", keyValueStore);
      log("RMI Server is running on port " + port + "... Waiting for client connections...");

    } catch (RemoteException e) {
      log("RMI Server exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
