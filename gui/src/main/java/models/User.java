package models;

import static util.ValidityChecker.*;

public class User {
    private final String username,password,carName,host,address,MQTTusername,MQTTpassword,MQTTclientID;
    private final boolean anonymous;

    public User(String username, String password,String carName, String host, String address, String MQTTusername, String MQTTpassword, String MQTTclientID) throws Exception{

        if (!notNull(username,password,carName,host,address,MQTTusername,MQTTpassword,MQTTclientID)){
            throw new Exception("User details cannot be null.");
        }
        if (anyEquals(";;;",username,carName,password,carName,host,address,MQTTusername,MQTTpassword,MQTTclientID)){
            throw new Exception("Illegal user details provided.");
        }

        this.username = username;
        this.password = password;

        checkConDetValidity(carName,host,address);

        this.carName = carName;
        this.host = host;
        this.address = address;
        this.MQTTusername = MQTTusername;
        this.MQTTpassword = MQTTpassword;
        this.MQTTclientID = MQTTclientID;

        if (username.isEmpty()){
            anonymous = true;
        }
        else {
            anonymous = false;
        }
    }

    public User(String carName, String host, String address,String MQTTusername, String MQTTpassword, String MQTTclientID) throws Exception{
        this("","",carName,host,address,MQTTusername,MQTTpassword,MQTTclientID);
    }

    public User(String carName, String host, String address) throws Exception{
        this(carName,host,address,"","","");
    }


    private void checkConDetValidity(String carName, String host, String address) throws Exception{
        if (!notBlank(carName,host,address)){
            throw new Exception("Connection details cannot be empty.");
        }
    }

    public boolean checkPassword(String providedPassword){
        return password.equals(providedPassword);
    }



    public String getUsername(){
        return username;
    }

    public String getCarName(){
        return carName;
    }

    public String getHost() {
        return host;
    }

    public String getAddress() {
        return address;
    }

    public String getMQTTusername() {
        return MQTTusername;
    }

    public String getMQTTpassword() {
        return MQTTpassword;
    }

    public String getMQTTclientID() {
        return MQTTclientID;
    }
}
