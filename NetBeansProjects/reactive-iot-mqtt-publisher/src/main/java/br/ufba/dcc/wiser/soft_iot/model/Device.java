package br.ufba.dcc.wiser.soft_iot.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;



public class Device {
	private String id;
	private double latitude;
	private double longitude;
	@JsonIgnoreProperties("device")
	private List<Sensor> sensors;
	
	public Device(){
		
	}
	
	public Sensor getSensorbySensorId(String sensorId){
		for(Sensor sensor : sensors){
			if(sensor.getId().contentEquals(sensorId))
				return sensor;
		}
		return null;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public List<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}
	
	
}
