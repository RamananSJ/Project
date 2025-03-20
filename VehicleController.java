package com.Truck.Project;

import com.Truck.Project.OnboardVehicleApplication;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final OnboardVehicleApplication onboardVehicleApplication;

    public VehicleController(OnboardVehicleApplication onboardVehicleApplication) {
        this.onboardVehicleApplication = onboardVehicleApplication;
    }

    @GetMapping("/{vehicleId}/status")
    public String getStatus(@PathVariable String vehicleId) {
        return onboardVehicleApplication.getLatestStatus();
    }
}
