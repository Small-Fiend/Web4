package beans;

import database.DataBase;
import logic.Couch;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import java.sql.SQLException;

@Stateless
public class CouchEJB {
    public void addCouch(Couch couch, int ID) throws SQLException, ClassNotFoundException {
        DataBase dataSource = new DataBase();
        if(dataSource.existCouch(couch.getinitials())){
            FacesMessage facesMessage = new FacesMessage("Такой тренер уже существует.");
            throw new ValidatorException(facesMessage);
        }
        DataBase.addCouch(couch, ID);
    }
}
