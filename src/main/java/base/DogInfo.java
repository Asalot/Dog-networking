package base;

import java.util.Arrays;
import java.util.List;

public class DogInfo {

    private String outcome="";
    private String id="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name="";

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender genderValue) {
        this.gender = genderValue;
    }

    private Gender gender;
    private String age="";
    private String weigth="";
    private String color="";
    private String breed="";
    private String intakeDate="";
    private String city="";
    private String hold="";
    private String status="";
    private String comments="";
    private String kennel="";
    private String typeDog="";
    private String date;
    private String url="";

    public void setTypeDog(String typeDog) {
        this.typeDog = typeDog;
    }

    public enum Gender{
        M("Male"),
        F("Female") ;
        public final String label;

        private Gender(String label) {
            this.label = label;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DogInfo(List<String> list,int listNum) {
          try {
              int dif = 0;

              if (list.size() == 9) dif = 1;
              if (list.size() == 10) name = list.get(0);
              id = list.get(1 - dif);
              try {
                  gender = Gender.valueOf(list.get(2 - dif));
              } catch (Exception e) {
              }
              age = list.get(3 - dif);
              weigth = list.get(4 - dif);
              color = list.get(5 - dif);
              breed = list.get(6 - dif);
              if (listNum == 1) intakeDate = list.get(6);
              else intakeDate = list.get(7);
              if (listNum == 0) city = list.get(8);
              else if (listNum == 1) city = list.get(7);
              else city = list.get(6);
              if (city.equals("Huntington Bch")) city = "Huntington Beach";
              else if (city.equals("Anaheim Co")) city = "Anaheim";
              else if (city.equals("San Juan Cap")) city = "San Juan Capistrano";
              city = city.replaceAll("\\s", "");
              url = "https://petharbor.com/pet.asp?uaid=ORNG." + id;
              if (list.size() == 10) hold = list.get(9);
          }catch (Exception e)  {
              System.out.println(list.toString());
              System.out.println(e.getMessage());
          }

     }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public String getKennel() {
        return kennel;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setKennel(String kennel) {
        this.kennel = kennel;
    }

    public DogInfo(String idValue) {
        id = idValue;
    }

    public List<Object> getInfoDog(){
        return Arrays.asList(outcome,name,id,(gender==null)?"":gender.label, age, weigth,color,breed,intakeDate,city,hold,
                date,url,status,kennel,comments,typeDog);
    }
}
