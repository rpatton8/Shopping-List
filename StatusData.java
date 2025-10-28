package ryan.android.shopping;

import java.util.HashMap;
import java.util.Map;

public class StatusData {

    private final Map<String, Status> statusMap;

    StatusData () {
        statusMap = new HashMap<>();
    }

    public Map<String, Status> getStatusMap() {
        return statusMap;
    }

    public void readItemStatus(String itemName, String status, String checked) {
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            switch(status) {
                case "instock":
                    thisStatus.setAsInStock();
                case "needed":
                    thisStatus.setAsNeeded();
                case "paused":
                    thisStatus.setAsPaused();
            }
            switch(checked) {
                case "checked":
                    thisStatus.setAsChecked();
                case "unchecked":
                    thisStatus.setAsUnchecked();
            }
        } else {
            Status newStatus = new Status(itemName, status, checked);
            statusMap.put(itemName, newStatus);
        }
    }
}