import org.w3c.dom.ls.LSOutput;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("Welcome to Smart Home Automation System!");
        System.out.println("=".repeat(70));
        Scanner input = new Scanner(System.in);
        Fan fan = new Fan("Fan");
        Light light = new Light("Light");
        while(true){
            showMenu();
            int choice = input.nextInt();
            input.nextLine(); //Assume newline character
            switch (choice){
                case 1:
                    System.out.println("*** Showing All Devices ***");
                    light.showStatus();
                    fan.showStatus();
                    break;
                case 2:
                    System.out.print("\nWhich device do you want to turn ON: ");
                    String deviceOn = input.nextLine().trim();
                    if (deviceOn.equalsIgnoreCase("Light")) {
                        light.turnOn();
                    } else if (deviceOn.equalsIgnoreCase("Fan")) {
                        fan.turnOn();
                    } else {
                        System.out.println("Invalid device name. Please enter 'Light' or 'Fan'.");
                    }
                    break;
                case 3:
                    System.out.print("\nWhich device do you want to turn OFF: ");
                    String deviceOFF = input.nextLine().trim();
                    if (deviceOFF.equalsIgnoreCase("Light")) {
                        light.turnOff();
                    } else if (deviceOFF.equalsIgnoreCase("Fan")) {
                        fan.turnOff();
                    } else {
                        System.out.println("Invalid device name. Please enter 'Light' or 'Fan'.");
                    }
                    break;
                case 4:
                    System.out.print("\nWhich device do you want to adjust: ");
                    String deviceAdjust = input.nextLine().trim();
                    if (deviceAdjust.equalsIgnoreCase("Light")) {
                        // light is off
                        if(!light.status) {
                            light.showStatus();
                            System.out.print("Do you want to turn on Light (Y/N))?: ");
                            String yOrN = input.nextLine().trim();
                            if(yesNo(yOrN)){
                                if(yOrN.equalsIgnoreCase("Y") || yOrN.equalsIgnoreCase("YES")) {
                                    light.turnOn();
                                }else if(yOrN.equalsIgnoreCase("N")|| yOrN.equalsIgnoreCase("NO")) {
                                    System.out.println("Adjusting Unsuccessful!!! ");
                                    break;
                                }
                            }
                        }
                        //after light turn on
                            System.out.print("Enter brightness level [1, 10]:");
                            int brightnessLevel = input.nextInt();
                            light.setBrightness(brightnessLevel);
                    }else if (deviceAdjust.equalsIgnoreCase("Fan")) {
                        if(!fan.status) {
                            fan.showStatus();
                            System.out.print("Do you want to turn on Fan (Y/N))?: ");
                            String yOrN = input.nextLine().trim();
                            if(yesNo(yOrN)){
                                if(yOrN.equalsIgnoreCase("Y") || yOrN.equalsIgnoreCase("YES")) {
                                    fan.turnOn();
                                }else if(yOrN.equalsIgnoreCase("N")|| yOrN.equalsIgnoreCase("NO")) {
                                    System.out.println("Adjusting Unsuccessful!!! ");
                                    break;
                                }
                            }
                        }
                        // after fan turn on
                        System.out.print("Enter speed level [1, 5]:");
                        int speedLevel = input.nextInt();
                        fan.setSpeed(speedLevel);
                    }
                    break;
                case 5:
                    input.close();
                    return;
            }

        }

    }

    public static void showMenu(){
        System.out.println("\nChoose an option:");
        System.out.println("1. Show all Devices");
        System.out.println("2. Turn ON Device");
        System.out.println("3. Turn OFF Device");
        System.out.println("4. Adjust Device");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }
    public static boolean yesNo(String message){
        if(message.equalsIgnoreCase("Y") || message.equalsIgnoreCase("YES")
                || message.equalsIgnoreCase("N") || message.equalsIgnoreCase("NO")) {
        return true;}
        return false;
    }
}