package com.cac.trashpic.ui.metal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MetalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MetalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is metak fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}