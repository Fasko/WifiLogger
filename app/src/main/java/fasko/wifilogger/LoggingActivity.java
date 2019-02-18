package fasko.wifilogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.os.Environment;
import android.widget.Toast;
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
        EditText scanCountET = findViewById(R.id.ScanCountField);
        int scanCount =  + Integer.parseInt(0 + scanCountET.getText().toString()); //Avoid string format issue when empty


        if (scanCount <= 0){
            Toast.makeText(this, "Scan Count must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText xCoordET = findViewById(R.id.XCoordField);
        EditText yCoordET = findViewById(R.id.YCoordField);
        EditText zCoordET = findViewById(R.id.ZCoordField);
        String xCoord = xCoordET.getText().toString();
        String yCoord = yCoordET.getText().toString();
        String zCoord = zCoordET.getText().toString();
        EditText testOrTrainingET = findViewById(R.id.pointDataField);
        String testOrTraining = testOrTrainingET.getText().toString();

        // Enforces all or nothing XYZ rule
        int inputCounter = 0;
        if (!xCoord.isEmpty()){
            inputCounter++;
        }
        if (!yCoord.isEmpty()){
            inputCounter++;
        }
        if (!zCoord.isEmpty()) {
            inputCounter++;
        }

        if (inputCounter == 1 || inputCounter == 2){
            Toast.makeText(this,"Must enter all XYZ coordinates if using XYZ", Toast.LENGTH_SHORT).show();
            return;
        }

        //xyzFlag is true when all inputs is input
        //testOrTrainingFlag is true when training or testing data is input
        boolean testOrTrainingFlag = !(testOrTraining.isEmpty());
        boolean xyzFlag = !(xCoord.isEmpty() || yCoord.isEmpty() || zCoord.isEmpty());

        //Checks if there is no input
        if (!testOrTrainingFlag && !xyzFlag){
            Toast.makeText(this,"Must enter XYZ or Test/Training Data", Toast.LENGTH_SHORT).show();
            return;
        }

        //Build location-based input
        String locationDataString = null;
        if (xyzFlag && testOrTrainingFlag){
            locationDataString = xCoord + "," +yCoord + "," +zCoord + " | " + testOrTraining;
        } else if (xyzFlag){
            locationDataString = xCoord + "," +yCoord + "," +zCoord;
        } else if (testOrTrainingFlag){
            locationDataString = testOrTraining;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));

        //todo write wifi information
        File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir,"exampleFile.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(locationDataString + " | " + timestamp + " | " +scanCount);
        }catch (IOException e){
            //handle exception
        }
    }
}
