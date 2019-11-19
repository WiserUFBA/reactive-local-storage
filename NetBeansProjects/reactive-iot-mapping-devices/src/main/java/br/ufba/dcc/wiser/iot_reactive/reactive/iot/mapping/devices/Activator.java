package br.ufba.dcc.wiser.iot_reactive.reactive.iot.mapping.devices;

import br.ufba.dcc.wiser.soft_iot.mapping_devices.controller.ControllerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        try{
            
         ControllerImpl contr = new ControllerImpl();

         contr.start(context);
         
        }catch(Exception c){
            c.printStackTrace();
        }

// TODO add activation code here
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
