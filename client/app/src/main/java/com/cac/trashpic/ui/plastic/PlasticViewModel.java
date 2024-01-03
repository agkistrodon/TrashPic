package com.cac.trashpic.ui.plastic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlasticViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PlasticViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pastic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}