import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class DataPersistence {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/p3-project";
    private static final String USER = "p3-user";
    private static final String PASS = "test";

    public static String getDbUrl() {
        return DB_URL;
    }
    public static String getDbUser() {
        return USER;
    }
    public static String getDbPass() {
        return PASS;
    }
    public static void saveUserData(User user) throws IOException, SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(user);
            }

            String sql = "INSERT INTO users (username, password, userdata, usertype) VALUES (?, ?, ?, ?) ON CONFLICT (username) DO UPDATE SET password = excluded.password, userdata = excluded.userdata, usertype = excluded.usertype";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setBytes(3, baos.toByteArray());
                pstmt.setString(4, user.getUserType());
                pstmt.executeUpdate();
            }
        }
    }

    public static User authenticateUser(String username, String password) throws SQLException, IOException, ClassNotFoundException {
        User authenticatedUser = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT username, password, usertype, userdata FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String fetchedUsername = rs.getString("username");
                        String fetchedPassword = rs.getString("password");
                        String userType = rs.getString("usertype");
                        byte[] buf = rs.getBytes("userdata");
                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf))) {
                            authenticatedUser = (User) ois.readObject();
                            authenticatedUser.setUsername(fetchedUsername);
                            authenticatedUser.setPassword(fetchedPassword);
                            authenticatedUser.setUserType(userType);
                        }
                    }
                }
            }
        }
        return authenticatedUser;
    }


    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

//    public static List<String[]> listAllUsers() throws SQLException {
//        List<String[]> users = new ArrayList<>();
//        String sql = "SELECT username, usertype FROM users";
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                String[] user = new String[2];
//                user[0] = rs.getString("username");
//                user[1] = rs.getString("usertype");
//                users.add(user);
//            }
//        }
//        return users;
//    }

    public static List<String[]> listAllUsers() throws InterruptedException {
        List<String[]> users = Collections.synchronizedList(new ArrayList<>());
        UserListRunnable userListRunnable = new UserListRunnable(users);

        Thread thread = new Thread(userListRunnable);
        thread.start();
        thread.join();

        return users;
    }

    public static void updateUserDetails(String oldUsername, String newUsername, String newPassword, String userType) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, usertype = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, userType);
            pstmt.setString(4, oldUsername);
            pstmt.executeUpdate();
        }
    }
}

