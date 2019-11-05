import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JDBCUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        Properties prop = new Properties();
        try {
            InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            prop.load(is);

            // register driver
            String driverClassName = prop.getProperty("driverClassName");
            Class.forName(driverClassName);

            //  get the url, user, password form from the "jdbc.properties"
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // get a connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    // release resource
    public static void close(Connection conn, Statement stat, ResultSet rs) {
        close(conn, stat);
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn, Statement stat) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = JDBCUtil.getConnection();
        Statement stmt = conn.createStatement();

        JDBCUtil.close(conn, stmt);
    }
}
