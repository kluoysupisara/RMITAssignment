public class Fan extends Appliance{
    private int speed;

    public Fan(String name) {
        super(name);
        this.speed = 1; // Default speed when turned ON
    }

    public void setSpeed(int speed){
        // allow speed [1,5]
        if(speed >= 0 && speed <= 5){
            this.speed = speed;
            System.out.println("Fan speed set to " + this.speed);
        }else {
            System.out.println("Fan speed out of range. Please set 1 to 5.");
        }

    }
    @Override
    public void showStatus() {
        super.showStatus();
        if(status){
            displayScale("Speed", speed, 5);
            //System.out.println("Speed: " + speed);
        }
    }

    @Override
    public void turnOn(){
        super.turnOn();
        this.speed = 1; // Reset when turn On again
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
