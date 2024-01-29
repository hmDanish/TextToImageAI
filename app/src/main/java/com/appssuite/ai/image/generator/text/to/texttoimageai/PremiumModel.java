package com.appssuite.ai.image.generator.text.to.texttoimageai;

public class PremiumModel {
    String packName;
    int name;

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public PremiumModel(String packName, int name) {
        this.packName = packName;
        this.name = name;
    }
}
