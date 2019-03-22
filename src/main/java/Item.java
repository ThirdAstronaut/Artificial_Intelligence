import lombok.ToString;

@ToString
public class Item implements Comparable {

    private int id;
    private int profit;
    private int weight;
    private int cityNum;
    private double ratio;

    public Item(int id, int profit, int weight, int cityNum) {
        this.id = id;
        this.profit = profit;
        this.weight = weight;
        this.cityNum = cityNum;
        this.ratio = this.profit / this.weight;
    }

    public Item(String[] params) {
        this.id = Integer.valueOf(params[0]);
        this.profit = Integer.valueOf(params[1]);
        this.weight = Integer.valueOf(params[2]);
        this.cityNum = Integer.valueOf(params[3]);
        this.ratio = this.profit / this.weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public int compareTo(Object o) {
        return (int) (this.getRatio() - ((Item) o).getRatio());
    }
}
