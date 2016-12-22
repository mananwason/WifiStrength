package mananwason.me.mewlibrary.model;

/**
 * Created by Manan Wason on 22/12/16.
 */
public class SensorReadingEvent {
    private String sensorName;
    private int reading;


    public SensorReadingEvent(String sensorName, int reading) {
        this.sensorName = sensorName;
        this.reading = reading;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }
}
