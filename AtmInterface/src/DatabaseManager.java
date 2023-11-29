import java.sql.*;

public class DatabaseManager {
    private Connection connection;
    private Statement statement;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBalance(String cardNumber, String pin) {
        try {
            String query = "SELECT balance FROM accounts WHERE card_number = '" + cardNumber + "' AND pin = '" + pin + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void updateBalance(String cardNumber, String pin, double newBalance) {
        try {
            String updateQuery = "UPDATE accounts SET balance = " + newBalance +
                    " WHERE card_number = '" + cardNumber + "' AND pin = '" + pin + "'";
            statement.executeUpdate(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

