package com.cac.trashpic.ui.cardboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CardboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CardboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cardboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}