import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
public class UserListRunnable implements Runnable {
    private final List<String[]> usersList;

    public UserListRunnable(List<String[]> usersList) {
        this.usersList = usersList;
    }

    @Override
    public void run() {
        try {
            String sql = "SELECT username, usertype FROM users";
            try (Connection conn = DriverManager.getConnection(DataPersistence.getDbUrl(), DataPersistence.getDbUser(), DataPersistence.getDbPass());
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String[] user = new String[2];
                    user[0] = rs.getString("username");
                    user[1] = rs.getString("usertype");
                    usersList.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}

