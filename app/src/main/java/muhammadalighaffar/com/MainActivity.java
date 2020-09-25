package muhammadalighaffar.com;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtTitle,edtDescription;

    //Creating final keys for cloud firestore
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";

    //Database Reference
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTitle=findViewById(R.id.edtTitle);
        edtDescription=findViewById(R.id.edtDescription);
    }
    public void saveNote(View view){
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE,edtTitle.getText().toString());
        note.put(KEY_DESCRIPTION,edtDescription.getText().toString());

        db.collection("Notebook").add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,"Note is saved",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error :"+e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });


    }
}