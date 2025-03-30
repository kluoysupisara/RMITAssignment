import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Map<String, Appliance> devices = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("Welcome to Smart Home Automation System!");
        System.out.println("=".repeat(70));
        Scanner input = new Scanner(System.in);

        while(true){
            showMenu();
            int choice = input.nextInt();
            input.nextLine(); //Assume newline character

            switch (choice){
                case 1:
                    System.out.println("*** Showing All Devices ***");
                    for (Appliance device : devices.values()) {
                        device.showStatus();
                    }
                    break;
                case 2:
                    if (devices.isEmpty()) {
                        System.out.println("You do not have any devices now.");
                    }
                    else{
                        turnOnDevice(input);
                    }
                    break;
                case 3:
                    if (devices.isEmpty()) {
                        System.out.println("You do not have any devices now.");
                    }
                    else{
                        turnOffDevice(input);
                    }
                    break;
                case 4:
                    adjustDevice(input);
                    break;
                case 5:
                    addDevice(input);
                    break;
                case 6:
                    removeDevice(input);
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }


        }

    }

    private static void showMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Show All Devices");
        System.out.println("2. Turn ON Device");
        System.out.println("3. Turn OFF Device");
        System.out.println("4. Adjust Device");
        System.out.println("5. Add a Device");
        System.out.println("6. Remove a Device");
        System.out.println("7. Exit");
        System.out.print("Please select: ");
    }

    private static void turnOnDevice(Scanner input){
        System.out.print("\nWhich device do you want to turn ON: ");
        String deviceOn = input.nextLine().trim().toLowerCase();
        //System.out.println("deviceOn: " + deviceOn);
        Appliance device = devices.get(deviceOn);
        if (device == null) {
            System.out.println("No device found.");
        }else {
            device.turnOn();
        }
    }

    private static void turnOffDevice(Scanner input){
        System.out.print("\nWhich device do you want to turn OFF: ");
        String deviceOFF = input.nextLine().trim().toLowerCase();
        Appliance device = devices.get(deviceOFF);
        if (device == null) {
            System.out.println("No device found.");
        }else {
            device.turnOff();
        }
    }

    private static void adjustDevice(Scanner input){
        System.out.print("\nWhich device do you want to adjust: ");
        String deviceAdjust = input.nextLine().trim();
        Appliance device = devices.get(deviceAdjust);

        if (!device.status) {
            device.showStatus();
            System.out.print("Do you want to turn on Light (Y/N))?: ");
            String yOrN = input.nextLine().trim();
            if (yOrN.equalsIgnoreCase("Y") || yOrN.equalsIgnoreCase("YES")) {
                device.turnOn();
            } else {
                System.out.println("Adjusting Unsuccessful!!! ");
            }
        }
        if (device instanceof Light light) {
            System.out.print("Enter brightness level [1, 10]: ");
            int brightnessLevel = input.nextInt();
            light.setBrightness(brightnessLevel);
        } else if (device instanceof Fan fan) {
            System.out.print("Enter speed level [1, 5]: ");
            int speedLevel = input.nextInt();
            fan.setSpeed(speedLevel);
        }
    }
    private static void addDevice(Scanner input){
        System.out.print("Enter device type (Light/Fan): ");
        String type = input.nextLine().trim();

        System.out.print("Enter device name: ");
        String name = input.nextLine().trim();

        if (devices.containsKey(name.toLowerCase())) {
            System.out.println("The device cannot be added because it already exists in the system.");
            return;
        }

        Appliance newDevice;
        if (type.equalsIgnoreCase("Light")) {
            newDevice = new Light(name);
        } else if (type.equalsIgnoreCase("Fan")) {
            newDevice = new Fan(name);
        } else {
            System.out.println("Invalid device type! Please enter 'Light' or 'Fan'.");
            return;
        }

        devices.put(name.toLowerCase(), newDevice);
        System.out.println(name + " has been added to the system.");
    }

    private static void removeDevice(Scanner input) {
        System.out.print("Which device do you want to remove: ");
        String name = input.nextLine().trim().toLowerCase();

        if (devices.remove(name) != null) {
            System.out.println(name + " has been removed from the system.");
        } else {
            System.out.println("Sorry! This device does not exist in the system.");
        }
    }
}