import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Route implements Cloneable,  Comparable<Route> {

   private List<Integer> cities;
   private double distance;
   private StolenItems stolenItems;
  // private double time;

    public Route() {
        cities = new ArrayList<>();
        for (int i = 0; i < Main.getDimension(); i++) {
            cities.add(-1);
        }
        stolenItems = new StolenItems();
    }

    public Route(Route route){
        this.cities = new ArrayList<>(route.getCities());
        this.distance = route.distance;
        this.stolenItems = new StolenItems(route.stolenItems);
    }
    public List<Integer> getCities() {
        return cities;
    }

    public void setCities(List<Integer> cities) {
        this.cities = cities;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void calcDistance(){
        int i = cities.get(0);
//        cities.forEach(System.out::print);
         for (int j = 1; j < cities.size(); j++) {
            distance += Algorithm.getDistanceBetweenTwoCities(i, cities.get(j));
            i = cities.get(j);
        }
        distance += Algorithm.getDistanceBetweenTwoCities(i, cities.get(0));
    }


/*
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }*/

    public StolenItems getStolenItems() {
        return stolenItems;
    }

    public void setStolenItems(StolenItems stolenItems) {
        this.stolenItems = stolenItems;
    }

    @Override
    protected Route clone() throws CloneNotSupportedException {
        Route route = (Route) super.clone();
        route.cities = new ArrayList<>(this.cities);
        route.stolenItems = new StolenItems(this.stolenItems);
        route.distance = this.distance;

        return (Route) route;

    }



    @Override
    public int compareTo(Route o) {
        return Double.compare(o.stolenItems.getFitness(), this.stolenItems.getFitness());
    }
}
