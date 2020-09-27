package muhammadalighaffar.com;

import com.google.firebase.firestore.Exclude;

public class StudentDetails {

    private String ID,studentid,name,program;

    public StudentDetails(){
        //public no-arg constructor needed for firestore
    }

    public StudentDetails(String studentid, String name, String program) {
        this.studentid = studentid;
        this.name = name;
        this.program = program;
    }

    public String getStudentID() {
        return studentid;
    }

    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }

    //For saving firestore system genrated ID's
    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
