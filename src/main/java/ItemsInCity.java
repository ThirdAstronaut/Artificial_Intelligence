import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ItemsInCity {

    private static HashMap<Integer, List<Item>> itemsMap;

    public static HashMap<Integer, List<Item>> getItems() {
        return itemsMap;
    }

    public static void fillItems(List<Item> allItems, List<Node> cities) {
        HashMap<Integer, List<Item>> tmp = new HashMap<>();   //cityNum, item id, item ratio
        for (Node n : cities) {

            // log.error(""+n.getId());

            tmp.put(n.getId(), new ArrayList<Item>());

            for (Item item : allItems) {

                if (item.getCityNum() == n.getId())

                    tmp.get(n.getId()).add(item);
            }
            for (Item i : tmp.get(n.getId())) {

                allItems.remove(i);
            }
            Collections.sort(tmp.get(n.getId()), Comparator.comparing(Item::getRatio));
        }
        itemsMap = tmp;
    }


}
