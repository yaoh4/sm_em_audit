package gov.nih.nci.cbiit.scimgmt.entmaint.security;

public class Address {
    private Integer id;
    private String address;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String zip;
    private String country;

    /**
     * @param integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @return
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     * @return
     */
    public String getZip() {
        return zip;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param string
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param string
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @param string
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * @param string
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param integer
     */
    public void setId(int id) {
        this.id = new Integer(id);
    }

    /**
     * @param string
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @param string
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Address)) {
            return false;
        }

        Address bean = (Address)obj;
        int nil = (this.getId() == null) ? 1 : 0;
        nil += (bean.getId() == null) ? 1 : 0;

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getId().equals(bean.getId());
        }

    }


    public int hashCode() {
        return (this.getId() == null) ? 17 : this.getId().hashCode();
    }

    public boolean isEmpty() {
        if (id == null && address == null && address2 == null && 
            address3 == null && city == null && state == null && zip == null && 
            country == null)
            return true;
        return false;

    }
}
