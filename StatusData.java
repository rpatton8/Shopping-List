package ryan.android.shopping;

import java.util.HashMap;

class StatusData {

    private HashMap<String, Status> statusMap;

    StatusData () {
        statusMap = new HashMap<>();
    }

    HashMap<String, Status> getStatusMap() {
        return statusMap;
    }

    void readStatus(String itemName, String status, String checked) {
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            if (status.equals("instock")) {
                thisStatus.setAsInStock();
            } else if (status.equals("needed")) {
                thisStatus.setAsNeeded();
            } else if (status.equals("paused")) {
                thisStatus.setAsPaused();
            }
            if (checked.equals("checked")) {
                thisStatus.setAsChecked();
            } else if (checked.equals("unchecked")) {
                thisStatus.setAsUnchecked();
            }
        } else {
            Status newStatus = new Status(itemName, status, checked);
            statusMap.put(itemName, newStatus);
        }
    }
}