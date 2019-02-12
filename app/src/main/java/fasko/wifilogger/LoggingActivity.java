package fasko.wifilogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);
    }

    //Fetch logging details, scan, write to file
    //Internal Storage > Android > Data > fasko.wifilogger > files > Documents
    public void onClick(View view) {
        //todo Validate inputs (must enter scan count AND (XYZ || Training/Testing Point))

        EditText scanCountET = findViewById(R.id.ScanCountField);
        int scanCount = Integer.parseInt(scanCountET.getText().toString());

        EditText xCoord = findViewById(R.id.XCoordField);
        EditText yCoord = findViewById(R.id.YCoordField);
        EditText zCoord = findViewById(R.id.ZCoordField);

        String XYZ = xCoord.getText().toString() + "," +yCoord.getText().toString()
                + "," +zCoord.getText().toString();

        EditText testOrTrainingET = findViewById(R.id.pointDataField);
        String testOrTraining = testOrTrainingET.getText().toString();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));

        //todo Write the basic information to top of file, write wifi information
        File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir,"exampleFile.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(XYZ + " | " + timestamp);
        }catch (IOException e){
            //handle exception
        }
    }
}
