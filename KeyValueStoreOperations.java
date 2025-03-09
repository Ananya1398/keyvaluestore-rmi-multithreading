import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The KeyValueStoreOperations class implements the KeyValueStoreOperation interface
 * to provide basic key-value store operations such as put, get, delete, and containsKey.
 * This class uses a ConcurrentHashMap to represent the key-value store.
 */
public class KeyValueStoreOperations extends UnicastRemoteObject implements KeyValueStoreOperation {
  private final ConcurrentHashMap<String, String> KeyValueStore = new ConcurrentHashMap<>();

  /**
   * Constructor for KeyValueStoreOperations.
   * Calls the UnicastRemoteObject constructor to allow remote invocation.
   */
  protected KeyValueStoreOperations() throws RemoteException {
    super();
  }

  /**
   * Logs a message with a timestamp.
   * @param message The message to log.
   */
  private static void log(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String timestamp = sdf.format(new Date());
    String fullThreadName = Thread.currentThread().getName();
    System.out.println("[" + timestamp + "] [" + fullThreadName + "] " + message);
  }

  /**
   * Puts a key-value pair into the key-value store.
   * @param key The key to store.
   * @param value The value to associate with the key.
   */
  @Override
  public String put(String key, String value) throws RemoteException {
    String message;
    if (KeyValueStore.containsKey(key)) {
      message = "Key " + key + " already exists in the key-value store.";
    } else {
      KeyValueStore.put(key, value);
      message = "Successfully added key: " + key + " with value: " + value;
    }
    log(message);
    return message;
  }

  /**
   * Retrieves the value associated with the specified key from the key-value store.
   * @param key The key whose associated value is to be retrieved.
   */
  @Override
  public String get(String key) throws RemoteException {
    String message;
    String result = KeyValueStore.get(key);
    if (result == null) {
      message = "Key not found: " + key;
    } else {
      message = "Value for key " + key + ": " + result;
    }
    log(message);
    return message;
  }

  /**
   * Deletes the key-value pair associated with the specified key.
   * @param key The key to delete from the store.
   */
  @Override
  public String delete(String key) throws RemoteException {
    String message;
    String deletedValue = KeyValueStore.remove(key);
    if (deletedValue == null) {
      message = "Key not found for deletion: " + key;
    } else {
      message = "Successfully deleted key: " + key;
    }
    log(message);
    return message;
  }

  /**
   * Checks if the specified key exists in the key-value store.
   * @param key The key to check for in the store.
   */
  @Override
  public boolean containsKey(String key) throws RemoteException {
    boolean exists = KeyValueStore.containsKey(key);
    String message = "Check existence for key '" + key + "': " + exists;
    log(message);
    return exists;
  }
}
