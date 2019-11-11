import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JDBCUtil {
    private static String url;
    private static String user;
    private static String password;
    private static String db_url;
    private static String db_name;

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
            db_url = prop.getProperty("db_url");
            db_name = prop.getProperty("db_name");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // get a connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        Statement s = conn.createStatement();
        ResultSet resultSet = s.executeQuery("show databases");
        resultSet.beforeFirst();
        String checkdb = "";
        resultSet.next();
        do{
            checkdb = resultSet.getString("Database");
            if(checkdb.equals(db_name)){
                DBAffair.setFlag(1);
                break;
            }
        }while(resultSet.next());
        if(DBAffair.getFlag() == 0){
            String sql = "create database if not exists " + db_name;
            s.executeUpdate(sql);
        }
        conn = DriverManager.getConnection(db_url, user, password);
        return conn;
    }

//    public static void main(String[] args) throws SQLException {
//        Connection connection = getConnection();
//    }

    // release resource
    public static void close(Connection conn) throws SQLException {
        if(conn != null) {
            conn.close();
        }
    }

    public static void close(Statement statement) throws SQLException{
        if(statement != null) statement.close();
    }

     // release resource
    public static void close(Statement stat, ResultSet rs) throws SQLException {
        if(stat != null) {
            stat.close();
        }

        if(rs != null) {
            rs.close();
        }

    }

    // release resource
    public static void close(Connection conn, Statement stat) throws SQLException {
        if (conn != null) {
            conn.close();
        }
        if (stat != null) {
            stat.close();
        }
    }

    // release resource
    public static void close(Connection conn, Statement stat, ResultSet rs) throws SQLException {
        close(conn, stat);
        if (rs != null) {
            rs.close();
        }
    }

}
