import java.util.ArrayList;
import java.util.List;

public class StolenItems  implements Cloneable{

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
        StolenItems clone = (StolenItems) super.clone();
        clone.maxKnapsackWeight = this.maxKnapsackWeight;
        clone.minVelocity = this.minVelocity;
        clone.maxVelocity = this.maxVelocity;
        clone.items = new ArrayList<>(this.items);
        clone.currentVelocity = this.currentVelocity;
        clone.currentKnapsackWeight = this.currentKnapsackWeight;
        clone.fitness = this.fitness;
        clone.time = this.time;
        clone.profit = this.profit;
    return clone;
    }

    public StolenItems(StolenItems stolenItems){
        this.maxKnapsackWeight = stolenItems.maxKnapsackWeight;
        this.minVelocity = stolenItems.minVelocity;
        this.maxVelocity = stolenItems.maxVelocity;
        items = new ArrayList<>(stolenItems.items);
        currentKnapsackWeight = calcKnapsackWeight();
        currentVelocity = calcVelocity();
        this.fitness = stolenItems.fitness;
        this.time = stolenItems.time;
        this.profit = stolenItems.profit;
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

   /* public void stealItemFromCity(Integer cityId, Integer nextCity) {
        boolean added = false;
         //for (Item i : ItemsInCity.getItems().get(cityId)) {   //wszystkie przedmioty w odwiedzonym miescie posortowane wg ratio
        for (int i = 0; i <  ItemsInCity.getItems().get(cityId).size(); i++) {
            double  weight = ItemsInCity.getItems().get(cityId).get(i).getWeight();
            Item it = ItemsInCity.getItems().get(cityId).get(i);
            if(!added &&   weight + currentKnapsackWeight <= maxKnapsackWeight) {
                items.add(it);
                profit += it.getProfit();
                currentKnapsackWeight += weight;
                currentVelocity = calcVelocity();
                added = true;
                if(currentVelocity < minVelocity)
                    currentVelocity = minVelocity;
  //              calcFitness();
            }
            time += (Algorithm.getDistanceBetweenTwoCities(cityId, nextCity) / currentVelocity);
        }

    }
*/
    public void calcCost(List<Integer> cities){
        items = new ArrayList<>();
        currentKnapsackWeight = 0;
        currentVelocity = Main.getMaxSpeed();
        time = 0;
        profit = 0;
        fitness = 0;
        double distance;
        for (int i = 0; i < cities.size(); i++) {         //city num, item id, ratio
            boolean added = false;
            int cityId = cities.get(i);
            if (ItemsInCity.getItems().get(cityId) != null) {
                for (int j = 0; j < ItemsInCity.getItems().get(cityId).size(); j++) {
                    Item it = ItemsInCity.getItems().get(cityId).get(j);
                    if (it.getWeight() + currentKnapsackWeight <= maxKnapsackWeight && !added) {
                        items.add(it);
                        currentKnapsackWeight += it.getWeight();
                        profit += it.getProfit();
                        added = true;
                        if (currentVelocity < minVelocity) {
                            currentVelocity = minVelocity;
                        }
                    }

                }
            }

            distance = Algorithm.getDistanceBetweenTwoCities(cities.get(i), cities.get((i + 1) % cities.size() ));
            currentVelocity = maxVelocity - currentKnapsackWeight * ((maxVelocity - minVelocity) / maxKnapsackWeight);
                time += distance / currentVelocity;
        }
       /* distance = Algorithm.getDistanceBetweenTwoCities(cities.get(cities.size()-1), cities.get(0));
        currentVelocity = maxVelocity - currentKnapsackWeight * ((maxVelocity - minVelocity) / maxKnapsackWeight);
        time += distance / currentVelocity;
*/
        //calcFitness();
        fitness = profit - time;

    }


    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setCurrentVelocity(double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void setCurrentKnapsackWeight(double currentKnapsackWeight) {
        this.currentKnapsackWeight = currentKnapsackWeight;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double calcFitness(){
        fitness = profit - time;
        return fitness;
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