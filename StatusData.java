package ryan.android.shopping;

import java.util.HashMap;
import java.util.Map;

public class StatusData {

    private Map<String, Status> statusMap;

    public StatusData () {
        statusMap = new HashMap<>();
    }

    public Map<String, Status> getStatusMap() {
        return statusMap;
    }

    public void readItemStatus(String itemName, String isInStock, String isNeeded, String isPaused) {
        Status newStatus = new Status(itemName, isInStock, isNeeded, isPaused);
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            if (isInStock.equals("true")) {
                thisStatus.setAsInStock();
            } else if (isNeeded.equals("true")) {
                thisStatus.setAsNeeded();
            } else if (isPaused.equals("true")) {
                thisStatus.setAsPaused();
            }
        } else {
            statusMap.put(itemName, newStatus);
        }

    }

}
