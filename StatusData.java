package ryan.android.shopping;

import android.content.Context;
import java.util.HashMap;

class StatusData {

    private Context context;
    private HashMap<String, Status> statusMap;

    StatusData (Context context) {
        this.context = context;
        statusMap = new HashMap<>();
    }

    private Context getContext() {
        return context;
    }

    HashMap<String, Status> getStatusMap() {
        return statusMap;
    }

    void readStatus(String itemName, String status, String checked) {
        if (statusMap.containsKey(itemName)) {
            Status thisStatus = statusMap.get(itemName);
            if (status.equals(getContext().getString(R.string.instock))) {
                thisStatus.setAsInStock();
            } else if (status.equals(getContext().getString(R.string.needed))) {
                thisStatus.setAsNeeded();
            } else if (status.equals(getContext().getString(R.string.paused))) {
                thisStatus.setAsPaused();
            }
            if (checked.equals(getContext().getString(R.string.checked))) {
                thisStatus.setAsChecked();
            } else if (checked.equals(getContext().getString(R.string.unchecked))) {
                thisStatus.setAsUnchecked();
            }
        } else {
            Status newStatus = new Status(itemName, status, checked, context);
            statusMap.put(itemName, newStatus);
        }
    }
}