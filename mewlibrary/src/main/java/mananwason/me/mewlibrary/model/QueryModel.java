package mananwason.me.mewlibrary.model;

import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by apurv on 10/1/2015.
 */
public final class QueryModel implements BaseColumns, Serializable {

    private static final long serialVersionUID = 8692497235318058617L;
    private int _id;
    private String sensors;
    private long startTime , endTime;
    private String routingKey;
    private String queryNo;
    private int processed;
    private int deviceState;

    private int userActivity;

    public QueryModel(){

    }

   public QueryModel(String sensors, long startTime, long endTime, String routingKey, String queryNo,int userActivity, int deviceState){
        this.sensors = sensors;
        this.startTime = startTime;
        this.endTime = endTime;
        this.routingKey = routingKey;
        this.queryNo = queryNo;
        this.processed = 0;
        this.userActivity=userActivity;
        this.deviceState=deviceState;
    }

    public int getUserActivity() {return this.userActivity;}
    public void setUserActivity(int userActivity) { this.userActivity = userActivity; }

    public int getDeviceState() {return this.deviceState;}
    public void setDeviceState(int deviceState) { this.deviceState = deviceState; }

public void setId(int id){
        this._id = id;
    }
    public int getId(){
        return this._id;
    }

    public void setSensors(String sensors){
        this.sensors = sensors;
    }
    public String getSensors(){
        return this.sensors;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }
    public long getStartTime(){
        return  this.startTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
    }
    public long getEndTime(){
        return  this.endTime;
    }

    public void setRoutingKey(String routingKey){
        this.routingKey = routingKey;
    }
    public String getRoutingKey(){
        return this.routingKey;
    }

    public void setQueryNo(String queryNo){
        this.queryNo = queryNo;
    }
    public String getQueryNo(){
        return this.queryNo;
    }

    public void setProcessed(int processed){
        this.processed = processed;
    }
    public int getProcessed(){
        return  this.processed;
    }


}
