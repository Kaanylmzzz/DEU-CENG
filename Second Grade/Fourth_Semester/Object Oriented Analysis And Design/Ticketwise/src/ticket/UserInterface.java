package ticket;

public interface UserInterface {
    void addCustomer(String name, String surname, String email, String phoneNumber,
                     String gender, String identity, String birthday, String password, boolean policy);
    String getPassword();
    void setPassword(String password);
    String getName();
    void setName(String name);
    String getSurname();
    void setSurname(String surname);
    String getEmail();
    void setEmail(String email);
    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);
    String getGender();
    void setGender(String gender);
    String getIdentity();
    void setIdentity(String identity);
    String getBirthday();
    void setBirthday(String birthday);
    boolean isPolicy();
    void setPolicy(boolean policy);
}
