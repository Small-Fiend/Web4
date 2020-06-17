package logic;

import javax.xml.bind.annotation.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Branch implements Serializable, Comparable<Branch> {
    @XmlElement(name = "branchId")
    private int id;
    @XmlElementWrapper(name = "couchList")
    @XmlElement(name = "couch")
    private List<Couch> arrayCouches;
    @XmlElement(name = "branchName")
    private String nameBranch;
    @XmlTransient
    private String error;
    @XmlTransient
    private boolean checkError = false;


    public Branch(){
        this.arrayCouches = new ArrayList<Couch>();
    }

    public Branch(int id, String name){
        this.id = id;
        this.nameBranch = name;
        this.arrayCouches = new ArrayList<Couch>();
    }

    public Branch(String name, List<Couch> couchList){
        this.nameBranch = name;
        this.arrayCouches = couchList;
    }

    public int getLength(){
        return this.arrayCouches.size();
    }

    public String getNameBranch() {
        return nameBranch;
    }

    public List<Couch> getArrayCouches() {
        return arrayCouches;
    }

    public void setNameBranch(String nameBranch) {
        this.nameBranch = nameBranch;
    }

    public String getRowError() {
        return error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Couch getCouch(int index) {
        return arrayCouches.get(index);
    }

    public boolean isCheckError() {
        return checkError;
    }

    public void addElementToIndex(int index, Couch Couch){
        this.arrayCouches.add(index, Couch);
    }

    public void addElement(Couch Couch){
        this.arrayCouches.add(Couch);
    }

    public void deleteCouch(int index){
        this.arrayCouches.remove(index);
    }

    public void setArrayCouches(List<Couch> arrayCouches) {
        this.arrayCouches = arrayCouches;
    }

   /* public void writeFile(String pathToFile){
        try (Writer fileSave = new FileWriter(pathToFile)) {
            JSONObject writeFile = new JSONObject();
            int index = this.arrayCouches.size();
            fileSave.write('[');
            for (Couch element: this.arrayCouches) {
                Couch.writeFile(element, fileSave);
                if (index > 1)  fileSave.write(',');
                index--;
            }
            fileSave.write(']');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String pathToTheFile){
        try {
            File file = new File(pathToTheFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            Couch timeElement;
            JSONArray json = new JSONArray(reader.readLine());
            for (int i = 0; i < json.length(); i++){
                timeElement = Couch.readElement(json.getJSONObject(i));
                this.arrayCouches.add(timeElement);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    public void listSort(){
        try {
            Collections.sort(this.arrayCouches);
        }catch (Error e){
            this.checkError = true;
            this.error += "\n" + e;
        }
    }

    public boolean equals(Couch element, Couch element2){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(element.getBirthday()).equals(format.format(element2.getBirthday())) &&
                element.getinitials().equals(element2.getinitials()) &&
                element.getPhoneNumber() == element.getPhoneNumber() &&
                element.getSpecialization().equals(element.getSpecialization());
    }

    public void deleteDuplicate() {
        try {
            for (int i = 0; i < this.arrayCouches.size(); i++){
                for (int j = 0; j < this.arrayCouches.size(); j++){
                    if((this.equals(this.getCouch(i), getCouch(j)))){
                        if (i != j)
                            this.deleteCouch(j);
                    }
                }
            }
        }catch (Error e){
            this.checkError = true;
            this.error += "\n" + e;
        }
    }

    @Override
    public String toString() {
        String returnString = "arrayCoaches{";
        for (Couch element: this.arrayCouches){
            returnString += element.toString();
        }
        return returnString + ", nameBranch=" + nameBranch + '}';
    }


    @Override
    public int compareTo(Branch branch) {
        return this.getNameBranch().compareTo(branch.getNameBranch());
    }
}
