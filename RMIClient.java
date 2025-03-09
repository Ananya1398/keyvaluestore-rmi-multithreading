import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * KeyValueStoreRMIClient class to interact with the RMI key-value store server.
 */
public class RMIClient {

  private static ExecutorService executor;
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


  public static void main(String[] args) {
    if (args.length < 2) {
      log("Missing arguments. Please use this format: java KeyValueStoreRMIClient <hostaddress> <port>");
      return;
    }

    String hostAddress = args[0];
    int port;

    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      log("Invalid port number. Please enter a valid integer for the port.");
      return;
    }

    try {
      Registry registry = LocateRegistry.getRegistry(hostAddress, port);
      KeyValueStoreOperation stub = (KeyValueStoreOperation) registry.lookup("KeyValueStore");
      log("Connected to RMI server at " + hostAddress + ":" + port);
      prepopulateData(stub);
      executeCommands(stub);

      int THREAD_POOL_SIZE = 5;
      executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

      Scanner scanner = new Scanner(System.in);
      log("Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to exit client:");

      while (true) {
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("close")) {
          log("Closing client connection!");
          break;
        }

        executor.submit(() -> {
          String response = processCommand(stub, input);
          log("Response from server: " + response);
          log("Enter the operation followed by arguments: (Put key value, Get key, Delete key) or type 'close' to exit client:");

        });

      }
    } catch (NotBoundException e) {
      log("Server is not running or not bound in the registry.");
    } catch (RemoteException e) {
      log("RemoteException: Connection to the server failed.");
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      executor.shutdown();
    }
  }

  /**
   * Prepopulates the key-value store with initial data.
   *
   * @param stub The RMI remote object.
   */
  private static void prepopulateData(KeyValueStoreOperation stub) {

    String[] prepopulatedData = {
            "put 1 panda",
            "put 2 bird",
            "put 3 cat",
            "put 4 dog",
            "put 5 fish"
    };

    int THREAD_POOL_SIZE = 5;
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    executor.submit(() -> {
      log("Starting data pre-population");
    });

    for (String operation : prepopulatedData) {
      executor.submit(() -> {
        String response = processCommand(stub, operation);
        log(" Response from server for prepopulating data: " + response);
      });
    }

    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        log("Forcing shutdown as tasks did not complete.");
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }
  }

  /**
   * Performs 5 PUTs, 5 GETs and 5 DELETEs
   *
   * @param stub The RMI remote object.
   */
  private static void executeCommands(KeyValueStoreOperation stub) {

    String[] executeCommands = {
            "put 6 monkey",
            "put 7 elephant",
            "put 8 raccoon",
            "put 9 swan",
            "put 10 duck",
            "get 6",
            "get 7",
            "get 8",
            "get 9",
            "get 10",
            "delete 6",
            "delete 7",
            "delete 8",
            "delete 9",
            "delete 10"
    };

    int THREAD_POOL_SIZE = 5;
    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    executor.submit(() -> {
      log("Starting 5 PUTs, 5 GETs and 5 DELETEs");
    });

    for (String operation : executeCommands) {
      executor.submit(() -> {
        String response = processCommand(stub, operation);
        log("Response from server for executing 5 PUTs, 5 GETs and 5 DELETEs: " + response);
      });

    }

    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        log("Forcing shutdown as tasks did not complete.");
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }
  }



  /**
   * Processes a given command by calling the appropriate RMI method.
   *
   * @param stub The RMI remote object.
   * @param command The command string in the format "operation key [value]".
   * @return The response from the server.
   */
  private static String processCommand(KeyValueStoreOperation stub, String command) {
    String[] arguments = command.split(" ");
    if (arguments.length < 2) {
      return "Invalid input. Need to provide at least 1 operation and 1 argument.";
    }

    String operation = arguments[0];
    String key = arguments[1];

    try {
      switch (operation.toLowerCase()) {
        case "put":
          if (arguments.length < 3) {
            return "Invalid input. Need to provide both key and value for Put command.";
          }
          return stub.put(key, arguments[2]);

        case "get":
          return stub.get(key);

        case "delete":
          return stub.delete(key);

        default:
          return "Invalid operation. Please use 'put', 'get', or 'delete' commands.";
      }
    } catch (RemoteException e) {
      return "RemoteException: Failed to execute operation - " + command;
    }
  }
}
