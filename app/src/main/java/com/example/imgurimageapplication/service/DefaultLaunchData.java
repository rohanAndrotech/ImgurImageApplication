package com.example.imgurimageapplication.service;

import androidx.lifecycle.MutableLiveData;

import com.example.imgurimageapplication.model.ImageDetails;
import com.example.imgurimageapplication.model.SearchResponse;
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

/**
 * Default value for first launch.
 */
public class DefaultLaunchData {
    MutableLiveData<Map<String, ImageDetails>> mutableLiveData;
    ApiInterface apiInterface;
    List<String> imageUrlsList, imagetitleList;
    ImageDetails details;
    Map<String, ImageDetails> stringMap;
    String mquery = "vanilla";
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * Get Image Data.
     *
     * @return MutableLiveData<Map < String, ImageDetails>>
     */
    public MutableLiveData<Map<String, ImageDetails>> getImageData() {

        if (mutableLiveData == null) {
            mIsLoading.setValue(true);
            mutableLiveData = new MutableLiveData<>();
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Disposable disposable =
                    apiInterface.getSearchImages(getMquery(), Constants.KEY_VALUE)
                    .debounce(Constants.DEBOUNCE_VALUE, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SearchResponse>() {
                        @Override
                        public void accept(SearchResponse searchResponse) {
                            imageUrlsList = new ArrayList<>();
                            imagetitleList = new ArrayList<>();
                            stringMap = new HashMap<>();
                            details = new ImageDetails(imageUrlsList, imagetitleList);

                            for (int i = 0; i < searchResponse.getData().size(); i++) {
                                if ((searchResponse.getData().get(i).getImages() != null)) {
                                    for (int j = 0; j < searchResponse.getData().get(i).getImages().size(); j++) {
                                        if ((searchResponse.getData().get(i).getImages()
                                                .get(j).getLink().contains(".jpg")
                                                || searchResponse.getData()
                                                .get(i).getImages().get(j).getLink().contains(".png"))) {

                                            if (searchResponse.getData().get(i).getTitle() != null) {
                                                imageUrlsList.add(i, searchResponse.getData().get(i)
                                                        .getImages().get(j).getLink());
                                                imagetitleList.add(i, searchResponse.getData().get(i)
                                                        .getTitle());
                                            }
                                        }
                                    }
                                }
                            }
                            stringMap.put(getMquery(), new ImageDetails(imageUrlsList, imagetitleList));
                            mutableLiveData.setValue(stringMap);
                        }}
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                mutableLiveData.setValue(null);
                                throwable.printStackTrace();
                                mIsLoading.setValue(false);
                            }
                    });
            if(disposable!=null) {
                mCompositeDisposable.add(disposable);
            }
            return mutableLiveData;
        }
        return mutableLiveData;
    }

    public String getMquery() {
        return mquery;
    }
}
