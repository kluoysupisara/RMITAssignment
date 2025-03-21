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
        }

    }
}
