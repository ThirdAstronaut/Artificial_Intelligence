import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
    static List<String[]> data = new ArrayList<>();

    public static void main(String... args) throws CloneNotSupportedException {
        readData("src\\main\\resources\\easy_4.ttp");
        Distance.fillDistances(cities);
        ItemsInCity.fillItems(items, cities);

        int popSize = 100;
        int tournament = 5;
        int generations = 100;
        double mutationAge = 0.1;
        double prob = 0.7;
        List<Route> first = new ArrayList<>();
        for(Route r : Algorithm.generateRandomPopulation(popSize)){
            Route tmp = r.clone();
            tmp.calcDistance();
            Algorithm.stealItems(tmp);
            first.add(tmp);
        }

//        List<Route> first1000 = new ArrayList<>();
  //      first1000 = first.stream().map(Route::new).collect(Collectors.toList());

    //    Route singleBest = Algorithm.findShortestDistanceRoute(first1000);
      //  Route randomBest = Algorithm.findShortestDistanceRoute(first1000);


    for(Integer i : ItemsInCity.getItems().keySet()){
            log.error( ItemsInCity.getItems().get(i).toString());

    }


        for (int i = 0; i < generations; i++) {
           /* for(Route r : first){
                System.out.println(r.getCities().toString() + "PRZED");
            }*/

            List<Route> parents = new ArrayList<>();
            for (Route r : Algorithm.select(first,popSize,  tournament)) {
                Route tmp = r.clone();
                tmp.calcDistance();
                Algorithm.stealItems(tmp);
                parents.add(tmp);
            }

           /* List<Route> parents = new ArrayList<>();
           */// List second1000 = new ArrayList<>();
            //second1000.addAll(first1000);
           // parents.addAll(Algorithm.select(second1000, popSize, tournament));
//           List<Route> parents = Algorithm.select(first, popSize, tournament).stream().map(Route::new).collect(Collectors.toList());


            /*for(Route r : parents){
                System.out.println(r.getCities().toString() + "PO" + parents.size());
            }*/
            //log.error(""+Algorithm.findShortestDistance(parents));
            // log.error("Most precious knapsack" + Algorithm.findMostPreciousKnapsack(parents));

            /* TEST ROULETTE */
           /* List<Route> parents = new ArrayList<>();
            for (int j = 0; j < first1000.size(); j++) {
                 parents.add(first1000.get(Algorithm.roulette(first1000)));

            }*/
            /* TEST*/


            //             first1000 = Algorithm.crossover(parents, prob);

            /*TEST*/
            // Collections.shuffle(parents);
            List<Route> offspring = new ArrayList<>();


            for (int j = 0; j < parents.size(); j += 2) {
                Route route1 = (Route) parents.get(j).clone();
                Route route2 = (Route) parents.get(j + 1).clone();
                     if (new Random().nextDouble() < prob) {

                    offspring.add((Route) new Algorithm().crossCX(new Route(route1), new Route(route2)));
                    offspring.add((Route)new Algorithm().crossCX(new Route(route2), new Route(route1)));

                //                            log.error(++counter+" "+offspring.get(j).getCities().toString() + " off0");
                //                          log.error(++counter+" "+offspring.get(j+1).getCities().toString() + " off1");
               } else {
                    offspring.add(parents.get(j));
                    offspring.add(parents.get(j + 1));

                    //         log.error(offspring.get(i+1).getCities().toString() + " off1");

                }

            }

            for(Route r : offspring){
                System.out.println(r.getCities().toString() + "KRZYZOWANIE NIE DZIALA" + parents.size());
            }

            //log.error("" + Algorithm.findShortestDistance(offspring));
            /*TEST*/


            for (int j = 0; j < offspring.size(); j++) {
                if (new Random().nextDouble() < mutationAge) {
                    Algorithm.mutateRoute((Route) offspring.get(j).clone(), dimension);
                    //log.error("mutation" );

                    //calcPopulationDistances(first1000);
                }
            }
//            first = offspring.stream().map(Route::new).collect(Collectors.toList());
            first = new ArrayList<>(offspring);
            for(Route r : first){
                Algorithm.stealItems(r);
            }
/*first1000 = new ArrayList<>();
            first1000.addAll((Collection<? extends Route>) ((ArrayList<Route>) offspring).clone());*/
            //first1000 = (List<Route>) ((ArrayList<Route>) offspring).clone();

            //   setRoutesArray(first1000, offspring);
//            first1000 = offspring;
            //
            Collections.shuffle(first);
                     saveData(i, Algorithm.findShortestDistance(first), Algorithm.findAvgDistance(first), Algorithm.findLongestDistance(first));
             }
        generateCsv();
        }


    private static void saveData(int i, double singelBest, double avgDistance, double longestDistance) {
        data.add(new String[]{String.valueOf(i), String.valueOf(singelBest), String.valueOf(avgDistance), String.valueOf(longestDistance)});
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
            while ((str = in.readLine()) != null && counter < 9) {
                String[] tmp = str.split(":");
                params[counter++] = tmp[tmp.length - 1].trim();
            }
            int dims = Integer.valueOf(params[2]);
            while ((str = in.readLine()) != null && dims > 0) { /*&& Files.lines(Paths.get(fileName)).count() > 10 && Files.lines(Paths.get(fileName)).count() < 10 + dimension*/
                String[] tmp = str.split(":");
                cities.add(new Node(str.split("\t")));
                dims--;
            }
            //in.readLine();
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
        File file = new File("log.csv");

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // create a List which contains String array
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
