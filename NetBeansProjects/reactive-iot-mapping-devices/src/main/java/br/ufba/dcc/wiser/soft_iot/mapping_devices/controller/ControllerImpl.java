package br.ufba.dcc.wiser.soft_iot.mapping_devices.controller;

import br.ufba.dcc.wiser.iotreactive.model.Device;
import br.ufba.dcc.wiser.iotreactive.model.Sensor;
import br.ufba.dcc.wiser.soft_iot.mapping_devices.controller.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.spi.cluster.ClusterManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ControllerImpl extends AbstractVerticle implements Controller {

    private List<Device> listDevices;
    private String strJsonDevices;
    private boolean debugModeValue;
    private String enderecoBarramento;

    public void start(BundleContext context) {
        System.out.println("Starting mapping of connected devices...");
       
        this.strJsonDevices = "[{id:libeliumSmartWater01,latitude:53.290411,longitude:-9.074406,sensors:[{id:TemperatureSensorPt-1000,type:WaterMeter,collection_time:30000,publishing_time: 60000}]}]";
        
        loadConnectedDevices(this.strJsonDevices, context);
    }

    private void loadConnectedDevices(String strDevices, BundleContext context) {
        List<Device> listDevices = new ArrayList<Device>();
        try {
            System.out.println("JSON load:");
            JSONArray jsonArrayDevices = new JSONArray(strDevices);
            for (int i = 0; i < jsonArrayDevices.length(); i++) {
                JSONObject jsonDevice = jsonArrayDevices.getJSONObject(i);
                ObjectMapper mapper = new ObjectMapper();
                Device device = mapper.readValue(jsonDevice.toString(), Device.class);
                listDevices.add(device);

                List<Sensor> listSensors = new ArrayList<Sensor>();
                JSONArray jsonArraySensors = jsonDevice.getJSONArray("sensors");
                for (int j = 0; j < jsonArraySensors.length(); j++) {
                    JSONObject jsonSensor = jsonArraySensors.getJSONObject(j);
                    Sensor sensor = mapper.readValue(jsonSensor.toString(), Sensor.class);
                    listSensors.add(sensor);
                }
                device.setSensors(listSensors);
            }
        } catch (JsonParseException | JsonMappingException e) {
            System.out.println("Verify the correct format of 'DevicesConnected' property in configuration file.");
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception v){
            v.printStackTrace();
        }
        
        this.listDevices = listDevices;

        addIoTMessageChannel(this.listDevices,context);

    }

    public void addIoTMessageChannel(List<Device> listDevices, BundleContext context) {

        try{
        Gson json = new Gson();
        String devices = json.toJson(listDevices);

        Vertx vetx = Vertx.vertx();

        final ServiceReference eventBusRef = context.getServiceReference("io.vertx.core.eventbus.EventBus");

        EventBus eventBus = (EventBus) context.getService(eventBusRef);

        vetx.setPeriodic(Util.timepublish, id -> {

            eventBus.publish(Util.enderecoBarramento, devices);
            System.out.println("We now have a clustered event bus: " + eventBus);
        });
        
        }catch(Exception v){
            v.printStackTrace();
        }

        //     EventBus eventBus = Vertx.vertx().eventBus();
//        try {
//
//            printlnDebug("We now have a clustered event bus: " + eventBus);
//
//            Gson json = new Gson();
//
//            String JSONObject = json.toJson(listDevices);
//
//            printlnDebug("enderecoBarramento " + enderecoBarramento);
//
//            eventBus.send(enderecoBarramento, "teste");
//
//        } catch (Exception c) {
//            printlnDebug("erro " + c.getMessage());
//            c.printStackTrace();
//
//        }
    }

    @Override
    public Device getDeviceById(String deviceId) {
        for (Device device : listDevices) {
            if (device.getId().contentEquals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    private void printlnDebug(String str) {
        if (debugModeValue) {
            System.out.println(str);
        }
    }

    public void setStrJsonDevices(String strJsonDevices) {
        this.strJsonDevices = strJsonDevices;
    }

    @Override
    public List<Device> getListDevices() {
        return listDevices;
    }

    public void setDebugModeValue(boolean debugModeValue) {
        this.debugModeValue = debugModeValue;
    }

    /**
     * @return the enderecoBarramento
     */
    public String getEnderecoBarramento() {
        return enderecoBarramento;
    }

    /**
     * @param enderecoBarramento the enderecoBarramento to set
     */
    public void setEnderecoBarramento(String enderecoBarramento) {
        this.enderecoBarramento = enderecoBarramento;
    }

}
