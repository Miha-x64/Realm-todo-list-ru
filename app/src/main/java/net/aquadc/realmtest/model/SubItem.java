package net.aquadc.realmtest.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by miha on 16.09.16
 */
public class SubItem extends RealmObject {

    @PrimaryKey
    private String uuid;
    public String getUuid() {
        return uuid;
    }

    private String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
