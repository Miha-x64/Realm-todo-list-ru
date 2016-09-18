package net.aquadc.realmtest.interaction;

import android.support.annotation.DrawableRes;

/**
 * Created by miha on 16.09.16
 * Фрагмент должен реализовать этот интерефйс чтобы взаимодействовать с FAB в Activity
 */
public interface FabClient {
    @DrawableRes int getFabIcon();
    void onFabClick();
}
