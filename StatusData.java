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

    public void readStatus(String itemName, String status, String checked) {
        Status newStatus = new Status(itemName, status, checked);
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            if (status.equals("instock")) {
                thisStatus.setAsInStock();
            } else if (status.equals("needed")) {
                thisStatus.setAsNeeded();
            } else if (status.equals("paused")) {
                thisStatus.setAsPaused();
            }
        } else {
            statusMap.put(itemName, newStatus);
        }

    }

}