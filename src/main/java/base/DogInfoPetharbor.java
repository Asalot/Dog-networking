package base;

import java.util.Arrays;
import java.util.List;

public class DogInfoPetharbor {

    private String id="";
    private String gender="";
    private String age="";
    private String color="";
    private String breed="";

    public String getMainInfoGAB(){
        return age+" "+gender+" "+breed;
    }
    public void setIntakeDate(String intakeDate) {
        this.intakeDate = intakeDate;
    }

    private String intakeDate="";

    public String getShelter() {
        return shelter;
    }

    private String shelter="";
    private String reason="";
    private String comments="";
    private String date;

    public String getUrl() {
        return url;
    }

    private String url="";

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DogInfoPetharbor(List<String> list) {
        try{id=list.get(0);
        gender=list.get(1);
        color=list.get(2);
        breed=list.get(3);
        age=list.get(4);
        shelter=list.get(6);
        reason=list.get(7);
        comments=list.get(8);}catch(Exception e){}
    }

    public String getId() {
        return id;
    }

    public List<Object> getInfoDog(){
        return Arrays.asList(id,gender, color,breed,age,intakeDate,shelter,reason, comments, date,url);
    }
}
