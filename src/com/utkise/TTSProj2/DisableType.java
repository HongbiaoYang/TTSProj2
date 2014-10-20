package com.utkise.TTSProj2;

import java.util.ArrayList;
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
    private Integer imageV;
    private Integer customCount;

    public DisableType(String name, Integer img, Integer imgV) {
        generalInfo = null;
        tripInfo = null;
        safety = null;
        comfort = null;
        emergency = null;
        this.response = null;
        this.tag = name;
        this.image = img;
        this.imageV = imgV;
        this.customCount = 1;
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


    public List<ItemStruct> getAllInfo() {
        List<ItemStruct> newList = new ArrayList<ItemStruct>();
        newList.addAll(getGeneralInfo());
        newList.addAll(getTripInfo());
        newList.addAll(getSafety());
        newList.addAll(getComfort());

        return newList;
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

    public Integer getImageV() {
        return imageV;
    }

    public void setImageV(Integer imgV) {
        this.imageV = imgV;
    }

    public Integer getCustomCount() {
        return customCount;
    }

    public void setCustomCount(Integer customCount) {
        this.customCount = customCount;
    }

    public void incrementCustomCount() {
        this.customCount ++;
    }

    public void decreaseCustomCount() {
        this.customCount--;
    }
}
