package database;

import logic.Branch;
import logic.Couch;
import logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static String url = "jdbc:mariadb://localhost:3306/labWeb";
    private static String USER = "root";
    private static String PASSWORD = "789";

    /*public void CreateTable() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
        conn.setAutoCommit(false);

        String scriptBranch = "CREATE TABLE `branch` (\n" +
                "  `name_branch` char(20) DEFAULT NULL,\n" +
                "  `branchId` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`branchId`)\n" +
                ")";
        String scriptCouch = " CREATE TABLE `couch` (\n" +
                "  `initials` char(20) DEFAULT NULL,\n" +
                "  `birthday` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),\n" +
                "  `spetialization` char(20) DEFAULT NULL,\n" +
                "  `phoneNumber` bigint(11) DEFAULT NULL,\n" +
                "  `idBranch` int(4) DEFAULT NULL,\n" +
                "  KEY `couch_branch_branchId_fk` (`idBranch`),\n" +
                "  CONSTRAINT `couch_branch_branchId_fk` FOREIGN KEY (`idBranch`) REFERENCES `branch` (`branchId`)\n" +
                ")";
        String scriptUsersTable = "CREATE TABLE `user_table` (\n" +
                "  `userId` int(11) NOT NULL,\n" +
                "  `name` char(20) DEFAULT NULL,\n" +
                "  `login` char(30) DEFAULT NULL,\n" +
                "  `password` char(30) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`userId`)\n" +
                ")";
        String scriptLinks = " CREATE TABLE `links` (\n" +
                "  `userId` int(11) DEFAULT NULL,\n" +
                "  `branchId` int(11) DEFAULT NULL,\n" +
                "  KEY `links_ibfk_1` (`branchId`),\n" +
                "  KEY `links_ibfk_2` (`userId`),\n" +
                "  CONSTRAINT `links_ibfk_1` FOREIGN KEY (`branchId`) REFERENCES `branch` (`branchId`),\n" +
                "  CONSTRAINT `links_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user_table` (`userId`)\n" +
                ")" ;
        Statement statement = conn.createStatement();
        statement.addBatch(scriptBranch);
        statement.addBatch(scriptCouch);
        statement.addBatch(scriptUsersTable);
        statement.addBatch(scriptLinks);
        statement.executeBatch();
        statement.close();
        conn.commit();
        conn.close();
    };*/

    public static List<User> getArrayUser(){
        List<User> timeArray = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("select userId, login, name from user_table");
            ResultSet rs = preparedStatement.executeQuery();
            timeArray = new ArrayList<User>();
            while ( rs.next() ) {
                User timeUser = new User(rs.getInt("userId"),
                        rs.getString("login"),
                        rs.getString("name"));
                timeUser.setBranch(getBranch(timeUser.getId()));
                timeArray.add(timeUser);
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return timeArray;
    }
    public static int getId(){
        List<Integer> arrayId = new ArrayList<Integer>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("select id from users");
            ResultSet rs = preparedStatement.executeQuery();
            while ( rs.next() ) {
                arrayId.add(rs.getInt("id"));
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (arrayId.size() != 0){
            return arrayId.get(arrayId.size() - 1) + 1;
        }else {
            return 0;
        }
    }
    public static String getPassword(int idUser){
        String password = "";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("select password from user_table where userId = (?)");
            preparedStatement.setInt(1, idUser);
            ResultSet rs = preparedStatement.executeQuery();
            while ( rs.next() ) {
                password = rs.getString("password");
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static void addUser(User user){

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
            conn.setAutoCommit(false);

            PreparedStatement preparedStatement = conn.prepareStatement("insert into users values (?, ?, ?, ?)");
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getNameUser());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            for (Branch branch : user.getBranch()){
                preparedStatement = conn.prepareStatement("insert into branch values (?, ?)");
                preparedStatement.setString(1, branch.getNameBranch());
                preparedStatement.setInt(2, user.getId());
                preparedStatement.executeUpdate();
                for (Couch couch: branch.getArrayCouches()){
                    preparedStatement = conn.prepareStatement("insert into couch (initials, birthday, spetialization, phoneNumber, nameBranch) values (?, ?, ?, ?, ?)");
                    preparedStatement.setString(1, couch.getinitials());
                    preparedStatement.setTimestamp(2, couch.getBirthday());
                    preparedStatement.setString(3, couch.getSpecialization());
                    preparedStatement.setLong(4, couch.getPhoneNumber());
                    preparedStatement.setString(5, branch.getNameBranch());
                    preparedStatement.executeUpdate();
                }
            }
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static User checkUser(User user){
        List<User> userList = getArrayUser();
        for (User userDB: userList){
            if(user.getLogin().equals(userDB.getLogin())){
                user.setId(userDB.getId());
                user.setNameUser(userDB.getNameUser());
            }
        }

        return user;
    }

    public User getUser(String login, String password) throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
        conn.setAutoCommit(false);
        PreparedStatement statement = conn.prepareStatement("select userId, name from user_table where login=? and password=?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        rs.next();
        List<Branch> branches = getBranch(rs.getInt("userId"));
        User tempUser = new User(rs.getInt("userId"),
                login,
                rs.getString("name"),
                password,
                branches);
        conn.close();
        return  tempUser;
    }

    public boolean existUser(String login, String password) throws SQLException, ClassNotFoundException {
        // Данный метод проверяет наличие Юзера
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
        conn.setAutoCommit(false);
        PreparedStatement statement = conn.prepareStatement("select exists(select userId from user_table where login = ? and password = ?)");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        rs.next();
        conn.close();
        return rs.getBoolean(1);
    }

    public boolean existCouch(String initials) throws SQLException, ClassNotFoundException {
        // Данный метод проверяет наличие тренера
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
        conn.setAutoCommit(false);
        PreparedStatement statement = conn.prepareStatement("select exists(select birthday from couch where initials = ?)");
        statement.setString(1, initials);
        ResultSet rs = statement.executeQuery();
        rs.next();
        conn.close();
        return rs.getBoolean(1);
    }

    public static List<Branch> getBranch(int idUser){
        List<Branch> branches = new ArrayList<>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("select name_branch, branchId from branch where branchId in (select branchId from links where userId = ?)");
            preparedStatement.setInt(1, idUser);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Branch timeBranch = new Branch(rs.getInt("branchId"), rs.getString("name_branch"));
                timeBranch.setArrayCouches(getArrayCouch(timeBranch.getId()));
                branches.add(timeBranch);
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return branches;
    }
    public static List<Couch> getArrayCouch(int idBranch){
        List<Couch> arrayCouch = new ArrayList<Couch>();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("select * from couch where idBranch = ? ");
            preparedStatement.setInt(1, idBranch);


            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Couch timeCouch = new Couch();
                timeCouch.setinitials(rs.getString("initials"));
                timeCouch.setBirthday(rs.getTimestamp("birthday"));
                timeCouch.setPhoneNumber(rs.getLong("phoneNumber"));
                timeCouch.setSpecialization(rs.getString("spetialization"));
                arrayCouch.add(timeCouch);
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayCouch;
    }
    public static Couch getCouch(String initials){
        Couch timeCouch = new Couch();
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("select * from couch where initials= ?");
            preparedStatement.setString(1, initials);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                timeCouch.setinitials(rs.getString("initials"));
                timeCouch.setBirthday(rs.getTimestamp("birthday"));
                timeCouch.setPhoneNumber(rs.getLong("phoneNumber"));
                timeCouch.setSpecialization(rs.getString("spetialization"));
            }

            conn.commit();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return timeCouch;
    }


    public static void addBranch(Branch branch){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("insert into branch (name_Branch, branchId) values (?, ?)");
            preparedStatement.setString(1, branch.getNameBranch());
            preparedStatement.setInt(2, branch.getId());
            preparedStatement.executeUpdate();
            conn.commit();

            for(Couch couch: branch.getArrayCouches()){
                addCouch(couch, branch.getId());
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addCouch(Couch couch, int idBranch){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("insert into couch (initials, birthday, spetialization, phoneNumber, idBranch) values (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, couch.getinitials());
            preparedStatement.setTimestamp(2, couch.getBirthday());
            preparedStatement.setString(3, couch.getSpecialization());
            preparedStatement.setLong(4, couch.getPhoneNumber());
            preparedStatement.setInt(5, idBranch);
            preparedStatement.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBranch(Branch branch){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
            conn.setAutoCommit(false);
            for(Couch couch: branch.getArrayCouches()){
                deleteCouch(couch.getinitials());
            }
            PreparedStatement preparedStatement = conn.prepareStatement("delete from branch where branchId = ?");
            preparedStatement.setInt(1, branch.getId());
            preparedStatement.execute();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void deleteCouch(String initials){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,USER,PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("delete from couch where initials = ?");
            preparedStatement.setString(1, initials);
            preparedStatement.execute();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
