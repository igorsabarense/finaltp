package igor_553251.finaltp;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_steps;
    TextView tv_calories;
    Sensor mStepDetectorSensor;
    SensorManager sensorManager;
    boolean running = false;
    Button btn_reset , btn_history;
    int pace = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_steps = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Steps");
                myRef.setValue(pace);
                myRef = database.getReference("Calories");
                myRef.setValue( tv_calories.getText());

                pace = 0;
                tv_steps.setText("0");
                tv_calories.setText("0");
                Toast.makeText(MainActivity.this, "Steps/Calories reseted and values saved!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        btn_history = (Button) findViewById(R.id.btn_history);
        btn_history.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent2;
                intent2 = new Intent(view.getContext(), dba.class);
                startActivity(intent2);

                return true;
            }

        });
        tv_calories = (TextView) findViewById(R.id.tv_calories);

    }

    @Override
    protected void onResume(){
        super.onResume();
        //ao entrar na app ele escuta o sensor, passando a activity, o sensor e o tempo de intervalo
        sensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    protected void onStop() {
        super.onStop();
        //ao sair do app ele para de escutar o sensor passando a actovity e o sensor
        sensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            pace++;
            tv_steps.setText("" + pace);

            double calories = pace*0.04;
            tv_calories.setText(String.format("%.2f", (calories)) + " Kcal ");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
