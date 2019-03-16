import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class Distance {

    private static HashMap<Integer, HashMap<Integer, Double>> distances;

    public static HashMap<Integer, HashMap<Integer, Double>> getDistances() {
        return distances;
    }

    public static void fillDistances(List<Node> cities) {
        HashMap<Integer, HashMap<Integer, Double>> tmp = new HashMap<>();
        for (Node n : cities) {
            tmp.put(n.getId(), new HashMap<>());
            for (Node node : cities) {
                tmp.get(n.getId()).put(node.getId(), n.getDistance(node));
            }
        }
        distances = tmp;
    }

}
