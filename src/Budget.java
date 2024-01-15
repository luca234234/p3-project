import java.io.Serializable;

public class Budget implements Reportable, Serializable {
    private double limit;
    private String period;

    public Budget(double limit, String period) {
        this.limit = limit;
        this.period = period;
    }

    @Override
    public void generateReport() {
        System.out.println("Budget Report: Limit = " + limit + ", Period = " + period);
    }

    public double getLimit() {return limit;}
    public String getPeriod() {return period;}

}

