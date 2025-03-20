package com.Server.Project;

import com.Server.Project.BackendServerApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
public class CommandController {

    private final BackendServerApplication backendServerApplication;

    public CommandController(BackendServerApplication backendServerApplication) {
        this.backendServerApplication = backendServerApplication;
    }

    @PostMapping("/{vehicleId}/{command}")
    public String sendCommand(@PathVariable String vehicleId, @PathVariable String command) {
        backendServerApplication.sendCommand(command);
        return "✅ Command Sent: " + command;
    }
}
