/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.soft_iot.mapping_devices.controller;

import br.ufba.dcc.wiser.iotreactive.model.Device;
import java.util.List;

/**
 *
 * @author cleberlira
 */
public interface Controller {
    List<Device> getListDevices();
	
    Device getDeviceById(String deviceId);
	
}
