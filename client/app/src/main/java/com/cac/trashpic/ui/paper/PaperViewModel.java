package com.cac.trashpic.ui.paper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaperViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PaperViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is aoer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}