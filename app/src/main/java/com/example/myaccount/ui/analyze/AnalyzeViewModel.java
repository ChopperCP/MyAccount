package com.example.myaccount.ui.analyze;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnalyzeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnalyzeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is analyze fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}