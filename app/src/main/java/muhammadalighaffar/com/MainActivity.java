package muhammadalighaffar.com;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtID,edtName,edtProgram;
    private TextView text_view_data;

    //Creating final keys for cloud firestore
    private static final String KEY_STUDENT_ID="studentid";
    private static final String KEY_NAME="name";
    private static final String KEY_PROGRAM="program";

    //Database Reference

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference students;
    private CollectionReference readStudentData = db.collection("Student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtID=findViewById(R.id.edtID);
        edtName=findViewById(R.id.edtName);
        edtProgram=findViewById(R.id.edtProgram);
        text_view_data=findViewById(R.id.text_view_data);
    }
    //Fetch data in realtime

    @Override
    protected void onStart() {
        super.onStart();
        readStudentData
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        StringBuilder builder = new StringBuilder();

                        for (QueryDocumentSnapshot document : value) {
                            StudentDetails studentDetails=document.toObject(StudentDetails.class);
                            studentDetails.setID(document.getId());


                            String id = studentDetails.getStudentID();
                            String name =studentDetails.getName();
                            String program =studentDetails.getProgram();

                            builder.append("Student ID :"+id +"\n"+
                                    "Student Name :"+name +"\n"+
                                    "Student Program :"+program+"\n\n");

                        }
                        text_view_data.setText(builder.toString());
                    }
                });
    }

    public void addDetails(View view){

        Map<String, Object> details = new HashMap<>();
        details.put(KEY_STUDENT_ID,edtID.getText().toString());
        details.put(KEY_NAME,edtName.getText().toString());
        details.put(KEY_PROGRAM,edtProgram.getText().toString());

        readStudentData.add(details)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Student Details saved", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        /*
        students = db.collection("Student").document(edtID.getText().toString());
        students.set(details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/

    }
    public void getRetrieveData(View view){
                readStudentData
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        StringBuilder builder = new StringBuilder();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                            StudentDetails studentDetails=document.toObject(StudentDetails.class);
                            studentDetails.setID(document.getId());


                            String id = studentDetails.getStudentID();
                            String name =studentDetails.getName();
                            String program =studentDetails.getProgram();

                            builder.append("Student ID :"+id +"\n"+
                                    "Student Name :"+name +"\n"+
                                    "Student Program :"+program+"\n\n");

                        }
                        text_view_data.setText(builder.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateData(View view){

        final Map<String, Object> details = new HashMap<>();
        details.put(KEY_STUDENT_ID,edtID.getText().toString());
        details.put(KEY_NAME,edtName.getText().toString());
        details.put(KEY_PROGRAM,edtProgram.getText().toString());

        readStudentData.whereEqualTo(KEY_STUDENT_ID, edtID.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            readStudentData.document(document.getId()).update(details);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void deleteNote(View view){

        readStudentData.whereEqualTo(KEY_STUDENT_ID, edtID.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                readStudentData.document(document.getId()).delete();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}