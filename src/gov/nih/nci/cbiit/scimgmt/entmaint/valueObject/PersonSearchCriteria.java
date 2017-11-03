package gov.nih.nci.cbiit.scimgmt.entmaint.valueObject;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.NciPeopleVw;

import java.util.List;

/**
 * Created by polonskyy on 7/14/17.
 */
public class PersonSearchCriteria {
    String term;
    List<NciPeopleVw> result;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<NciPeopleVw> getResult() {
        return result;
    }

    public void setResult(List<NciPeopleVw> result) {
        this.result = result;
    }
}
