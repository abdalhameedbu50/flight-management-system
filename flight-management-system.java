import java.sql.*;
import java.time.LocalTime;
import java.util.Scanner;

public class FlightManagementSystem {
    private static final String DB_URL = "confidential";
    private static final String DB_USER = "confidential";
    private static final String DB_PASSWORD = "confidential";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            
            while (true) {
                System.out.println("\nFlight Management System");
                System.out.println("1. Airport Operations");
                System.out.println("2. Airline Operations");
                System.out.println("3. Flight Operations");
                System.out.println("4. Passenger Operations");
                System.out.println("5. Manager Operations");
                System.out.println("6. Airport-Airline Relationships");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                
                int mainChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (mainChoice) {
                    case 1:
                        airportOperations(conn, scanner);
                        break;
                    case 2:
                        airlineOperations(conn, scanner);
                        break;
                    case 3:
                        flightOperations(conn, scanner);
                        break;
                    case 4:
                        passengerOperations(conn, scanner);
                        break;
                    case 5:
                        managerOperations(conn, scanner);
                        break;
                    case 6:
                        airportAirlineOperations(conn, scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Airport operations
    private static void airportOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nAirport Operations");
        System.out.println("1. Add new airport");
        System.out.println("2. View all airports");
        System.out.println("3. Update airport");
        System.out.println("4. Delete airport");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter airport name: ");
                String name = scanner.nextLine();
                System.out.print("Enter city: ");
                String city = scanner.nextLine();
                System.out.print("Enter state (optional): ");
                String state = scanner.nextLine();
                
                String insertSql = "INSERT INTO airport (`Airport name`, city, state) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, city);
                    stmt.setString(3, state.isEmpty() ? null : state);
                    stmt.executeUpdate();
                    System.out.println("Airport added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT * FROM airport";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nAirports:");
                    while (rs.next()) {
                        System.out.println("Name: " + rs.getString("Airport name"));
                        System.out.println("City: " + rs.getString("city"));
                        System.out.println("State: " + rs.getString("state"));
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter airport name to update: ");
                String oldName = scanner.nextLine();
                System.out.print("Enter new airport name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter new city: ");
                String newCity = scanner.nextLine();
                System.out.print("Enter new state: ");
                String newState = scanner.nextLine();
                
                String updateSql = "UPDATE airport SET `Airport name` = ?, city = ?, state = ? WHERE `Airport name` = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, newName);
                    stmt.setString(2, newCity);
                    stmt.setString(3, newState.isEmpty() ? null : newState);
                    stmt.setString(4, oldName);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " airport(s) updated");
                }
                break;
                
            case 4:
                System.out.print("Enter airport name to delete: ");
                String delName = scanner.nextLine();
                
                String deleteSql = "DELETE FROM airport WHERE `Airport name` = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setString(1, delName);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " airport(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    // Airline operations
    private static void airlineOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nAirline Operations");
        System.out.println("1. Add new airline");
        System.out.println("2. View all airlines");
        System.out.println("3. Update airline");
        System.out.println("4. Delete airline");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter airline ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter airline name: ");
                String name = scanner.nextLine();
                
                String insertSql = "INSERT INTO air_line (AirlineID, AirLine_Name) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, name);
                    stmt.executeUpdate();
                    System.out.println("Airline added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT * FROM air_line";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nAirlines:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("AirlineID"));
                        System.out.println("Name: " + rs.getString("AirLine_Name"));
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter airline ID to update: ");
                int oldId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter new airline name: ");
                String newName = scanner.nextLine();
                
                String updateSql = "UPDATE air_line SET AirLine_Name = ? WHERE AirlineID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, newName);
                    stmt.setInt(2, oldId);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " airline(s) updated");
                }
                break;
                
            case 4:
                System.out.print("Enter airline ID to delete: ");
                int delId = scanner.nextInt();
                
                String deleteSql = "DELETE FROM air_line WHERE AirlineID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setInt(1, delId);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " airline(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    // Flight operations
    private static void flightOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nFlight Operations");
        System.out.println("1. Add new flight");
        System.out.println("2. View all flights");
        System.out.println("3. Update flight status");
        System.out.println("4. Delete flight");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter flight number: ");
                int number = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter source airport: ");
                String source = scanner.nextLine();
                System.out.print("Enter destination airport: ");
                String dest = scanner.nextLine();
                System.out.print("Enter departure time (HH:MM:SS): ");
                String time = scanner.nextLine();
                System.out.print("Enter initial status: ");
                String status = scanner.nextLine();
                System.out.print("Enter airline ID: ");
                int airlineId = scanner.nextInt();
                
                String insertSql = "INSERT INTO flight_ (FlightNumber, Source_, Destination, Departure, Status, AirLine_ID) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, number);
                    stmt.setString(2, source);
                    stmt.setString(3, dest);
                    stmt.setString(4, time);
                    stmt.setString(5, status);
                    stmt.setInt(6, airlineId);
                    stmt.executeUpdate();
                    System.out.println("Flight added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT f.*, a.AirLine_Name FROM flight_ f JOIN air_line a ON f.AirLine_ID = a.AirlineID";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nFlights:");
                    while (rs.next()) {
                        System.out.println("Number: " + rs.getInt("FlightNumber"));
                        System.out.println("Airline: " + rs.getString("AirLine_Name"));
                        System.out.println("From: " + rs.getString("Source_"));
                        System.out.println("To: " + rs.getString("Destination"));
                        System.out.println("Departure: " + rs.getTime("Departure"));
                        System.out.println("Status: " + rs.getString("Status"));
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter flight number to update: ");
                int flightNum = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter new status: ");
                String newStatus = scanner.nextLine();
                
                String updateSql = "UPDATE flight_ SET Status = ? WHERE FlightNumber = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, newStatus);
                    stmt.setInt(2, flightNum);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " flight(s) updated");
                }
                break;
                
            case 4:
                System.out.print("Enter flight number to delete: ");
                int delNum = scanner.nextInt();
                
                String deleteSql = "DELETE FROM flight_ WHERE FlightNumber = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setInt(1, delNum);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " flight(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    // Passenger operations
    private static void passengerOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nPassenger Operations");
        System.out.println("1. Add new passenger");
        System.out.println("2. View all passengers");
        System.out.println("3. Update passenger details");
        System.out.println("4. Delete passenger");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter passport number: ");
                String passport = scanner.nextLine();
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter address: ");
                String address = scanner.nextLine();
                System.out.print("Enter sex (M/F): ");
                String sex = scanner.nextLine();
                System.out.print("Enter date of birth (YYYY-MM-DD): ");
                String dob = scanner.nextLine();
                System.out.print("Enter flight number: ");
                int flightNum = scanner.nextInt();
                
                String insertSql = "INSERT INTO passenger (PassportNumber, name, address, sex, `date of birth`, `Flight Number`) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, passport);
                    stmt.setString(2, name);
                    stmt.setString(3, address);
                    stmt.setString(4, sex);
                    stmt.setString(5, dob);
                    stmt.setInt(6, flightNum);
                    stmt.executeUpdate();
                    System.out.println("Passenger added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT p.*, f.Source_, f.Destination FROM passenger p JOIN flight_ f ON p.`Flight Number` = f.FlightNumber";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nPassengers:");
                    while (rs.next()) {
                        System.out.println("Passport: " + rs.getString("PassportNumber"));
                        System.out.println("Name: " + rs.getString("name"));
                        System.out.println("Flight: " + rs.getInt("Flight Number") + 
                                         " (" + rs.getString("Source_") + " to " + rs.getString("Destination") + ")");
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter passport number to update: ");
                String oldPassport = scanner.nextLine();
                System.out.print("Enter new address: ");
                String newAddress = scanner.nextLine();
                System.out.print("Enter new flight number: ");
                int newFlightNum = scanner.nextInt();
                
                String updateSql = "UPDATE passenger SET address = ?, `Flight Number` = ? WHERE PassportNumber = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, newAddress);
                    stmt.setInt(2, newFlightNum);
                    stmt.setString(3, oldPassport);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " passenger(s) updated");
                }
                break;
                
            case 4:
                System.out.print("Enter passport number to delete: ");
                String delPassport = scanner.nextLine();
                
                String deleteSql = "DELETE FROM passenger WHERE PassportNumber = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setString(1, delPassport);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " passenger(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    // Manager operations
    private static void managerOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nManager Operations");
        System.out.println("1. Add new manager");
        System.out.println("2. View all managers");
        System.out.println("3. Update manager salary");
        System.out.println("4. Delete manager");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter manager ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter manager name: ");
                String name = scanner.nextLine();
                System.out.print("Enter sex (M/F): ");
                String sex = scanner.nextLine();
                System.out.print("Enter salary: ");
                float salary = scanner.nextFloat();
                scanner.nextLine();
                System.out.print("Enter airport name: ");
                String airport = scanner.nextLine();
                
                String insertSql = "INSERT INTO manager (manager_id, manager_name, sex, salary, airport_name) " +
                                 "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, name);
                    stmt.setString(3, sex);
                    stmt.setFloat(4, salary);
                    stmt.setString(5, airport);
                    stmt.executeUpdate();
                    System.out.println("Manager added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT m.*, a.city FROM manager m JOIN airport a ON m.airport_name = a.`Airport name`";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nManagers:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("manager_id"));
                        System.out.println("Name: " + rs.getString("manager_name"));
                        System.out.println("Airport: " + rs.getString("airport_name") + 
                                         " (" + rs.getString("city") + ")");
                        System.out.println("Salary: " + rs.getFloat("salary"));
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter manager ID to update: ");
                int managerId = scanner.nextInt();
                System.out.print("Enter new salary: ");
                float newSalary = scanner.nextFloat();
                
                String updateSql = "UPDATE manager SET salary = ? WHERE manager_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setFloat(1, newSalary);
                    stmt.setInt(2, managerId);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " manager(s) updated");
                }
                break;
                
            case 4:
                System.out.print("Enter manager ID to delete: ");
                int delId = scanner.nextInt();
                
                String deleteSql = "DELETE FROM manager WHERE manager_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setInt(1, delId);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " manager(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    // Airport-Airline relationship operations
    private static void airportAirlineOperations(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nAirport-Airline Relationships");
        System.out.println("1. Add airline to airport");
        System.out.println("2. View all airport-airline relationships");
        System.out.println("3. Remove airline from airport");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.print("Enter airport name: ");
                String airport = scanner.nextLine();
                System.out.print("Enter airline ID: ");
                int airlineId = scanner.nextInt();
                
                String insertSql = "INSERT INTO contains (arport_name, `airline id`) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, airport);
                    stmt.setInt(2, airlineId);
                    stmt.executeUpdate();
                    System.out.println("Relationship added successfully");
                }
                break;
                
            case 2:
                String selectSql = "SELECT c.`arport_name`, a.AirLine_Name FROM contains c " +
                                  "JOIN air_line a ON c.`airline id` = a.AirlineID";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSql)) {
                    System.out.println("\nAirport-Airline Relationships:");
                    while (rs.next()) {
                        System.out.println("Airport: " + rs.getString("arport_name"));
                        System.out.println("Airline: " + rs.getString("AirLine_Name"));
                        System.out.println("---------------------");
                    }
                }
                break;
                
            case 3:
                System.out.print("Enter airport name: ");
                String delAirport = scanner.nextLine();
                System.out.print("Enter airline ID: ");
                int delAirlineId = scanner.nextInt();
                
                String deleteSql = "DELETE FROM contains WHERE arport_name = ? AND `airline id` = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                    stmt.setString(1, delAirport);
                    stmt.setInt(2, delAirlineId);
                    int rows = stmt.executeUpdate();
                    System.out.println(rows + " relationship(s) deleted");
                }
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }
}