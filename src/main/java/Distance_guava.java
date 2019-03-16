import com.google.common.collect.RowSortedTable;
import com.google.common.collect.TreeBasedTable;

import java.util.List;

public class Distance_guava {

    public static RowSortedTable<Integer, Integer, Double> getDistance(List<Node> cities){
        RowSortedTable<Integer, Integer, Double> tmp = TreeBasedTable.create();
        for(int i = 0; i < cities.size(); i++){
            for(int j = 0; j < cities.size(); j++)
                tmp.put(cities.get(i).getId(), cities.get(j).getId(), cities.get(i).getDistance(cities.get(j)));
        }
return tmp;
    }

}
