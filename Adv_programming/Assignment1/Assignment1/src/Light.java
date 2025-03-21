public class Light extends Appliance{
    private int brightness;

    public Light(String name) {
        super(name);
        this.brightness = 5; // Default brightness when turned ON
    }


    public void setBrightness(int brightness) {
        if(brightness >= 1 && brightness <= 10) {
            this.brightness = brightness;
        }

    }
}

