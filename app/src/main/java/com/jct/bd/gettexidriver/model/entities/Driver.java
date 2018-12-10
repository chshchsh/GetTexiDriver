package com.jct.bd.gettexidriver.model.entities;

import com.google.firebase.database.Exclude;

import java.util.List;

public class Driver {
    private String fullName;
    private String id;
    private String phoneNumber;
    private String email;
    private String creditCard;
    private String password;
    static private List<Ride> driverRides;

    public Driver(String fullName, String id, String phoneNumber, String email, String creditCard,String password) {
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.creditCard = creditCard;
        this.password = password;
    }
    public Driver(){
        this.fullName = "";
        this.id = "";
        this.phoneNumber = "";
        this.email = "";
        this.creditCard = "";
        this.password = "";

    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String lastName) {
        this.fullName = lastName;
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) throws Exception {
            if (IDCheck(id))
                this.id = id;
            else
                throw new Exception ("this id not exists ");
        }
        static boolean IDCheck(String strID)
        {
            int[] id_12_digits = { 1, 2, 1, 2, 1, 2, 1, 2, 1 };
            int count = 0;
            if (strID == null)
                return false;
            strID = leftPad(strID,9,'0');
            for (int i = 0; i < 9; i++)
            {
                int num = Integer.parseInt(strID.substring(i, i+1)) * id_12_digits[i];
                if (num > 9)
                    num = (num / 10) + (num % 10);
                count += num;
            }
            return (count % 10 == 0);
        }
        public static String leftPad(String originalString, int length,
        char padCharacter) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(padCharacter);
            }
            String padding = sb.toString();
            String paddedString = padding.substring(originalString.length())
                    + originalString;
            return paddedString;
        }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception {
        if (phoneNumber.length() == 9 || phoneNumber.length() == 10)
            this.phoneNumber = phoneNumber;
        else
            throw new Exception("the length of the phone number is not make sense");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email.contains("@"))
            this.email = email;
        else
            throw new Exception("the email must contains @");
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if(password.length()>=6)
        this.password = password;
        else
            throw new Exception("the password must be longer then five letters");
    }
}
