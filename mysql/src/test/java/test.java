import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author \
 * @Date 2025/7/11 11:37
 * @Description:
 */

public class test {
    String url = "jdbc:mysql://localhost:53306/test?useSSL=false&serverTimezone=UTC";
    String user = "aiplan";
    String password = "aiplan_1q2a3z";


    @Test
    public void test1() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("连接成功！");
        conn.close();
    }
}
