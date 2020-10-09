package com.example.mqttproyectolab_121;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    //global variables
    Mqtt mqtt;
    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);

        StartMQTT("home/room", tv1);
        StartMQTT("home/bathroom", tv2);
        /*MQTT m = new MQTT("home", getApplicationContext(), tv1);
        MQTT n = new MQTT("room", getApplicationContext(), tv2);
        m.Subscribe(m.setTopic());
        n.Subscribe(n.setTopic());*/
    }

    private void StartMQTT(String topic,final TextView TV){
        mqtt = new Mqtt(getApplicationContext(), topic);
        mqtt.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Toast.makeText(MainActivity.this, "MQTT connected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void connectionLost(Throwable throwable) {
                //Toast.makeText(MainActivity.this, "MQTT disconnect", Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                TV.setText(mqttMessage.toString());
                Toast.makeText(MainActivity.this, "Arrived", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
