package logic;

import javax.xml.bind.annotation.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@XmlAccessorType(XmlAccessType.FIELD)
public class Couch implements Serializable, Comparable<Couch> {
    private String initials;
    @XmlElement(name="birthday")
    @XmlSchemaType(name="timestamp")
    private Timestamp birthday;
    private String specialization;
    private long phoneNumber;

    public Couch(){}

    public Couch(String initials){
        this.initials = initials;
        this.birthday = null;
        this.specialization = "";
    }

    public Couch(String initials, Timestamp birthday, String specialization, long phoneNumber){
        this.initials = initials;
        this.birthday = birthday;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
    }

    public String getinitials() {
        return initials;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getInitials() {
        return initials;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public void setinitials(String initials) {
        this.initials = initials;
    }

    public void setPhoneNumber(long phoneNumber) {
            try {
                String timePhone = phoneNumber + "";
                if (timePhone.length() == 11){
                    this.phoneNumber = phoneNumber;
                }
            } catch (NumberFormatException e) {
                System.out.println("!!Error!!\nWrong number entered!!");
            }
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }


    /*public static Writer writeFile(Couch element, Writer fileSave) {
        try {
            JSONObject writeFile = new JSONObject();
            writeFile.put("initials", element.getinitials());
            writeFile.put("birthday", element.getBirthday());
            writeFile.put("phoneNumber", element.getPhoneNumber());
            writeFile.put("Likes", element.getSpecialization());
            fileSave.write(writeFile.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileSave;
    }

    public static void readFile(String filePath, Couch element){
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            DateFormat formatt = new SimpleDateFormat("dd.MM.yyyy");
            JSONObject json = new JSONObject(reader.readLine());
            element.setinitials(json.getString("initials"));
            element.setBirthday(new Timestamp(formatt.parse(json.getString("birthday")).getTime()));
            element.setPhoneNumber(json.getInt("phoneNumber"));
            element.setSpecialization(json.getString("specialization"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Couch readElement(JSONObject json) throws ParseException {
        DateFormat formatt = new SimpleDateFormat("dd.MM.yyyy");
        Couch timeElement = new Couch();
        timeElement.setinitials(json.getString("initials"));
        timeElement.setBirthday(new Timestamp(formatt.parse(json.getString("birthday")).getTime()));
        timeElement.setSpecialization(json.getString("specialization"));
        timeElement.setPhoneNumber(json.getInt("phoneNumber"));
        return timeElement;
    }*/

    @Override
    public int compareTo(Couch element) {
        return this.initials.compareTo(element.getinitials());
    }

    @Override
    public String toString(){
        return "initials: " + this.initials +
                " birthday: " + this.birthday +
                " specialization: " + this.specialization +
                " phoneNumber: " + this.phoneNumber;
    }
}
