import java.util.ArrayList;
import java.util.List;

public class StolenItems implements Cloneable{

    private double fitness;
    private int profit;
    private int time;
    private double maxKnapsackWeight;
    private double minVelocity;
    private double maxVelocity;
    private double currentVelocity;
    private double currentKnapsackWeight;
    private List<Item> items;


    public StolenItems() {
        this.maxKnapsackWeight = Main.getCapacityOfKnapsack();
        this.minVelocity = Main.getMinSpeed();
        this.maxVelocity = Main.getMaxSpeed();
        items = new ArrayList<>();
        currentVelocity = calcVelocity();
        currentKnapsackWeight = calcKnapsackWeight();
    }

    @Override
    public StolenItems clone() throws CloneNotSupportedException {
        return (StolenItems)super.clone();
    }

    public StolenItems(StolenItems stolenItems){
        this.maxKnapsackWeight = stolenItems.maxKnapsackWeight;
        this.minVelocity = stolenItems.minVelocity;
        this.maxVelocity = stolenItems.maxVelocity;
        items = new ArrayList<>();
        items.addAll(stolenItems.items);
        currentVelocity = calcVelocity();
        currentKnapsackWeight = calcKnapsackWeight();
    }


    public double getMaxKnapsackWeight() {
        return maxKnapsackWeight;
    }

    public void setMaxKnapsackWeight(double maxKnapsackWeight) {
        this.maxKnapsackWeight = maxKnapsackWeight;
    }

    public double getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    private double calcVelocity() {
        return maxVelocity - currentKnapsackWeight * ((maxVelocity - minVelocity) / maxKnapsackWeight);
    }

    private double calcKnapsackWeight() {
        return items.stream().mapToDouble(Item::getWeight).sum();
    }

    public void stealItemFromCity(Integer cityId, Integer nextCity) {
        boolean added = false;

        for (Item i : ItemsInCity.getItems().get(cityId)) {   //wszystkie przedmioty w odwiedzonym miescie posortowane wg ratio
           if(!added && i.getWeight() + currentKnapsackWeight <= maxKnapsackWeight) {
                   items.add(i);
                   profit += i.getProfit();
                   currentKnapsackWeight += i.getWeight();
                   currentVelocity = calcVelocity();
                   added = true;
                   if(currentVelocity < minVelocity)
                       currentVelocity = minVelocity;
           }
            time += Algorithm.getDistanceBetweenTwoCities(cityId, nextCity) / currentVelocity;
        }
        fitness = profit - time;

    }

    public double getFitness() {
        return fitness;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }

    public double getCurrentKnapsackWeight() {
        return currentKnapsackWeight;
    }

    public List<Item> getItems() {
        return items;
    }

    public double calcKnapsackValue(){
        return items.stream().mapToDouble(Item::getProfit).sum();
    }
}
/* for (int i = 0; i < ItemsInCity.getItems().get(node.getId()).size(); i++) {
            if(!added)
            if (ItemsInCity.getItems().get(node.getId()).get(i).getWeight() + currentKnapsackWeight <= maxKnapsackWeight) {

                items.add((Item) ItemsInCity.getItems().get(node.getId()));


            }

       */