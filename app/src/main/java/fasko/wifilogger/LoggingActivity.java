package fasko.wifilogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.os.Environment;
import java.io.File;

public class LoggingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        //Create WiFi Log directory if not created
        //Internal Storage > Android > Data > fasko.wifilogger > files > Documents > WiFi_Logs
        File file = new File(getApplicationContext().getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), "WiFi_Logs");
        if (!file.mkdirs()) {
            System.out.println("Directory not created");
        }
    }
    //todo implementation of entering coordinates/filename and information logging.

    //Fetch logging details, scan, write to file
    public void onClick(View view) {
        EditText scanCount = findViewById(R.id.ScanCountField);
        String v1 = scanCount.getText().toString();

        EditText xCoord = findViewById(R.id.XCoordField);
        String v2 = xCoord.getText().toString();

        EditText yCoord = findViewById(R.id.YCoordField);
        String v3 = yCoord.getText().toString();

        EditText zCoord = findViewById(R.id.ZCoordField);
        String v4 = zCoord.getText().toString();

        EditText testOrTraining = findViewById(R.id.pointDataField);
        String v5 = testOrTraining.getText().toString();

        System.out.println(v1 + " " + v2 + " " + v3 + " " + v4 + " " + v5);
    }
}
