package fasko.wifilogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LoggingActivity extends AppCompatActivity {
    private WifiManager wifiManager;
    int currentScanIteration = 0;
    int totalScanCount;
    String fileHeader;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    //todo Implement multiple scans, come up with better file naming convention
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "scan" +fileName+ ".txt");
            while(totalScanCount > currentScanIteration){
                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    //If it's a blank file append the header to the top
                    if (file.length() == 0) {
                        fileWriter.append(fileHeader);
                    }
                    fileWriter.append("\n---Scan" + ++currentScanIteration + "---");
                    Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                        @Override
                        public int compare(ScanResult lhs, ScanResult rhs) {
                            return (Integer.compare(rhs.level, lhs.level));
                        }
                    };
                    List<ScanResult> results = wifiManager.getScanResults();
                    Collections.sort(results, comparator);

                    for (ScanResult scanResult : results) {
                        if(scanResult.SSID.equals("4csuuseonly"))
                            fileWriter.append("\nSSID: " + scanResult.SSID + "\nBSSID: " + scanResult.BSSID + "\ndB: " + scanResult.level + "\n");
                    }
                } catch (IOException e) {
                    //handle exception
                }
                onClick(getWindow().getDecorView().getRootView());
            }
            currentScanIteration = 0;
            totalScanCount =0;
        }
    };

    //Fetch logging details, scan, write to file
    //Internal Storage > Android > Data > fasko.wifilogger > files > Documents
    public void onClick(View view){

        EditText scanCountET = findViewById(R.id.ScanCountField);
        totalScanCount =  + Integer.parseInt(0 + scanCountET.getText().toString()); //Avoid string format issue when empty
        if (totalScanCount <= 0){
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
        fileHeader = (locationDataString + " | " + timestamp + " | " +totalScanCount);
        fileName = locationDataString;
        scanWifi();
        }

    public void scanWifi()  {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan(); //Goes to onReceive
        Toast.makeText(this, "Scanning Wifi "+currentScanIteration + "/" +totalScanCount, Toast.LENGTH_SHORT).show();
    }
}
