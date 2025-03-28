public class Light extends Appliance{
    private int brightness;

    public Light(String name) {
        super(name);
        this.brightness = 5; // Default brightness when turned ON
    }


    public void setBrightness(int brightness) {
        if(brightness >= 1 && brightness <= 10) {
            this.brightness = brightness;
            System.out.println("Light brightness set to: " + brightness);
        }else{
            System.out.println("Light brightness out of range. Please set 1 to 10.");
        }
    }

    @Override
    public void showStatus() {
        super.showStatus();
        if(status){
            //System.out.println("Brightness: " + brightness);
            displayScale("Brightness", brightness, 10);
        }
    }

    @Override
    public void turnOn(){
        super.turnOn();
        this.brightness = 5; // Reset when turn On again
    }
    @Override
    public void turnOff(){
        super.turnOff();
    }

    private void displayScale(String label, int value, int max) {
        int filledBlocks = (int) ((value / (double) max) * max); // Normalize to max blocks
        int emptyBlocks = max - filledBlocks;
        String bar = "[" + "█".repeat(filledBlocks) + "-".repeat(emptyBlocks) + "]";
        System.out.println(label + ": " + bar + " " + value + "/" + max);
    }
}

