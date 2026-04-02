package ryan.android.shopping;

import java.util.HashMap;
import java.util.Map;

class StatusData {

    private final Map<String, Status> statusMap;

    StatusData () {
        statusMap = new HashMap<>();
    }

    public Map<String, Status> getStatusMap() {
        return statusMap;
    }

    public void readStatus(String itemName, String status, String checked) {
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            switch (status) {
                case "instock":
                    thisStatus.setAsInStock();
                    break;
                case "needed":
                    thisStatus.setAsNeeded();
                    break;
                case "paused":
                    thisStatus.setAsPaused();
                    break;
            }
            switch (checked) {
                case "checked":
                    thisStatus.setAsChecked();
                    break;
                case "unchecked":
                    thisStatus.setAsUnchecked();
                    break;
            }
        } else {
            Status newStatus = new Status(itemName, status, checked);
            statusMap.put(itemName, newStatus);
        }
    }
}