package com.example.mqttproyectolab_121;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTT{

    //class attributes
    private MqttAndroidClient ClientMQTT;
    private MqttConnectOptions Options;

    private TextView TV1;

    private Button BT1;

    private Switch SW1;

    private String ClientId;
    private String Topic;
    private final String UserName = "admin";
    private final String Password = "root";
    private final String AppName = "iot.com";
    private final String TAG = MainActivity.class.getName();
    private final String BROKER_URL = "tcp://192.168.43.117:1883";

    //constructors methods
    public MQTT(String topic, Context context, TextView textview){
        Topic = topic;
        TV1 = textview;
        ClientId = UserName + "@" + AppName;
        Options = new MqttConnectOptions();
        //Options.setUserName(UserName);
        //Options.setPassword(Password.toCharArray());
        //Options.setCleanSession(false);
        Options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        ClientMQTT = new MqttAndroidClient(context, BROKER_URL, ClientId);
        ClientMQTT.setCallback(new MQTT.MQTTCallback(BROKER_URL, ClientId, Topic, TV1));
    }

    //callback class to receive message
    public class MQTTCallback implements MqttCallback {
        //class attributes
        final private String TAG = "yes";
        private String BROKER_URL;
        private String deviceId;
        private String TOPIC;
        private TextView TV;
        private Switch SW;

        //constructors methods
        public MQTTCallback(String BROKER_URL, String deviceId, String topic, Switch sw) {
            this.BROKER_URL = BROKER_URL;
            this.deviceId = deviceId;
            this.TOPIC = topic;
            this.SW = sw;
        }
        public MQTTCallback(String BROKER_URL, String deviceId, String topic, TextView textView) {
            this.BROKER_URL = BROKER_URL;
            this.deviceId = deviceId;
            this.TOPIC = topic;
            this.TV = textView;
        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String TOPIC, MqttMessage mqttMessage) throws Exception {
            /*text view*/
            TV.setText(mqttMessage.toString());
            /*switch*/
            if(TOPIC.matches("casa/sala")){
                String State = new String(mqttMessage.getPayload());
                if(State.matches("ON")){
                    SW.setChecked(true);
                }else{
                    SW.setChecked(false);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }

    //method to connect
    public void connect(){
        try {
            ClientMQTT.connect(Options);
            Log.w("Mqtt", "Connect to: " + BROKER_URL);
        } catch (MqttException e) {
            Log.w("Mqtt", "Failed to connect to: " + BROKER_URL);
            //Connect();
        }
        /*MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setUserName(UserName);
        //mqttConnectOptions.setPassword(Password.toCharArray());

        try {

            ClientMQTT.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    ClientMQTT.setBufferOpts(disconnectedBufferOptions);
                    Subscribe(Topic);
                    Log.w("Mqtt", "Connect to: " + BROKER_URL);
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + BROKER_URL + exception.toString());
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }*/
    }

    //events
    public void EventsPublish(TextView tv){
        //tv.setText(mqttMessage.toString());
    }
    public void Events(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MqttMessage message = new MqttMessage(("shipping").getBytes());
                    ClientMQTT.publish(Topic, message);
                }catch (MqttException e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void EventsPublish(Switch sw){
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("ON").getBytes());
                        ClientMQTT.publish(Topic, message);
                    }catch (MqttException e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        MqttMessage message = new MqttMessage(("OFF").getBytes());
                        ClientMQTT.publish(Topic, message);
                    }catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //method to subscribe
    public void Subscribe(String tp){
        /*try {
            ClientMQTT.subscribe(this.Topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }*/
        /*try {
            ClientMQTT.subscribe(tp, 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!" + tp);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptions subscribing");
            ex.printStackTrace();
        }*/
        /*try {
            ClientMQTT.connect(Options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        ClientMQTT.subscribe(tp, 1, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.w("Mqtt", "Subscribed!" + tp);
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.w("Mqtt", "Subscribed fail!");
                            }
                        });

                    } catch (MqttException ex) {
                        System.err.println("Exceptions subscribing");
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }*/
        try {
            IMqttToken subToken = ClientMQTT.subscribe(Topic, 1);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed!");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //method to publish
    /*public void publishToDevice(String message) throws MqttException {
        byte[] EncodedPayload = new byte[0];
        try {
            EncodedPayload = message.getBytes("UTF-8");
            MqttMessage Message = new MqttMessage(EncodedPayload);
            ClientMQTT.publish(Topic, Message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }*/

    //get y set
    public String setTopic() { return Topic; }
}



