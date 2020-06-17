package beans;

import com.sun.xml.internal.bind.v2.TODO;
import logic.Branch;
import logic.Couch;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.validator.ValidatorException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@ManagedBean
@SessionScoped
public class CouchBean {
    private CouchEJB couchEJB = new CouchEJB();
    private String initials;
    private Timestamp birthday;
    private String specialization;
    private long phoneNumber;
    private Branch branch;
    private int ID;

    public Branch getBranch() {
        return branch;
    }

    public int getCurrentBranchID() {
        //System.out.println(branch.toString());
        return branch.getId();
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public CouchEJB getCouchEJB() {
        return couchEJB;
    }

    public void setCouchEJB(CouchEJB couchEJB) {
        this.couchEJB = couchEJB;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getPhoneNumber() {
        return String.valueOf(this.phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        try{
            this.phoneNumber = Long.parseLong(phoneNumber);
        } catch (Exception e) {
        }
    }

    public String getBirthday() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (birthday == null) {
            return "";
        }
        String date = dateFormat.format(birthday);
        System.out.println(date);
        return date;
    }

    public void setBirthday(String birthday) {
        try {
            System.out.println(birthday);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(birthday);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            this.birthday = timestamp;
        } catch(Exception e) {
        }
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void insertCouch(int branchID) {
        this.ID = branchID;
        System.out.println(ID);
        try {
            Flash fScope = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            fScope.put("ID", ID);
            FacesContext.getCurrentInstance().getExternalContext().redirect("insert.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void birthdayValid(FacesContext facesContext, UIComponent uiComponent, Object o){
        String toValidate = o.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(toValidate, formatter);
        LocalDate nowDate = LocalDate.now();

        int years = Period.between(localDate, nowDate).getYears();
        System.out.println(years);
        if (years <= 18) {
            FacesMessage facesMessage = new FacesMessage("Тренеру не может быть меньше 18 лет");
            throw new ValidatorException(facesMessage);
        }
        else {
            DateFormat formatt= new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.birthday = new Timestamp(formatt.parse(o.toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void titleValidator(FacesContext facesContext, UIComponent uiComponent, Object o){
        String toValidate;
        try {
            toValidate = o.toString();
            if (toValidate.equals("")) {
                FacesMessage facesMessage = new FacesMessage("Название не должно быть пустым.");
                throw new ValidatorException(facesMessage);
            }
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    /*public void durationValidator(FacesContext facesContext, UIComponent uiComponent, Object o) {
        long toValidate;
        toValidate = Integer.parseInt(o.toString());
        if (toValidate <= 0) {
            FacesMessage facesMessage = new FacesMessage("Число должно быть положительным и не равно нулю");
            throw new ValidatorException(facesMessage);
        }
    }*/

    public void sizeValidator(FacesContext facesContext, UIComponent uiComponent, Object o) {
        long toValidate;
        toValidate = (long) Double.parseDouble(o.toString());
        if (toValidate <= 0) {
            FacesMessage facesMessage = new FacesMessage("Проверьте номер. Должно быть 11 цифр.");
            throw new ValidatorException(facesMessage);
        }
    }


    public String addCouch(CustomerBean customerBean) throws SQLException, ClassNotFoundException {

        Couch couch = new Couch(initials, birthday, specialization, phoneNumber);
        System.out.println(birthday);
        couchEJB.addCouch(couch, ID);
        customerBean.updateUser();
        return "result";
    }
}
