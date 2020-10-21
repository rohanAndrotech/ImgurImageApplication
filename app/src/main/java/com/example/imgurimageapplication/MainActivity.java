package com.example.imgurimageapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.imgurimageapplication.model.ImageDetails;
import com.example.imgurimageapplication.util.Constants;
import com.example.imgurimageapplication.view.ImageAdapter;
import com.example.imgurimageapplication.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.searchView)
    SearchView searchView;

    ProgressDialog mProgressdialog;
    private String mquery = "";
    private MainViewModel mMainViewModel;
    private List<String> mImageUrlsList, mImageTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        //For now just added toast we can restrict user to perform any action.
        if (!Constants.isNetworkAvailable(MainActivity.this)) {
            checkInternetConnection();
        }

        mMainViewModel.getIsLoading().observe(this, aBoolean -> {
            if(aBoolean){
                showProgress();
            }else{
                hideProgress();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mquery = query;
                mMainViewModel.fetchImageList(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

        });

        mMainViewModel.getResult().observe(MainActivity.this,
                stringImageData -> {
                    try {
                        if (stringImageData == null)
                            return;
                        for (String key : stringImageData.keySet())
                            mquery = key;
                        if (!Objects.requireNonNull(stringImageData.get(mquery)).getImageLink().isEmpty()) {
                            setAdapterData(stringImageData);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    R.string.image_not_found, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ImageDetailsActivity.class);
            intent.putExtra(Constants.IMAGE_URL, mImageUrlsList.get(position));
            intent.putExtra(Constants.IMAGE_TITLE, mImageTitleList.get(position));
            intent.putExtra(Constants.IMAGE_POSITION,position);
            startActivity(intent);
        });
    }

    /**
     *  Set Image data to recyclerview adapter.
     * @param stringImageData
     */
    private void setAdapterData(Map<String, ImageDetails> stringImageData) {
        mImageUrlsList = (Objects.requireNonNull(stringImageData.get(mquery)))
                .getImageLink();
        mImageTitleList = (Objects.requireNonNull(stringImageData.get(mquery)))
                .getImageTitle();
        gridView.setAdapter(new ImageAdapter(MainActivity.this,
                mImageUrlsList));
    }

    /**
     * Check Internet Connection.
     */
    private void checkInternetConnection() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Please check internet connection!!!");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * Initialize the view.
     */
    private void initView() {
        mMainViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);
        mImageUrlsList = new ArrayList<>();
        mImageTitleList = new ArrayList<>();
        mProgressdialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainViewModel.reset();
    }

    private void showProgress() {
        mProgressdialog.setMessage("Please Wait....");
        mProgressdialog.show();
    }

    private void hideProgress() {
        mProgressdialog.dismiss();
    }

}