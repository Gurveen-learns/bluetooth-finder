package gurveen.com.bluetoothlocater;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView statusTextView;
    Button searchButton;

    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> all_address = new ArrayList<>();

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                statusTextView.setText("Search Finished");
                Log.d("result","Search Finish ");
                searchButton.setEnabled(true);
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("result","Device Found");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();

                if (!all_address.contains(address)){
                    all_address.add(address);
                    String device_info = "";
                    if (name == null || name.equals("")){
                       device_info = address;
                    }else {
                        device_info = name;
                    }

                    bluetoothDevices.add(device_info);

                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void searchClicked(View view){
        statusTextView.setText("Searching......");
        searchButton.setEnabled(false);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,bluetoothDevices);
        listView.setAdapter(arrayAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(broadcastReceiver,intentFilter);
    }
}
