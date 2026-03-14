package code.web.lightup.util;


import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;


public class BaseDao {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "lightup";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Jdbi jdbi = null;


    public static Jdbi get() {
        if (jdbi == null) {
            try {
                MysqlDataSource ds = new MysqlDataSource();
                ds.setURL("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE);
                ds.setUser(DB_USER);
                ds.setPassword(DB_PASSWORD);
                ds.setUseSSL(false);
                ds.setServerTimezone("UTC");
                ds.setAllowPublicKeyRetrieval(true);

                jdbi = Jdbi.create(ds);

                System.out.println("Kết nối JDBI MySQL thành công!");
            } catch (Exception e) {
                System.err.println("Lỗi kết nối JDBI MySQL!");
                e.printStackTrace();
            }
        }
        return jdbi;
    }

}

