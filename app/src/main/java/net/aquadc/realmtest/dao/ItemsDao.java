package net.aquadc.realmtest.dao;

import net.aquadc.realmtest.model.Item;
import net.aquadc.realmtest.model.SubItem;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by miha on 16.09.16
 * DAO — Data Access Object
 */
public class ItemsDao {

    private final Realm realm;

    public ItemsDao(Realm realm) {
        this.realm = realm;
    }

    // Create

    public void addItem(String text) {
        // начать транзакцию
        realm.beginTransaction();
        // создать новый Item, сгенерировать ID
        Item item = realm.createObject(Item.class, UUID.randomUUID().toString());
        // записать текст
        item.setText(text);
        // завершить транзакицию (сохранить)
        realm.commitTransaction();
    }

    public void addSubItem(Item item, String subItemText) {
        realm.beginTransaction();
        SubItem subItem = realm.createObject(SubItem.class);
        subItem.setText(subItemText);
        // добавить созданный subItem к item'у
        item.getSubItems().add(subItem);
        realm.commitTransaction();
    }

    // Read

    public Item findOne(String uuid) {
        return realm.where(Item.class).equalTo("uuid", uuid).findFirst();
    }

    public RealmResults<Item> findAll() {
        return realm.where(Item.class).findAll();
    }

    // Update

    public void updateItem(Item item, String text) {
        realm.beginTransaction();
        // изменить текст Item'а
        item.setText(text);
        realm.commitTransaction();
    }

    // Delete

    public void removeItem(Item item) {
        realm.beginTransaction();

        // Удаляем subItem'ы.
        // Создаём копию списка!
        // Иначе после удаления первого subItem'а всё повалится.
        for (SubItem subItem : new ArrayList<>(item.getSubItems())) {
            subItem.deleteFromRealm();
        }

        // удалить Item
        item.deleteFromRealm();

        realm.commitTransaction();
    }

}
