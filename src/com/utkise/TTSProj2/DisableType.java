package com.utkise.TTSProj2;

import java.util.List;

/**
 * Created by Bill on 9/9/14.
 */
public class DisableType {
    private List<ItemStruct> generalInfo;
    private List<ItemStruct> tripInfo;
    private List<ItemStruct> safety;
    private List<ItemStruct> comfort;
    private List<ItemStruct> emergency;
    private List<ItemStruct> response;

    private String tag;
    private Integer image;

    public DisableType(String name, Integer img) {
        generalInfo = null;
        tripInfo = null;
        safety = null;
        comfort = null;
        emergency = null;
        this.response = null;
        this.tag = name;
        this.image = img;
    }

    public void setTripInfo(List<ItemStruct> tripInfo) {
        this.tripInfo = tripInfo;
    }

    public void setSafety(List<ItemStruct> safety) {
        this.safety = safety;
    }

    public void setEmergency(List<ItemStruct> emergency) {
        this.emergency = emergency;
    }

    public void setComfort(List<ItemStruct> comfort) {
        this.comfort = comfort;
    }

    public void setGeneralInfo(List<ItemStruct> generalInfo) {
        this.generalInfo = generalInfo;
    }

    public void setInformation(String type, List<ItemStruct> info) {
        if (type.equalsIgnoreCase("general")) {
            this.generalInfo = info;
        } else if (type.equalsIgnoreCase("trip")) {
            this.tripInfo = info;
        } else if (type.equalsIgnoreCase("safety")) {
            this.safety = info;
        } else if (type.equalsIgnoreCase("comfort")) {
            this.comfort = info;
        } else if (type.equalsIgnoreCase("emergency")) {
            this.emergency = info;
        } else if (type.equalsIgnoreCase("response"))  {
            this.response = info;
        }

    }

    public List<ItemStruct> getInformation(String type) {
        if (type.equalsIgnoreCase("general")) {
            return generalInfo;
        } else if (type.equalsIgnoreCase("trip")) {
            return tripInfo;
        } else if (type.equalsIgnoreCase("safety")) {
            return safety;
        } else if (type.equalsIgnoreCase("comfort")) {
            return comfort;
        } else if (type.equalsIgnoreCase("emergency")) {
            return emergency;
        } else if (type.equalsIgnoreCase("response")){
            return response;
        } else {
            return null;
        }
    }



    public List<ItemStruct> getGeneralInfo() {
        return generalInfo;
    }

    public List<ItemStruct> getTripInfo() {
        return tripInfo;
    }

    public List<ItemStruct> getSafety() {
        return safety;
    }

    public List<ItemStruct> getComfort() {
        return comfort;
    }

    public List<ItemStruct> getEmergency() {
        return emergency;
    }

    public String getTag() {
        return tag;
    }

    public Integer getImage() {
        return image;
    }
}
