package ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Company implements CompanyInterface {
    private String companyName;
    private String companyPhoneNumber;
    private String type;
    private String password;
    public static ArrayList<Company> companies = new ArrayList<>();


    public Company(String companyName, String companyPhoneNumber,String type, String password) {
        this.companyName = companyName;
        this.companyPhoneNumber = companyPhoneNumber;
        this.type = type;
        this.password = password;
    }
    public Company(String companyName,String password) {
        this.companyName = companyName;
        this.password = password;
    }


    public void addCompany(String companyName, String companyPhoneNumber,String type, String password) {
        Company newCompany= new Company(companyName,companyPhoneNumber,type,password);
        companies.add(newCompany);
        writeToFile("companiesInfo.txt",newCompany);

    }

    public static boolean loginCompany(String companyName, String password) {
        readFromFile("companiesInfo.txt");
        for (Company company : companies) {
            if (company.getCompanyName().equals(companyName) && company.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void writeToFile(String filename, Company company) {
        try {
            FileWriter writer = new FileWriter(filename, true); // true ile dosyanın sonuna ekleme yapılmasını sağlar
            writer.write(company.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public String toString() {
        return companyName + "," + companyPhoneNumber + "," + type + "," + password;
    }
    public static void readFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Company company = new Company(data[0], data[1], data[2], data[3]);
                companies.add(company);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }


    public Company displayCompanyInfo(String companyName) {
        if (this.companyName.equals(companyName)) { // Changed == to equals() for string comparison
            System.out.println("ticket.Company Information:");
            System.out.println("Name: " + companyName);
            System.out.println("Phone Number: " + companyPhoneNumber);
            System.out.println("Type: " + type);
            return this;
        } else {
            System.out.println("No company found with the name.");
            return null;
        }
    }

    public static String findCompanyType(String companyName)
    {
        readFromFile("companiesInfo.txt");
        for(Company company : companies)
        {
            if(company.getCompanyName().equals(companyName))
            {
                return company.getType();
            }
        }

        return null;
    }





    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    @Override
    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static ArrayList<Company> getCompanies() {
        return companies;
    }

    public static void setCompanies(ArrayList<Company> companies) {
        Company.companies = companies;
    }

}
