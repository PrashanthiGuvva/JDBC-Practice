import java.sql.*;
import java.util.Scanner;
public class JDBCCrudExample {
    private static final String URL = "jdbc:mysql://localhost:3306/test1db";
    private static final String USER = "root";
    private static final String PASSWORD = "xxxx";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {
            System.out.println("✅ Connected to MySQL Database!");
            while (true) {
                System.out.println("\n=== JDBC CRUD Menu ===");
                System.out.println("1. Insert User");
                System.out.println("2. Read Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1 -> insertUser(conn, sc);
                    case 2 -> readUsers(conn);
                    case 3 -> updateUser(conn, sc);
                    case 4 -> deleteUser(conn, sc);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice, try again!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void insertUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            int rows = pstmt.executeUpdate();
            System.out.println(" User added! Rows affected: " + rows);
        }
    }
    private static void readUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Users in DB ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Email: %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        }
    }
    private static void updateUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        String sql = "UPDATE users SET name=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println(" User updated! Rows affected: " + rows);
        }
    }
    private static void deleteUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println("User deleted! Rows affected: " + rows);
        }
    }
}

