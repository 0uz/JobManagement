package com.zirhgrup.jobmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zirhgrup.jobmanagement.model.Elevator;

import java.util.List;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<List<Elevator>> _elevator = new MutableLiveData<>();
    private LiveData<List<Elevator>> elevator =  _elevator;


    public void saveData(List<Elevator> data){
        _elevator.setValue(data);
    }

    public LiveData<List<Elevator>> getElevator() {
        return elevator;
    }
}
