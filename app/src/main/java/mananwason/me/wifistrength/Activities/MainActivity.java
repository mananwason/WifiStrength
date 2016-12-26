package mananwason.me.wifistrength.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.OutputStream;

import mananwason.me.mewlibrary.implementations.WifiStateRecord;
import mananwason.me.mewlibrary.model.SensorReadingEvent;
import mananwason.me.wifistrength.R;

public class MainActivity extends AppCompatActivity {
    public static Bus eventBus;
    public static View focus;
    private Snackbar snackbar;
    private static OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        outputStream = new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus = view;
                new WifiStateRecord().start(MainActivity.this, 1000, "ABC", "ABC", outputStream);
                snackbar = Snackbar.make(view, "ABC", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void newReadingReceived(SensorReadingEvent sensorReading) {
        Log.d("TAG", sensorReading.getReading() + " " + sensorReading.getSensorName());
        snackbar.setText(sensorReading.getReading() + "");
    }
}
