public class Drone {
    //Precisa de um Socket que envia os dados coletados ao servidor, que ainda nao sei direito como sera implementado.
    private double AtmosphericPressure; //Pressão Atmosferica
    private double Radiation; //Radiação Solar
    private double Temperature; //Temperatura
    private double Moisture; //Umidade

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Construtores
    public Drone(){}
    public Drone(double atmosphericPressure, double radiation, double temperature, double moisture){
        this.AtmosphericPressure = atmosphericPressure;
        this.Radiation = radiation;
        this.Temperature = temperature;
        this.Moisture = moisture;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Envio de dados usando comunicação entre servidores



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Getters e Setters
    public double getAtmosphericPressure() {
        return AtmosphericPressure;
    }

    public void setAtmosphericPressure(double atmosphericPressure) {
        AtmosphericPressure = atmosphericPressure;
    }

    public double getRadiation() {
        return Radiation;
    }

    public void setRadiation(double radiation) {
        Radiation = radiation;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getMoisture() {
        return Moisture;
    }

    public void setMoisture(double moisture) {
        Moisture = moisture;
    }
}
