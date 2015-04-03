package com.utkise.TTSProj2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 9/9/14.
 */
public class DisableType {

    private List<ItemStruct> generalInfo;
    private List<ItemStruct> emergency;
    private List<ItemStruct> response;

    private String tag;
    private Integer image;
    private Integer imageV;
    private Integer imageS;
    private Integer customCount;

    public DisableType(String name, Integer img, Integer imgV, Integer imgS) {
        generalInfo = null;
        emergency = null;
        this.response = null;
        this.tag = name;
        this.image = img;
        this.imageV = imgV;
        this.imageS = imgS;
        this.customCount = 1;
    }

    public void setEmergency(List<ItemStruct> emergency) {
        this.emergency = emergency;
    }

    public void setGeneralInfo(List<ItemStruct> generalInfo) {
        this.generalInfo = generalInfo;
    }

    public void setInformation(String type, List<ItemStruct> info) {
        if (type.equalsIgnoreCase("general")) {
            this.generalInfo = info;
        } else if (type.equalsIgnoreCase("emergency")) {
            this.emergency = info;
        } else if (type.equalsIgnoreCase("response"))  {
            this.response = info;
        }

    }

    public List<ItemStruct> getInformation(String type) {
        if (type.equalsIgnoreCase("general")) {
            return generalInfo;
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

        return newList;
    }

    public List<ItemStruct> getResponseInfo() {return response;}

    public List<ItemStruct> getGeneralInfo() {
        return generalInfo;
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

    public Integer getImageS() {
        return imageS;
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

    public List<ItemStruct> getSystemResponse() {
        return response.subList(customCount, response.size());
    }
}
