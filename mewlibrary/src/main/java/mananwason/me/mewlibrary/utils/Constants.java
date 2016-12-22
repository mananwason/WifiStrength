package mananwason.me.mewlibrary.utils;

import android.app.Application;

/**
 * Created by garvitab on 18-09-2015.
 */
public class Constants extends Application {

    //*********************time delays****************************
    public static final int DELAY_QUERY_PROCESSING = 60 * 1000;        //2 minutes delay to start the sensor data collection
    public static final int TIME_BETWEEN_LOCATION_UPDATES = 30 * 1000;    //5 seconds
    public static final long DISTANCE_BETWEEN_LOCATION_UPDATES = 500;    //500 mts
    public static final long TIME_BTW_RES_UPDATES = 30 * 1000;    //30 seconds


    //*********************routing keys***************************
    public static final String QUERY_ROUTING_KEY = "queries";
    public static final String RESOURCE_ROUTING_KEY = "resources";
//	public static final String SENSOR_DATA_ROUTING_KEY="data";

    //*********************RabbitMQ*******************************
    public static final String BROADCAST_ACTION = "recruitment.iiitd.edu.amqpIntent.BROADCAST";
    public static final String AMQP_SUBSCRIBED_MESSAGE = "recruitment.iiitd.edu.amqpIntent.SUBSCRIBE";

    //****************Application specific************************
    public static final String TAG = "MEW";
    public static final String REQUESTED_SENSOR_NAME = "sensor_name";
    public static final String REQUESTER_ID = "requester_id";
    public static String DEVICE_ID = "";

    //********************sensing*********************************
    public static final int SENSOR_SERVICE_START_ID = 5001;
    public static final int SENSOR_SERVICE_STOP_ID = 5002;
    public static final int SERVICE_START_ID = 1357;
    public static final int SERVICE_STOP_ID = 2468;
    public static final String SENSING_ACTION = "recruitment.iiitd.edu.subscription.DATACOLLECTION";
    public static final String PROCESS_DATA_REQUEST = "recruitment.iiitd.edu.sensing.action.PROCESS";
    public static final String START_DATA_REQUEST = "recruitment.iiitd.edu.sensing.action.START";
    public static final String STOP_DATA_REQUEST = "recruitment.iiitd.edu.sensing.action.STOP";
    public static final String START_BT_SCAN = "recruitment.iiitd.edu.sensing.action.BT_SCAN_START";
    public static final String STOP_BT_SCAN = "recruitment.iiitd.edu.sensing.action.BT_SCAN_STOP";
    public static final String QUERY_NO = "queryNo";

    //********************applicationConstants*********************************
    public static final int NUMBER_OF_LEVELS = 5;
    public static final String FILE_BASE_PATH = "/Mew/DataCollection/";

    public enum SENSORS {
        ACCELEROMETER("accelerometer"),
        WIFI("wifi"),
        GPS("gps"),
        MIC("microphone"),
        NETWORK("network"),
        BLUETOOTH("bluetooth");

        private String value;

        SENSORS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SENSOR_NAME {
        ACCELEROMETER(1),
        WiFi(2),
        GPS(3),
        MICROPHONE(4),
        NETWORK(5),
        BLUETOOTH(6);

        private int value;

        private SENSOR_NAME(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum MESSAGE_TYPE {
        INFO(1),
        PROVIDER(2),
        LVNGLISTENER(3),
        LVNGPROVIDER(4),
        ENDSERVICING(5),
        QUERYSERVICED(6),
        NOPROCESSING(7),
        DATA(8);

        private int value;

        private MESSAGE_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }
}
