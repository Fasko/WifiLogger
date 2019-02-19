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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //if (!wifiManager.isWifiEnabled()) {
        //   Toast.makeText(this, "WiFi is disabled ... enabling now", Toast.LENGTH_LONG).show();
        //   wifiManager.setWifiEnabled(true);
        //}

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }
    public void doScanProcess(View view) {
        listView.setEnabled(false);
        scanWifi();
    }

    public void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            //Not all Android OSs returns an in order ScanResult, this sorts the list manually
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
                    arrayList.add("SSID: " + scanResult.SSID + "\nBSSID: " + scanResult.BSSID + "\ndB: " + scanResult.level);
            }
            adapter.notifyDataSetChanged();
            listView.setEnabled(true);
        }
    };

    public void goToLogging(View view) {
        Intent startNewActivity = new Intent(this, LoggingActivity.class);
        startActivity(startNewActivity);
    }
}