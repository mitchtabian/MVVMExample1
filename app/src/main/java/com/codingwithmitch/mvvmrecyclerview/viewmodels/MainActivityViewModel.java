package com.codingwithmitch.mvvmrecyclerview.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.codingwithmitch.mvvmrecyclerview.models.NicePlace;
import com.codingwithmitch.mvvmrecyclerview.repositories.NicePlaceRepository;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<NicePlace>> mNicePlaces;
    private NicePlaceRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(){
        if(mNicePlaces != null){
            return;
        }
        mRepo = NicePlaceRepository.getInstance();
        mNicePlaces = mRepo.getNicePlaces();
    }

    public void addNewValue(final NicePlace nicePlace){
        mIsUpdating.setValue(true);

        new AddNewPlace(this,nicePlace).execute();
    }

    public LiveData<List<NicePlace>> getNicePlaces(){
        return mNicePlaces;
    }


    public LiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }

    private static class AddNewPlace extends AsyncTask<Void,Void,Void>{

        private WeakReference<MainActivityViewModel> weakReference;
        private NicePlace nicePlace;

        AddNewPlace(MainActivityViewModel viewModel,NicePlace nicePlace) {
            weakReference = new WeakReference<>(viewModel);
            this.nicePlace=nicePlace;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            MainActivityViewModel viewModel = weakReference.get();
            if (viewModel == null) {
                return;
            }

            List<NicePlace> currentPlaces = viewModel.mNicePlaces.getValue();
            currentPlaces.add(nicePlace);
            viewModel.mNicePlaces.postValue(currentPlaces);
            viewModel.mIsUpdating.postValue(false);
        }
    }
}
