package edu.brown.cs.ebwhite.user;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.ebwhite.user.User;

public class UserTest {

    private static String dbPath = "testDB.sqlite3";

    @BeforeClass
    public static void setUpClass() throws Exception {
//        Db.setDb(dbPath);
        Db.database("testDB.sqlite3");
    }

    @Test
    public void invalidName() {

        boolean exists = true;
        User u1 = null;
        try {
            u1 = UserProxy.ofName("thiswillnotwork");
        } catch (SQLException e) {
            exists = false;
        }
        assert (exists);
        assert (u1 == null);
    }

    @Test
    public void findUser() {
        boolean exists = true;
        User u1 = null;
        try {
            u1 = UserProxy.ofName("kzliu");
        } catch (SQLException e) {
            System.out.println("HERE1");
            exists = false;
        }
        assert (exists);
        assert (u1 != null);
        assert (u1.getUsername().equals("kzliu"));
        assert (u1.getFollowing().size() > 0);
    }

    @Test
    public void sameUser() {
        boolean exists = true;
        User u1 = null;
        User u2 = null;
        try {
          u1 = UserProxy.ofName("kzliu");
            u2 = new UserProxy(m1.getId());
        } catch (SQLException e) {
            exists = false;
        }
        assert (exists);
        assert (u1 != null);
        assert (u1.getUsername().equals(u2.getUsername()));
        assert (u1.getId().equals(u2.getId()));
        assert (u1.getFollowers().equals(u2.getFollowers()));
        assert (u1.equals(u2));
    }

    @Test
    public void differentUser() {
        // Db.setDb(dbPath);
        boolean exists = true;
        User u1 = null;
        User u2 = null;
        try {
          u1 = MovieProxy.ofName("kzliu");
          u2 = MovieProxy.ofName("ebwhite");
        } catch (SQLException e) {
            System.out.println("HERE1");
            exists = false;
        }
        assert (exists);
        assert (u1 != null);
        assert (!u1.getUsername().equals(u2.getUsername()));
        assert (!u1.getId().equals(u2.getId()));
        assert (!u1.equals(u2));
    }

}
