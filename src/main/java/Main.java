import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Main {

    private static String problemName;
    private static String knapsackDataType;
    private static int dimension;
    private static int numOfItems;
    private static double capacityOfKnapsack;
    private static double minSpeed;
    private static double maxSpeed;
    private static double rentingRatio;
    private static String edgeWeightType;
    private static List<Node> cities = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    static List<String[]> data = new ArrayList<>();
    private static double allBest;
    private static double allWorst;
    private static double allAvg;

    private static final String FILE_NAME = "HARD_1-100-10-100-001-07-ROULETTE";


    public static void main(String... args) {
        readData("src\\main\\resources\\hard_1.ttp");
        Distance.fillDistances(cities);
        ItemsInCity.fillItems(items, cities);

//TODO KOMENT DLA PYTHONA
//saveData( "Id", "Best", "Avg", "Worst");


        int popSize = 100;
        int tournament = 10;
        int generations = 100;
        double mutation = 0.05;
        double px = 0.7;
        List<Route> first1000 = new ArrayList<>(Algorithm.generateRandomPopulation(popSize));

        /** EVALUATE */
        for (Route r : first1000) {
            r.getStolenItems().calcCost(r.getCities());

        }

        for (int i = 0; i < generations; i++) {

            /** SELECTION */
           // List<Route> tmp = Algorithm.select(first1000, popSize, tournament);
            List<Route> tmp = new ArrayList<>();
            for (int j = 0; j < first1000.size(); j++) {
                tmp.add(first1000.get(Algorithm.roulette(first1000)));
            }
            List<Route> parents = new ArrayList<>(tmp);

          /*  for (Route r : parents) {
                r.getStolenItems().calcCost(r.getCities());

            }
*/

            /* TEST ROULETTE */
           /* List<Route> parents = new ArrayList<>();
            for (int j = 0; j < first1000.size(); j++) {
                 parents.add(first1000.get(Algorithm.roulette(first1000)));

            }*/
            /* TEST*/


            //             first1000 = Algorithm.crossover(parents, prob);

            /*TEST*/
            // Collections.shuffle(parents);

   /*         for (Route r : parents) {
                System.out.println(r.getCities().toString() + "PRZED");
            }
*/

   /** CROSSOVER */
            List<Route> offspring = new ArrayList<>();
            for (int j = 0; j < parents.size(); j += 2) {
                Route route2 = null;
                Route route1 = null;
                try {
                    route1 = parents.get(j).clone();
                    route2 = parents.get(j + 1).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                double random = ThreadLocalRandom.current().nextDouble(0, 1);

                if (random < px) {
                    try {
                        offspring.add(Algorithm.crossCX(route1.clone(), Objects.requireNonNull(route2).clone()).clone());
                        offspring.add((Algorithm.crossCX(route2.clone(), route1.clone())).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        offspring.add(parents.get(j).clone());
                        offspring.add(parents.get(j + 1).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }


                }
            }

            /** MUTATION */
           for (Route anOffspring : offspring) {
                if (new Random().nextDouble() < mutation) {
                    Algorithm.mutateRoute(anOffspring, dimension);

                }
            }

            first1000 = new ArrayList<>(offspring);

            stats(first1000);

            saveData(i, allBest, allAvg, allWorst);

        }
        generateCsv();
        System.out.println(allBest + " : BEST");
        System.out.println(allAvg + " : AVG");
        System.out.println(allWorst + " : WORST");


        //TODO krzyżownia, mutacja, selekcja na najlepszych

    }

    private static void saveData(String i, String best, String avg, String worst) {
        data.add(new String[]{i, best, avg, worst});
    }

    private static void stats(List<Route> routes) {
        int bestId = 0;
        int worstId = 0;
        double avarage = 0;
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(bestId).getStolenItems().getFitness() < routes.get(i).getStolenItems().getFitness()) {
                bestId = i;
            } else if (routes.get(worstId).getStolenItems().getFitness() > routes.get(i).getStolenItems().getFitness()) {
                worstId = i;
            }
            avarage += routes.get(i).getStolenItems().getFitness();
        }
        allBest = routes.get(bestId).getStolenItems().getFitness();
        allWorst = routes.get(worstId).getStolenItems().getFitness();
        allAvg = avarage / routes.size();
    }

    private static void saveData(int i, double singelBest, double avgDistance, double longestDistance) {
        //TODO KOMENT DLA PYTHONA
        // data.add(new String[]{String.valueOf(i).replace(".", ","), String.valueOf(singelBest).replace(".", ","), String.valueOf(avgDistance).replace(".", ","), String.valueOf(longestDistance).replace(".", ",")});
        data.add(new String[]{String.valueOf(i), String.valueOf(singelBest), String.valueOf(avgDistance), String.valueOf(longestDistance)});
    }

    private static void readData(String fileName) {
        String[] params = new String[9];
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            int counter = 0;
            while ((str = in.readLine()) != null && counter < params.length) {
                String[] tmp = str.split(":");
                params[counter++] = tmp[tmp.length - 1].trim();
            }
            int dims = Integer.valueOf(params[2]);
            while ((str = in.readLine()) != null && dims > 0) {
                cities.add(new Node(str.split("\t")));
                dims--;
            }
            int itemsNum = Integer.valueOf(params[3]);
            while ((str = in.readLine()) != null && itemsNum > 0) {
                items.add(new Item(str.split("\t")));
                itemsNum--;
            }


            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assignParamsValues(params);
    }

    private static void assignParamsValues(String[] params) {
        problemName = params[0];
        knapsackDataType = params[1];
        dimension = Integer.valueOf(params[2]);
        numOfItems = Integer.valueOf(params[3]);
        capacityOfKnapsack = Double.valueOf(params[4]);
        minSpeed = Double.valueOf(params[5]);
        maxSpeed = Double.valueOf(params[6]);
        rentingRatio = Double.valueOf(params[7]);
        edgeWeightType = params[8];
    }

    static List<Node> getCities() {
        return cities;
    }


    private static void generateCsv() {
        File file = new File(FILE_NAME + "-p.csv");

        try {
            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static int getDimension() {
        return dimension;
    }

    static double getCapacityOfKnapsack() {
        return capacityOfKnapsack;
    }

    static double getMinSpeed() {
        return minSpeed;
    }

    static double getMaxSpeed() {
        return maxSpeed;
    }

    public static List<Item> getItems() {
        return items;
    }

}
