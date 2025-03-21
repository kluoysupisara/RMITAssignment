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
                    fan.showStatus();
                    light.showStatus();
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
                case 5:
                    input.close();
                    return;
            }

        }

    }

    public static void showMenu(){
        System.out.println("\nChoose an option:");
        System.out.println("1. Show all devices");
        System.out.println("2. Turn ON device");
        System.out.println("3. Turn OFF device");
        System.out.println("4. Adjust Light brightness");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }
    }