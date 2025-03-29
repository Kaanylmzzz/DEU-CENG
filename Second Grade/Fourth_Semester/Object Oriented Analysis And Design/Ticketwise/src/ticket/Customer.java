package ticket;

import java.io.*;
import java.util.ArrayList;

public class Customer implements UserInterface {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String gender;
    private String identity;
    private String birthday;
    private String password;
    private boolean policy;
    public static ArrayList<Customer> customers = new ArrayList<>();

    public Customer(String name, String surname, String email, String phoneNumber,
                    String gender, String identity, String birthday, String password, boolean policy){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.identity = identity;
        this.birthday = birthday;
        this.password = password;
        this.policy = policy;
    }
    public Customer(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public void addCustomer(String name, String surname, String email, String phoneNumber,
                            String gender, String identity, String birthday, String password, boolean policy){
        Customer newCustomer= new Customer(name, surname, email, phoneNumber, gender, identity, birthday, password, policy);
        customers.add(newCustomer);
        writeToFile("customersInfo.txt",newCustomer);
    }
    public static boolean login(String email, String password) {
        readFromFile("customersInfo.txt");
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    public static void writeToFile(String filename, Customer customer) {
        try {
            FileWriter writer = new FileWriter(filename, true); // true ile dosyanın sonuna ekleme yapılmasını sağlar
            writer.write(customer.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static void printCustomers() {
        for (Customer customer : customers) {
            System.out.println("Name: " + customer.getName());
            System.out.println("Surname: " + customer.getSurname());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Phone Number: " + customer.getPhoneNumber());
            System.out.println("Gender: " + customer.getGender());
            System.out.println("Identity: " + customer.getIdentity());
            System.out.println("Birthday: " + customer.getBirthday());
            System.out.println("Password: " + customer.getPassword());
            System.out.println("Policy: " + customer.isPolicy());
            System.out.println("-------------------");
        }
    }
    public String toString() {
        return name + "," + surname + "," + email + "," + phoneNumber + "," + gender + "," +
                identity + "," + birthday + "," + password + "," + policy;
    }
    public static void readFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Customer customer = new Customer(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], Boolean.parseBoolean(data[8]));
                customers.add(customer);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }
    public Customer existCustomer(String identity){ //Eğer kullanıcı zaten üyeyse

        for (Customer customer : customers) {
            if (customer.getIdentity() == identity) {
                System.out.println("ticket.Customer Information:");
                System.out.println("Name: " + customer.getName());
                System.out.println("Surname: " + customer.getSurname());
                System.out.println("Email: " + customer.getEmail());
                System.out.println("Phone Number: " + customer.getPhoneNumber());
                System.out.println("Gender: " + customer.getGender());
                System.out.println("Identity: " + customer.getIdentity());
                System.out.println("Birthday: " + customer.getBirthday());
                System.out.println("Policy Accepted: " + customer.isPolicy());
                return customer;
            }
        }

        System.out.println("No customer found with the provided identity.");
        return null;

    }

    public static String findCustomerName(String email)
    {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                String name = customer.getName();
                return name;
            }
        }
        return null;
    }
    public static String findCustomerSurname(String email)
    {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                String surname = customer.getSurname();
                return surname;
            }
        }
        return null;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isPolicy() {
        return policy;
    }

    public void setPolicy(boolean policy) {
        this.policy = policy;
    }
}
