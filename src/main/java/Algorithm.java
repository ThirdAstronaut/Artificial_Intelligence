import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//TODO nowa generacja mutacja, krzyżowanie, wybór
//TODO fitness = 1/distance, normalize fitness, sum = 100% fitness = fitness/sum
@Slf4j
public class Algorithm {

    /*public static List<Double> calcFitness(List<Route> population) {
        double startVal = Double.MAX_VALUE;
        Route route = null;
        List<Double> fitness = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            double dist = population.get(i).getDistance();
            if (dist < startVal) {
                startVal = dist;
                route = population.get(i);
            }

            fitness.add(1 / (Math.pow(dist, 8) + 1));
        }
        return fitness;
    }*/

    /*public static List<Double> normalizeFitness(List<Double> fitness) {
        List<Double> normalized = new ArrayList<>();
        double sum = 0;
        for (int i = 0; i < fitness.size(); i++) {
            sum += fitness.get(i);
        }
        for (int i = 0; i < fitness.size(); i++) {
            normalized.add(fitness.get(i) / sum);
        }
        return normalized;

    }*/


    //TODO zapis X najlepszych
    public static double findShortestDistance(List<Route> routes) {
        double min = Double.MAX_VALUE;
        int id = 0;
        for (Route r : routes) {
            if (r.getDistance() < min) {
                min = r.getDistance();
                //Main.saveData(r.getCities().toArray());
                System.out.println(r.getDistance() +"  " +id++);
            }
        }
        return min;
    }




    public static List<Route> select(List<Route> passedRoutes, int popSize, int tournament)  {

        List<Route> parents = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            int randTournament = -1;
            List<Route> sample = new ArrayList<>();
            for (int j = 0; j < tournament; j++) {
                int random = ThreadLocalRandom.current().nextInt(0, popSize);
                if(random != randTournament) {
                    try {
                    //    System.out.println(random);
                        sample.add((Route) passedRoutes.get(random).clone());
                        sample.get(j).getStolenItems().calcCost(sample.get(j).getCities());
                        randTournament = random;

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int newRand = (random + new Random().nextInt(popSize)) % popSize;
                        sample.add((Route) passedRoutes.get(newRand).clone());
                     //   System.out.println(newRand);
                        sample.get(j).getStolenItems().calcCost(sample.get(j).getCities());
                        randTournament = newRand;

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
              //  sample.get(j).getStolenItems().calcCost(sample.get(j).getCities());
           // log.error(sample.get(j).getStolenItems().getFitness() + "");
            }


            Route best = null;
                best = (Route) sample.get(0);

            for (int j = 1; j < tournament; j++) {
                if (sample.get(j).getStolenItems().getFitness()
 > best.getStolenItems().getFitness()) {

                        best = sample.get(j);

//sample.forEach(k -> System.out.println(k.getCities().toString()));
//log.error("zmieniam");
                }
        //        log.error(sample.get(j).getStolenItems().getFitness() + "");
            }

            try {
                parents.add((Route) best.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return parents;

    }



    public static double findLongestDistance(List<Route> routes) {
        double max = Double.MIN_VALUE;
        int id = 0;
        for (Route r : routes) {
            if (r.getDistance() > max) {
                max = r.getDistance();
                System.out.println(r.getDistance() +"  LONGEST" +id++);

                //Main.saveData(r.getCities().toArray());
            }
        }
        return max;
    }

    public static double findMostPreciousKnapsack(List<Route> routes) {
        double max = Double.MIN_VALUE;
        for (Route r : routes) {
            if (r.getStolenItems().calcKnapsackValue() > max) {
                max = r.getStolenItems().calcKnapsackValue();
                //Main.saveData(r.getCities().toArray());
            }
        }
        return max;
    }

    public static double findWorstFitness(List<Route> routes) {
        double max = Double.MAX_VALUE;
        int id = 0;
        for (Route r : routes) {
            if (r.getStolenItems().getFitness() < max) {
                max = r.getStolenItems().getFitness();
                //    System.out.println(r.getDistance() +"  LONGEST" +id++);

                //Main.saveData(r.getCities().toArray());
            }
        }
        return max;
    }

    public static double findAvgFitness(List<Route> routes) {
        double sum = 0;
        for (Route r : routes) {
            sum += r.getStolenItems().getFitness();
        }
        return sum / routes.size();
    }

    public static double findBestFitness(List<Route> routes) {
        double max = Double.MAX_VALUE;
        int id = 0;
        for (Route r : routes) {
            if (r.getStolenItems().getFitness() < max) {
                max = r.getStolenItems().getFitness();
                //    System.out.println(r.getDistance() +"  LONGEST" +id++);

                //Main.saveData(r.getCities().toArray());
            }
        }
        return max;
    }

    public static double findAvgDistance(List<Route> routes) {
        double sum = 0;
        for (Route r : routes) {
            sum += r.getDistance();
        }
        return sum / routes.size();
    }

    public static Route findShortestDistanceRoute(List<Route> routes) {
        double min = Double.MAX_VALUE;
        Route route = null;
        for (Route r : routes) {
            if (r.getDistance() < min) {
                min = r.getDistance();
                route = r;
            }
        }
        //   System.out.println(route.getCities());
        return route;
    }


    public static void mutateRoute(Route route, int dimension) {
      //  Route route1 = new Route(route);
        int a = new Random().nextInt(dimension);
        int b = new Random().nextInt(dimension);
        swap(route, a, b);
        route.getStolenItems().calcCost(route.getCities());
        }

/*
    public static void mutateRoutes(List<Route> routes, int dimension) {
        for (int i = 0; i < routes.size(); i++) {
            int a = new Random().nextInt(dimension);
            int b = new Random().nextInt(dimension);
            if (a > b) {
                swap(routes.get(i), a, b);
                routes.get(i).calcDistance();
                stealItems(routes.get(i));
            }
        }
    }
*/
    private static void swap(Route route, int a, int b) {
        int old = route.getCities().get(a);
        route.getCities().set(a, route.getCities().get(b));
        route.getCities().set(b, old);
    }

    /*public static List<Route> mutateRoutes(List<Route> routes) {
        List<Route> newRoutes = new ArrayList<>();
        newRoutes.addAll(routes);
        for (int i = 0; i < routes.size(); i++) {
            int a = new Random().nextInt(10);
            int b = new Random().nextInt(10);
         //   if (a > b) {
                    swap(newRoutes.get(i), a, b);
                    newRoutes.get(i).calcDistance();
           // }
        }
        return newRoutes;
    }*/


    private static Route cutParentsAtIndex(Route route1, Route route2, int cutPlace) {
        Route route = new Route();
        for (int i = 0; i < cutPlace; i++) {
            route.getCities().add(i, route1.getCities().get(i));
        }
        for (int i = cutPlace; i < route1.getCities().size(); i++) {
            if (!(route.getCities().contains(route2.getCities().get(i))))
                route.getCities().add(i, route2.getCities().get(i));
            else
                route.getCities().add(i, route1.getCities().get(i));
        }
        route.calcDistance();
       // stealItems(route);
        return route;
    }

    public static Route findBestRouteFromPopulation(List<Route> routes) {
        double min = Double.MAX_VALUE;
        Route route = new Route();
        for (Route r : routes) {
            if (r.getDistance() < min) {
                min = r.getDistance();
                route = r;
            }
        }
        return route;
    }


    public static double getDistanceBetweenTwoCities(int idOne, int idTwo) {
        return Distance.getDistances().get(idOne).get(idTwo);
    }

    /**
     * RETURNS LIST OF RANDOMLY GENERATED ROUTES
     */
    public static List<Route> generateRandomPopulation(int size) {
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                routes.add(generateRandomRoute().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            //    log.error(routes.get(i).getCities().toString()+"");
        }
        return routes;
    }

    /**
     * ROUTE WITH SHUFFLED CITIES PATH AND CALCULATED DISTANCE
     */
    public static Route generateRandomRoute() {
       /* List<Integer> citiesId = new ArrayList<>();
        Route route = new Route();
        for(int i = 0; i < cities.size(); i++)
            citiesId.add(i);
        for(int i = 0; i < cities.size(); i++){
            int id = (int)(Math.random() * cities.size() + 1);
            route.getCities().add(id);
            citiesId.remove(id);
        }*/
        Route route = new Route();
        for (int i = 1; i < Main.getCities().size() + 1; i++)
            route.getCities().set(i - 1, i);
        Collections.shuffle(route.getCities());
 //       route.calcDistance();
        stealItems(route);
  //      route.getStolenItems().calcCost(route.getCities());
     //   route.getStolenItems().calcFitness();
        return route;
    }

    public static void stealItems(Route route) {

        for (int i = 0; i < route.getCities().size(); i++) {

      //      route.getStolenItems().stealItemFromCity(route.getCities().get(i), route.getCities().get( (i + 1) /*% route.getCities().size()*/));
        route.getStolenItems().calcCost(route.getCities());
        }
    }


    public static boolean isElementInList(List<Integer> list, int element) {
        return list.contains(element);

    }

    /*private void pmx(Individual first, Individual second) {
        Node[] parent1 = first.route;
        Node[] parent2 = second.route;

        int startIndex = 3;

        Node[] crossedFirstParent = new Node[parent1.length];
        System.arraycopy(parent1, 0, crossedFirstParent, 0, parent1.length);
        Node[] crossedSecondParent = new Node[parent2.length];
        System.arraycopy(parent2, 0, crossedSecondParent, 0, parent2.length);

        for(int i = 0; i < 4; i++){
            swap(parent2, crossedFirstParent, startIndex + i);
            swap(parent1, crossedSecondParent, startIndex + i);
        }

        first.route = crossedFirstParent;
        second.route = crossedSecondParent;
    }
*/


    public static Route crossCX(Route route1, Route route2) {
        Route route = new Route();
        int index = 0;
        while (!element_already_inOffspring(route.getCities(), route2.getCities().get(index))) {
            route.getCities().set(index, route1.getCities().get(index));
            int position = getPosition_ofSecondParentElement_infirstParent(route1.getCities(), route2.getCities().get(index));
            route.getCities().set(position, route2.getCities().get(index));
            index = position;
        }

        for (int offspring_index = 0; offspring_index < route.getCities().size(); offspring_index++) {
            if (route.getCities().get(offspring_index) == -1) {
                route.getCities().set(offspring_index, route2.getCities().get(offspring_index));
            }
        }
        //route.calcDistance();
        route.getStolenItems().calcCost(route.getCities());
        //route.getStolenItems().calcFitness();
      //  route.getStolenItems().calcCost(route.getCities());

      //   log.error("corssCX route fitness: "+ route.getStolenItems().getFitness());
        return route;
    }

    private static int getPosition_ofSecondParentElement_infirstParent(List<Integer> cities, Integer integer) {
        int position = 0;
        for (int index = 0; index < cities.size(); index++) {
            if (cities.get(index).equals(integer)) {
                position = index;
                break;
            }
        }
        return position;
    }

    private static boolean element_already_inOffspring(List<Integer> cities, Integer integer) {
        for (int index = 0; index < cities.size(); index++) {
            if (cities.get(index).equals(integer)) {
                return true;
            }
        }
        return false;
    }

    public static List<Route> crossover(List<Route> routes, double probability) {
        List<Route> offspring = new ArrayList<>();
        for (int i = 0; i < routes.size(); i += 2) {
            Route route1 = routes.get(i);
            Route route2 = routes.get(i + 1);
            if (/*-1+*/ new Random().nextDouble() < probability) {
                int cutPlace = /*new Random().nextInt(route1.getCities().size());*/ routes.get(1).getCities().size() / 2;
                offspring.add(crossCX(route1, route2));
                offspring.add((crossCX(route2, route1)));
                //              log.error(offspring.get(i).getCities().toString() + " off0");
                //             log.error(offspring.get(i+1).getCities().toString() + " off1");
            } else {
                offspring.add(Algorithm.generateRandomRoute());
                offspring.add(Algorithm.generateRandomRoute());

                //         log.error(offspring.get(i+1).getCities().toString() + " off1");

            }
        }

        return offspring;
    }

    public static int roulette(List<Route> cities) {
        double sum = 0;
        for (int i = 0; i < cities.size(); i++) {
            sum += cities.get(i).getDistance();
        }
        double rand = new Random().nextDouble() * sum;
        for (int i = 0; i < cities.size(); i++) {
            rand -= cities.get(i).getDistance();
            if (rand < 0) return i;
        }
        return cities.size() - 1;
    }
}
