interface Command {
    void execute();
    void undo();
}

class SmartLight {
    public void switchOn() {
        System.out.println("[SmartLight] Light is now ON");
    }

    public void switchOff() {
        System.out.println("[SmartLight] Light is now OFF");
    }
}

class ClimateControl {
    private int currentTemp = 21;
    private int previousTemp;

    public void updateTemperature(int temp) {
        previousTemp = currentTemp;
        currentTemp = temp;
        System.out.println("[ClimateControl] Temperature set to " + temp + "°C");
    }

    public void restorePreviousTemperature() {
        currentTemp = previousTemp;
        System.out.println("[ClimateControl] Restored temperature to " + currentTemp + "°C");
    }
}

class LightOnCommand implements Command {
    private SmartLight light;

    public LightOnCommand(SmartLight light) {
        this.light = light;
    }

    public void execute() {
        light.switchOn();
    }

    public void undo() {
        light.switchOff();
    }
}

class TemperatureSetCommand implements Command {
    private ClimateControl climateControl;
    private int targetTemp;

    public TemperatureSetCommand(ClimateControl climateControl, int targetTemp) {
        this.climateControl = climateControl;
        this.targetTemp = targetTemp;
    }

    public void execute() {
        climateControl.updateTemperature(targetTemp);
    }

    public void undo() {
        climateControl.restorePreviousTemperature();
    }
}

import java.util.HashMap;
import java.util.Map;

class HomeAssistantRemote {
    private Map<String, Command> commandSlots = new HashMap<>();
    private Command lastExecuted;

    public void assignCommand(String key, Command command) {
        commandSlots.put(key, command);
    }

    public void press(String key) {
        Command command = commandSlots.get(key);
        if (command != null) {
            command.execute();
            lastExecuted = command;
        } else {
            System.out.println("[Remote] No command assigned to: " + key);
        }
    }

    public void pressUndo() {
        if (lastExecuted != null) {
            System.out.println("[Remote] Undoing last action...");
            lastExecuted.undo();
        } else {
            System.out.println("[Remote] Nothing to undo.");
        }
    }
}

public class SmartHomeCommandDemo {
    public static void main(String[] args) {
        SmartLight light = new SmartLight();
        ClimateControl climate = new ClimateControl();

        HomeAssistantRemote remote = new HomeAssistantRemote();

        remote.assignCommand("light_on", new LightOnCommand(light));
        remote.assignCommand("set_temp", new TemperatureSetCommand(climate, 24));

        remote.press("light_on");
        remote.press("set_temp");
        remote.pressUndo();
    }
}
