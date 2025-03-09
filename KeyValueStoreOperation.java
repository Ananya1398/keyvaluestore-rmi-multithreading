import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The KeyValueStoreOperation interface defines the contract for a key-value store
 * that supports basic CRUD operations (put, get, delete, containsKey).
 * This interface is used for Remote Method Invocation (RMI).
 */
public interface KeyValueStoreOperation extends Remote {

  /**
   * Puts a key-value pair into the key-value store.
   * @param key The key to store.
   * @param value The value to associate with the key.
   * @throws RemoteException if a remote communication error occurs.
   */
  String put(String key, String value) throws RemoteException;

  /**
   * Retrieves the value associated with the specified key from the key-value store.
   * @param key The key whose associated value is to be retrieved.
   * @throws RemoteException if a remote communication error occurs.
   */
  String get(String key) throws RemoteException;

  /**
   * Deletes the key-value pair associated with the specified key.
   * @param key The key to delete from the store.
   * @throws RemoteException if a remote communication error occurs.
   */
  String delete(String key) throws RemoteException;

  /**
   * Checks if the specified key exists in the key-value store.
   * @param key The key to check for in the store.
   * @return true if the store contains the key, false otherwise.
   * @throws RemoteException if a remote communication error occurs.
   */
  boolean containsKey(String key) throws RemoteException;
}
