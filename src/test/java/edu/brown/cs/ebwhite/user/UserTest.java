//package edu.brown.cs.ebwhite.user;
//
//import java.sql.SQLException;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import edu.brown.cs.ebwhite.database.Db;
//
//public class UserTest {
//
//  private static String dbPath = "~/course/cs032/scavenger/turtlDB.sqlite3";
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    Db.database(dbPath);
//    }
//
//    @Test
//    public void invalidName() {
//
//        boolean exists = true;
//        User u1 = null;
//        try {
//            u1 = UserProxy.ofName("thiswillnotwork");
//        } catch (SQLException e) {
//            exists = false;
//        }
//        assert (exists);
//        assert (u1 == null);
//    }
//
//    @Test
//    public void findUser() {
//        boolean exists = true;
//        User u1 = null;
//        try {
//            u1 = UserProxy.ofName("kzliu");
//        } catch (SQLException e) {
//            System.out.println("HERE1");
//            exists = false;
//        }
//        assert (exists);
//        assert (u1 != null);
//        assert (u1.getUsername().equals("kzliu"));
//        assert (u1.getFollowing().size() > 0);
//    }
//
//    @Test
//    public void sameUser() {
//        boolean exists = true;
//        User u1 = null;
//        User u2 = null;
//        try {
//          u1 = UserProxy.ofName("kzliu");
//      u2 = new UserProxy(u1.getId());
//        } catch (SQLException e) {
//            exists = false;
//        }
//        assert (exists);
//        assert (u1 != null);
//        assert (u1.getUsername().equals(u2.getUsername()));
//    assert (u1.getId() == u2.getId());
//        assert (u1.getFollowers().equals(u2.getFollowers()));
//        assert (u1.equals(u2));
//    }
//
//    @Test
//    public void differentUser() {
//        // Db.setDb(dbPath);
//        boolean exists = true;
//        User u1 = null;
//        User u2 = null;
//        try {
//      u1 = UserProxy.ofName("kzliu");
//      u2 = UserProxy.ofName("ebwhite");
//        } catch (SQLException e) {
//            System.out.println("HERE1");
//            exists = false;
//        }
//        assert (exists);
//        assert (u1 != null);
//        assert (!u1.getUsername().equals(u2.getUsername()));
//    assert (u1.getId() != u2.getId());
//        assert (!u1.equals(u2));
//    }
//
// }
