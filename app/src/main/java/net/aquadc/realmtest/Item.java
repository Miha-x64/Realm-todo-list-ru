package net.aquadc.realmtest;

import io.realm.RealmObject;

// Всё, что нужно, чтобы создать модель, управляемую Realm'ом —
// просто унаследовать RealmObject.
public class Item extends RealmObject /*implements ItemAdapter.Parent<Item>*/ {

    // Поле со стандартными геттерами и сеттерами
    private String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    // Список подItem'ов
    /*private RealmList<Item> subitems;
    public RealmList<Item> getSubitems() {
        return subitems;
    }*/

    // флаг, является ли данный Item подItem'ом
    /*private boolean subitem;
    public boolean isSubitem() {
        return subitem;
    }
    public void setSubitem(boolean subitem) {
        this.subitem = subitem;
    }*/

    // toString для адаптера
    @Override
    public String toString() {
        return text;
    }

    /*@Override
    public int getChildCount() {
        return subitems.size();
    }

    @Override
    public Item getChild(int pos) {
        return subitems.get(pos);
    }*/
}
