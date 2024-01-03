package com.cac.trashpic.ui.glass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlassViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GlassViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is glass fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}