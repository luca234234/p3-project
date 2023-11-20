import java.io.*;

public class DataPersistence {

    public static void saveUserData(User user, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(user);
        }
    }

    public static User loadUserData(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (User) in.readObject();
        }
    }
}
