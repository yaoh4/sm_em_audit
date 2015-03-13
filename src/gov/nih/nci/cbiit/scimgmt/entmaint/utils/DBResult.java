package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.io.Serializable;


/**
 *  All Stored procedure calls return a  this result Object
 *  
 *  Status = SUCCESS > stored procedure is executed
 *  if the the Stored procedure call executes successfully Status = SUCCESS
 *  else Status = FAILURE
 *  
 *      The java clients should check for the status and display the error message  
 *      if the status = FAILURE 
 *      
 */
public class DBResult implements Serializable {
    //Return id from database stored procedure call
    private Long id;
    private String status;
    private String message;
    public static String SUCCESS = "SUCCESS";
    public static String FAILURE = "FAILURE";
    public DBResult() {
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
