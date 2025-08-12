import java.sql.*;
import java.util.*;

public class OnlineReservationSystem {

    private static final int MIN_PNR = 1000;
    private static final int MAX_PNR = 9999;
    private static final Scanner sc = new Scanner(System.in);

    // Class to store user credentials
    static class User {
        String username;
        String password;

        public void inputCredentials() {
            System.out.print("Enter MySQL Username: ");
            username = sc.nextLine();
            System.out.print("Enter MySQL Password: ");
            password = sc.nextLine();
        }
    }

    // Class to store reservation details
    static class Reservation {
        int pnrNumber;
        String passengerName;
        String trainNumber;
        String classType;
        String journeyDate;
        String fromLocation;
        String toLocation;

        public void inputDetails() {
            Random rand = new Random();
            pnrNumber = rand.nextInt(MAX_PNR - MIN_PNR + 1) + MIN_PNR;

            System.out.print("Enter Passenger Name: ");
            passengerName = sc.nextLine();
            System.out.print("Enter Train Number: ");
            trainNumber = sc.nextLine();
            System.out.print("Enter Class Type: ");
            classType = sc.nextLine();
            System.out.print("Enter Journey Date (YYYY-MM-DD): ");
            journeyDate = sc.nextLine();
            System.out.print("Enter From Location: ");
            fromLocation = sc.nextLine();
            System.out.print("Enter To Location: ");
            toLocation = sc.nextLine();
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.inputCredentials();

        String url = "jdbc:mysql://localhost:3306/harsh_kumar";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(url, user.username, user.password)) {
                System.out.println("\n✅ Connected to database successfully!");

                while (true) {
                    System.out.println("\n----- ONLINE RESERVATION SYSTEM -----");
                    System.out.println("1. Insert Reservation");
                    System.out.println("2. Delete Reservation");
                    System.out.println("3. Show All Reservations");
                    System.out.println("4. Exit");
                    System.out.print("Enter your choice: ");

                    int choice = Integer.parseInt(sc.nextLine());

                    switch (choice) {
                        case 1:
                            insertReservation(con);
                            break;
                        case 2:
                            deleteReservation(con);
                            break;
                        case 3:
                            showReservations(con);
                            break;
                        case 4:
                            System.out.println("🚪 Exiting...");
                            return;
                        default:
                            System.out.println("❌ Invalid choice! Try again.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void insertReservation(Connection con) {
        Reservation res = new Reservation();
        res.inputDetails();

        String sql = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, res.pnrNumber);
            pst.setString(2, res.passengerName);
            pst.setString(3, res.trainNumber);
            pst.setString(4, res.classType);
            pst.setString(5, res.journeyDate);
            pst.setString(6, res.fromLocation);
            pst.setString(7, res.toLocation);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Reservation Added! PNR: " + res.pnrNumber);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    private static void deleteReservation(Connection con) {
        System.out.print("Enter PNR Number to delete: ");
        int pnr = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM reservations WHERE pnr_number = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, pnr);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Reservation Deleted!");
            } else {
                System.out.println("❌ No record found with that PNR.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    private static void showReservations(Connection con) {
        String sql = "SELECT * FROM reservations";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n📋 All Reservations:");
            while (rs.next()) {
                System.out.println("---------------------------");
                System.out.println("PNR Number: " + rs.getInt("pnr_number"));
                System.out.println("Passenger Name: " + rs.getString("passenger_name"));
                System.out.println("Train Number: " + rs.getString("train_number"));
                System.out.println("Class Type: " + rs.getString("class_type"));
                System.out.println("Journey Date: " + rs.getDate("journey_date"));
                System.out.println("From: " + rs.getString("from_location"));
                System.out.println("To: " + rs.getString("to_location"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
