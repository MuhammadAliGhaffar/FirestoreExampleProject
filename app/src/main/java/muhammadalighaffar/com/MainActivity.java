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

    private EditText edtTitle,edtDescription;
    private TextView text_view_data;

    //Creating final keys for cloud firestore
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";

    //Database Reference
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Notebook").document("My first note");
    CollectionReference mynote = db.collection("Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTitle=findViewById(R.id.edtTitle);
        edtDescription=findViewById(R.id.edtDescription);
        text_view_data=findViewById(R.id.text_view_data);
    }
    public void saveNote(View view){
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE,edtTitle.getText().toString());
        note.put(KEY_DESCRIPTION,edtDescription.getText().toString());

        db.collection("Notebook")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed "+e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });




        /*noteRef.set(note)
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
        /*noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String description =documentSnapshot.getString(KEY_DESCRIPTION);

                            //Map<String,Object> note =documentSnapshot.getData();
                            text_view_data.setText("Title :"+title+"\n"+"Description :"+description);

                        }else{
                            Toast.makeText(MainActivity.this, "Document does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error :"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
                mynote
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("TAG", document.getId() + " => " + document.getData());

                                String title = document.getString(KEY_TITLE);
                                String description =document.getString(KEY_DESCRIPTION);

                                text_view_data.append("Title :"+title+" Description :"+description +"\n");

                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}