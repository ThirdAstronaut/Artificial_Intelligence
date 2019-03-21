import com.opencsv.CSVWriter;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
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
    private static List<Item> connections = new ArrayList<>();
    private static int numOfConnections = dimension * (dimension - 1) / 2;
    static List<String[]> data = new ArrayList<String[]>();
    private static int counter = 0;
  //  private static List<Route> first1000;
    static double allBest;
    static double allWorst;
    static double allAvg;




    public static void main(String... args) {
        readData("src\\main\\resources\\hard_1.ttp");
        Distance.fillDistances(cities);
        ItemsInCity.fillItems(items, cities);
        /*
        for(Integer i : Distance.getDistances().keySet()){
            System.out.println(Distance.getDistances().get(i).keySet());
        }

        for(Integer i : Distance.getDistances().keySet()){
            System.out.println(Distance.getDistances().get(i).values());
        }*/




        int popSize = 100;
        int tournament = 5;
        int generations = 100;

        double mutationAge = 0.01;
        double prob = 0.;
        List<Route> first1000 = new ArrayList<>(Algorithm.generateRandomPopulation(popSize));
    Collections.shuffle(first1000);


        for (Route r : first1000) {
r.getStolenItems().calcCost(r.getCities());

        }

        /*for(Route r : first1000){
            System.out.println(r.getCities().toString() + " FIRST 1000");
        }*/
        //  List<Route> first1000 = new ArrayList<>();
        //first1000 = ();



     //   evaluate(first1000);


        //Route singelBest = Algorithm.findShortestDistanceRoute(first1000);
        //Route randomBest = Algorithm.findShortestDistanceRoute(first1000);
        // System.out.println(singelBest.getDistance() + "singleBest bez niczego");
        for (int i = 0; i < generations; i++) {

       /*     for (Route route1 : first1000)
                route1.getStolenItems().calcCost(route1.getCities());

       */

       List<Route> tmp = Algorithm.select(first1000, popSize, tournament);
       List<Route> parents = new ArrayList<>(tmp);

            for (Route r : parents) {
                r.getStolenItems().calcCost(r.getCities());

            }


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
            List<Route> offspring = new ArrayList<>();
            for (int j = 0; j < parents.size(); j += 2) {
                Route route2 = null;
                Route route1 = null;
                try {
                    route1 = (Route) parents.get(j).clone();
                    route2 = (Route) parents.get(j + 1).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                double random = ThreadLocalRandom.current().nextDouble(0, 1);
         //       System.out.println(random);
                if (  random < prob) {
                    try {
                        offspring.add((Route) Algorithm.crossCX(route1.clone(), Objects.requireNonNull(route2).clone()).clone());
                        offspring.add((Route) (Algorithm.crossCX(route2.clone(), route1.clone())).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    //                            log.error(++counter+" "+offspring.get(j).getCities().toString() + " off0");
                    //                          log.error(++counter+" "+offspring.get(j+1).getCities().toString() + " off1");
                } else {
                    try {
                        offspring.add((Route) parents.get(j).clone());
                        offspring.add((Route) parents.get(j + 1).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }


                    //         log.error(offspring.get(i+1).getCities().toString() + " off1");

                }
            }


            for (Route r : offspring) {
                r.getStolenItems().calcCost(r.getCities());

            }
    /*        for (Route r : offspring) {
                System.out.println(r.getCities().toString() + "PO");
            }
*/
            //log.error("" + Algorithm.findShortestDistance(offspring));
            /*TEST*/


            for (int j = 0; j < offspring.size(); j++) {
                if (new Random().nextDouble() < mutationAge) {
                    Algorithm.mutateRoute(offspring.get(j), dimension);
                    //log.error("mutation" );

                    //calcPopulationDistances(first1000);
                }
            }


            for (Route r : offspring) {
                r.getStolenItems().calcCost(r.getCities());

            }
first1000 = new ArrayList<>(offspring);
/*for (Route r : first1000){
    r.getStolenItems().calcCost(r.getCities());
}*/



            for (Route r : first1000) {
                r.getStolenItems().calcCost(r.getCities());

            }

        stats(first1000);

     //       System.out.println(new BigDecimal(Algorithm.findBestFitness(first1000)).toPlainString());
            //saveData(i, Algorithm.findShortestDistance(first1000), Algorithm.findAvgDistance(first1000), Algorithm.findLongestDistance(first1000));
            //saveData(i, Algorithm.findBestFitness(first1000), Algorithm.findAvgFitness(first1000), Algorithm.findWorstFitness(first1000));
            saveData(i, allBest, allAvg, allWorst);

        }
        generateCsv();
        System.out.println(allBest + " : BEST");
        System.out.println(allAvg + " : AVG");
        System.out.println(allWorst + " : WORST");


        //TODO krzyżownia, mutacja, selekcja na najlepszych

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

   /* private static void evaluate(List<Route> first1000) {
        for (Route r : first1000) {
            r.getStolenItems().calcFitness();
        }
    }
*/
    private static void saveData(int i, double singelBest, double avgDistance, double longestDistance) {
        data.add(new String[]{String.valueOf(i).replace(".", ","), String.valueOf(singelBest).replace(".", ","), String.valueOf(avgDistance).replace(".", ","), String.valueOf(longestDistance).replace(".", ",")});
    }


   /*
public static void testDistances(){
        System.out.println(Distance.getDistances().size() + "size");
        System.out.println(Distance.getDistances().get(1).keySet().size() + "size");
        System.out.println(Distance.getDistances().get(1).values().size() + "size");

}*/

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
            while ((str = in.readLine()) != null && dims > 0) { /*&& Files.lines(Paths.get(fileName)).count() > 10 && Files.lines(Paths.get(fileName)).count() < 10 + dimension*/
                String[] tmp = str.split(":");
                cities.add(new Node(str.split("\t")));
                dims--;
            }
            // in.readLine();
            //cities.forEach(System.out::println);

            int itemsNum = Integer.valueOf(params[3]);
            while ((str = in.readLine()) != null && itemsNum > 0) { /*&& Files.lines(Paths.get(fileName)).count() > 10 && Files.lines(Paths.get(fileName)).count() < 10 + dimension*/
                String[] tmp = str.split(":");
                items.add(new Item(str.split("\t")));
                itemsNum--;
            }

            //items.forEach(System.out::println);

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

    public static List<Node> getCities() {
        return cities;
    }

    public static void saveData(Object[] text) {
        try (PrintStream out = new PrintStream(new FileOutputStream("results.txt"))) {
            out.println(problemName);
            for (int i = 0; i < text.length; i++) {
                out.print(text[i] + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generateCsv() {
        File file = new File("HARD1-PROB-0_6-"+"-log.csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // create a List which contains String array
           /* for(String[] s : data)
                for(String string : s)
                    string.replace(".", ",");
*/
            writer.writeAll(data);

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getProblemName() {
        return problemName;
    }

    public static String getKnapsackDataType() {
        return knapsackDataType;
    }

    public static int getDimension() {
        return dimension;
    }

    public static int getNumOfItems() {
        return numOfItems;
    }

    public static double getCapacityOfKnapsack() {
        return capacityOfKnapsack;
    }

    public static double getMinSpeed() {
        return minSpeed;
    }

    public static double getMaxSpeed() {
        return maxSpeed;
    }

    public static double getRentingRatio() {
        return rentingRatio;
    }

    public static String getEdgeWeightType() {
        return edgeWeightType;
    }

    public static List<Item> getItems() {
        return items;
    }

    public static List<Item> getConnections() {
        return connections;
    }

    public static int getNumOfConnections() {
        return numOfConnections;
    }
}
