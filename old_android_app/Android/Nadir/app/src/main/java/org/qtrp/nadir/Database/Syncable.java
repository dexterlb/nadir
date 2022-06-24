package org.qtrp.nadir.Database;

public class Syncable {
    Long lastUpdate;
    String uniqueId;
    Integer isDeleted;
    Integer isSynced;


    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUniqueID() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSynced() {
        return isSynced;
    }

    public void setSynced(Integer isSynced) {
        this.isSynced = isSynced;
    }
}
