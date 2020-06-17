package beans;

import database.DataBase;
import logic.User;

import javax.ejb.Stateless;
import java.sql.SQLException;

@Stateless
public class CustomerEJB {

    public User validateUserLogin(String login, String password) throws SQLException, ClassNotFoundException {
        DataBase dataSource = new DataBase();
        if(dataSource.existUser(login, password)){
            return dataSource.getUser(login, password);
        } else {
            return null;
        }
    }

    public void deleteCouch(String initials) throws SQLException, ClassNotFoundException {
        DataBase dataSource = new DataBase();
        dataSource.deleteCouch(initials);
    }

}