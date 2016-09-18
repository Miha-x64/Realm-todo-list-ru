package net.aquadc.realmtest.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// Всё, что нужно, чтобы создать модель, управляемую Realm'ом —
// просто унаследовать RealmObject.
public class Item extends RealmObject {

    // ID
    @PrimaryKey
    private String uuid;
    public String getUuid() {
        return uuid;
    }

    // Поле со стандартными геттерами и сеттерами
    private String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    // Список подItem'ов
    private RealmList<SubItem> subItems;
    public RealmList<SubItem> getSubItems() {
        return subItems;
    }

    // toString для адаптера
    @Override
    public String toString() {
        return text;
    }
}
