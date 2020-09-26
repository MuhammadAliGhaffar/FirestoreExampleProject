package muhammadalighaffar.com;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtID,edtName,edtProgram;
    private TextView text_view_data;

    //Creating final keys for cloud firestore
    private static final String KEY_ID="id";
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

    @Override
    protected void onStart() {
        super.onStart();
        readStudentData
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder builder = new StringBuilder();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("TAG", document.getId() + " => " + document.getData());

                                String id = document.getString(KEY_ID);
                                String name =document.getString(KEY_NAME);
                                String program =document.getString(KEY_PROGRAM);

                                builder.append("Student ID :"+id +"\n"+
                                        "Student Name :"+name +"\n"+
                                        "Student Program :"+program+"\n\n");

                            }
                            text_view_data.setText(builder.toString());

                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addDetails(View view){

        Map<String, Object> details = new HashMap<>();
        details.put(KEY_ID,edtID.getText().toString());
        details.put(KEY_NAME,edtName.getText().toString());
        details.put(KEY_PROGRAM,edtProgram.getText().toString());

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
                });

    }
    public void getRetrieveData(View view){
                readStudentData
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder builder = new StringBuilder();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("TAG", document.getId() + " => " + document.getData());

                                String id = document.getString(KEY_ID);
                                String name =document.getString(KEY_NAME);
                                String program =document.getString(KEY_PROGRAM);

                                builder.append("Student ID :"+id +"\n"+
                                                "Student Name :"+name +"\n"+
                                                "Student Program :"+program+"\n\n");

                            }
                            text_view_data.setText(builder.toString());

                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateData(View view){

        Map<String, Object> details = new HashMap<>();
        details.put(KEY_ID,edtID.getText().toString());
        details.put(KEY_NAME,edtName.getText().toString());
        details.put(KEY_PROGRAM,edtProgram.getText().toString());

        db.collection("Student").document(edtID.getText().toString()).update(details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Student details updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void deleteNote(View view){

        db.collection("Student").document(edtID.getText().toString()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Student details deleted", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
}