public abstract class Appliance implements SmartDevice{
    protected String name;
    protected boolean status;

    public Appliance(String name) {
        this.name = name;
        this.status = false; // Default status is OFF
    }

    @Override
    public void turnOn() {
        if(!status){
            status = true;
            System.out.printf("%s is now ON", name);
        }else{
            System.out.printf("%s is already ON", name);
        }

    }

    @Override
    public void turnOff() {
        if(status){
            status = false;
            System.out.printf("%s is now OFF", name);
        }else{
            System.out.printf("%s is already OFF", name);
        }


    }

    @Override
    public void showStatus() {
        System.out.printf("%s : %s%n", name, status?"ON":"OFF");

    }
}
