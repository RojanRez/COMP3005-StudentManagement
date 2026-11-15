
import java.sql.*;
import java.util.Scanner;

public class StudentApp {

    // PostgreSQL database connection details
    private static final String URL =
            "jdbc:postgresql://localhost:5432/student_db";
    private static final String USER = "student_app";

    // 1. Retrieve and display all student records
    public static void getAllStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Students ---");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("student_id") + " | " +
                    rs.getString("first_name") + " | " +
                    rs.getString("last_name") + " | " +
                    rs.getString("email") + " | " +
                    rs.getDate("enrollment_date")
                );
            }

            if (!found) {
                System.out.println("No students found.");
            }
        }
    }

    // 2. Insert a new student into the database
    public static void addStudent(Connection conn, String first_name,
            String last_name, String email, String enrollment_date)
            throws SQLException {

        String sql = "INSERT INTO students " +
                "(first_name, last_name, email, enrollment_date) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, Date.valueOf(enrollment_date));

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " student added successfully.");
        }
    }

    // 3. Update the email of a student using their ID
    public static void updateStudentEmail(Connection conn,
            int student_id, String new_email) throws SQLException {

        String sql = "UPDATE students SET email = ? WHERE student_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);

            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("Student email updated successfully.");
            else
                System.out.println("Student not found.");
        }
    }

    // 4. Delete a student using their ID
    public static void deleteStudent(Connection conn, int student_id)
            throws SQLException {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student_id);

            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("Student deleted successfully.");
            else
                System.out.println("Student not found.");
        }
    }

    // Display menu and run the selected operation
    public static void main(String[] args) {

        // Read the database password from an environment variable
        // instead of storing it directly in the code
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isEmpty()) {
            System.out.println("DB_PASSWORD is not set.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                URL, USER, password);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to PostgreSQL successfully!");

            while (true) {
                System.out.println("\n--- Student Management ---");
                System.out.println("1. View all students");
                System.out.println("2. Add student");
                System.out.println("3. Update student email");
                System.out.println("4. Delete student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("5")) {
                    System.out.println("Goodbye!");
                    break;
                }

                try {
                    switch (choice) {
                        case "1":
                            getAllStudents(conn);
                            break;

                        case "2":
                            System.out.print("First name: ");
                            String first = scanner.nextLine();
                            System.out.print("Last name: ");
                            String last = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Enrollment date (YYYY-MM-DD): ");
                            String date = scanner.nextLine();

                            addStudent(conn, first, last, email, date);
                            break;

                        case "3":
                            System.out.print("Student ID: ");
                            int updateId = Integer.parseInt(scanner.nextLine());
                            System.out.print("New email: ");
                            String newEmail = scanner.nextLine();

                            updateStudentEmail(conn, updateId, newEmail);
                            break;

                        case "4":
                            System.out.print("Student ID: ");
                            int deleteId = Integer.parseInt(scanner.nextLine());

                            deleteStudent(conn, deleteId);
                            break;

                        default:
                            System.out.println("Invalid option.");
                    }
                } catch (SQLException | IllegalArgumentException e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
