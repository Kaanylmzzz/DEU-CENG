package ticket;

public interface CompanyInterface {
    void addCompany(String companyName, String companyPhoneNumber,String type, String password);

    Company displayCompanyInfo(String companyName);

    String getCompanyName();
    void setCompanyName(String companyName);
    String getCompanyPhoneNumber();
    void setCompanyPhoneNumber(String companyPhoneNumber);
}
