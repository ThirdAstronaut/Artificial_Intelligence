import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Algorithm {


    public static List<Route> select(List<Route> passedRoutes, int popSize, int tournament) {

        List<Route> parents = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            int randTournament = -1;
            List<Route> sample = new ArrayList<>();
            for (int j = 0; j < tournament; j++) {
                int random = ThreadLocalRandom.current().nextInt(0, popSize);
                if (random != randTournament) {
                    try {
                        sample.add(passedRoutes.get(random).clone());
                        sample.get(j).getStolenItems().calcCost(sample.get(j).getCities());
                        randTournament = random;

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int newRand = (random + new Random().nextInt(popSize)) % popSize;
                        sample.add(passedRoutes.get(newRand).clone());
                        sample.get(j).getStolenItems().calcCost(sample.get(j).getCities());
                        randTournament = newRand;

                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }


            Route best = sample.get(0);

            for (int j = 1; j < tournament; j++) {
                if (sample.get(j).getStolenItems().getFitness() > best.getStolenItems().getFitness()) {

                    best = sample.get(j);
                }
            }

            try {
                parents.add(best.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return parents;

    }


    public static void mutateRoute(Route route, int dimension) {
        int a = new Random().nextInt(dimension);
        int b = new Random().nextInt(dimension);
        swap(route, a, b);
        route.getStolenItems().calcCost(route.getCities());
    }

    private static void swap(Route route, int a, int b) {
        int old = route.getCities().get(a);
        route.getCities().set(a, route.getCities().get(b));
        route.getCities().set(b, old);
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
        }
        return routes;
    }

    /**
     * ROUTE WITH SHUFFLED CITIES PATH AND CALCULATED DISTANCE
     */
    public static Route generateRandomRoute() {
        Route route = new Route();
        for (int i = 1; i < Main.getCities().size() + 1; i++)
            route.getCities().set(i - 1, i);
        Collections.shuffle(route.getCities());
        stealItems(route);
        return route;
    }

    public static void stealItems(Route route) {

        for (int i = 0; i < route.getCities().size(); i++) {
            route.getStolenItems().calcCost(route.getCities());
        }
    }

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
        route.getStolenItems().calcCost(route.getCities());
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


    public static int roulette(List<Route> cities) {
        double sum = 0;
        for (int i = 0; i < cities.size(); i++) {
            sum += cities.get(i).getStolenItems().getFitness();
        }
        double rand = new Random().nextDouble() * sum;
        for (int i = 0; i < cities.size(); i++) {
            rand += (-1) * cities.get(i).getStolenItems().getFitness();
            if (rand > 0) return i;
        }
        return cities.size() - 1;
    }
}
