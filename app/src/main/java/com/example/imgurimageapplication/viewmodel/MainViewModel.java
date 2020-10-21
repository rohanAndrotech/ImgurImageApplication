package com.example.imgurimageapplication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imgurimageapplication.model.ImageDetails;
import com.example.imgurimageapplication.model.SearchResponse;
import com.example.imgurimageapplication.service.ApiClient;
import com.example.imgurimageapplication.service.ApiInterface;
import com.example.imgurimageapplication.service.DefaultLaunchData;
import com.example.imgurimageapplication.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    DefaultLaunchData mConnection;
    MutableLiveData<Map<String, ImageDetails>> mResult;
    private ApiInterface mApiInterface;
    private List<String>mImageUrlsList, mImageTitleList;
    private Map<String, ImageDetails> mStringMap;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();


    public MainViewModel() {
        mConnection = new DefaultLaunchData();
        mResult = mConnection.getImageData();
    }


    /**
     * This function is used when making a new request
     * to the api for new search query
     * @param query the search query that is the endpoint of the api.
     */
    public void fetchImageList(final String query)
    {
        mIsLoading.setValue(true);
        mImageUrlsList = new ArrayList<>();
        mImageTitleList = new ArrayList<>();
        mApiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Disposable disposable =
                mApiInterface.getSearchImages(query, Constants.KEY_VALUE)
                .debounce(Constants.DEBOUNCE_VALUE, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResponse>() {
                    @Override
                    public void accept(SearchResponse searchResponse) throws Exception {
                        if (searchResponse != null) {
                            mStringMap = new HashMap<>();
                            for (int i = 0; i < searchResponse.getData().size(); i++) {
                                if ((searchResponse.getData().get(i).getImages() != null)) {
                                    for (int j = 0; j < searchResponse.getData().get(i).getImages().size(); j++) {
                                        if (searchResponse.getData().get(i).getImages().get(j).getLink()
                                                .contains(".jpg") || searchResponse.getData()
                                                .get(i).getImages().get(j).getLink().contains(".png")) {
                                            if (searchResponse.getData().get(i).getTitle() != null) {
                                                mImageUrlsList.add(i, searchResponse.getData().get(i)
                                                        .getImages().get(j).getLink());
                                                mImageTitleList.add(i, searchResponse.getData().get(i)
                                                        .getTitle());
                                            }
                                        }
                                    }
                                }
                            }
                            mStringMap.put(query, new ImageDetails(mImageUrlsList, mImageTitleList));
                            mResult.setValue(mStringMap);
                        }
                        mIsLoading.setValue(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        mResult.setValue(null);
                        throwable.printStackTrace();
                        mIsLoading.setValue(false);
                    }
                });
        if(disposable!=null) {
            mCompositeDisposable.add(disposable);
        }
    }

    /**
     * MutableLiveData return type stated getter function.
     * @return MutableLiveData<Map<String, ImageDetails>>
     */
    public MutableLiveData<Map<String, ImageDetails>> getResult() {
        return mResult;
    }

    public MutableLiveData<Boolean> getIsLoading(){
        return mIsLoading;
    }

    public void reset() {
        mCompositeDisposable.dispose();
        mCompositeDisposable = null;
    }
}
