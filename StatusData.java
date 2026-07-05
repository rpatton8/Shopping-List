package ryan.android.shopping;

import android.content.Context;
import java.util.HashMap;

class StatusData {

    private Context context;
    private HashMap<String, Status> statusMap;

    StatusData (Context context) {
        setContext(context);
        setStatusMap(new HashMap<>());
    }

    private StatusData getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    HashMap<String, Status> getStatusMap() {
        return statusMap;
    }
    
    private void setStatusMap(HashMap<String, Status> statusMap) {
        getThis().statusMap = statusMap;
    }

    void readStatus(String itemName, String status, String checked) {
        if (getStatusMap().containsKey(itemName)) {
            Status thisStatus = getStatusMap().get(itemName);
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
            getStatusMap().put(itemName, newStatus);
        }
    }
}