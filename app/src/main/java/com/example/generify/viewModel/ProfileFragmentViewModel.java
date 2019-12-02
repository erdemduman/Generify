package com.example.generify.viewModel;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.generify.command.Command;
import com.example.generify.command.ICommand;
import com.example.generify.constant.ServiceDictionary;
import com.example.generify.model.UserTopTrack;
import com.example.generify.service.SpotifyAuth;
import com.example.generify.service.UserService;
import com.example.generify.service.Worker;
import com.example.generify.util.parser.UserParser;
import com.spotify.sdk.android.authentication.AuthenticationRequest;

import java.util.List;

public class ProfileFragmentViewModel extends BaseViewModel {

    private MutableLiveData<AuthenticationRequest> spotifyAuthRequestProp;
    public String profileText;
    private ICommand back;
    private String userTopTracksRaw = "asd";
    private MutableLiveData<List<UserTopTrack>> userTopTracks;

    public ProfileFragmentViewModel(@NonNull Application application){
        super(application);
        spotifyAuthRequestProp = new MutableLiveData<>();
        userTopTracks = new MutableLiveData<>();
        initCmd();
        serviceCall();
    }

    private void initCmd(){
        back = new Command<Void>(this::cmdBack);
    }

    private void serviceCall(){
        UserService userService = new UserService(getApplication());
        try{
            userTopTracksRaw = new Worker(userService.getMethod(ServiceDictionary.USER_TOP_TRACKS)).execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        new Thread(() -> {
            userTopTracks.postValue(UserParser.userTopTracksParser(userTopTracksRaw));
        }).start();
    }

    public void onClickLoginToSpotify(){
        runSpotifyAuthRequest();
    }

    public void runSpotifyAuthRequest(){
        spotifyAuthRequestProp.setValue(SpotifyAuth.getAuthRequest());
    }

    public ICommand getBackCmd(){
        return back;
    }

    public LiveData<AuthenticationRequest> getSpotifyAuthRequest(){
        return spotifyAuthRequestProp;
    }

    private void cmdBack(Void param){
        Log.d("TAG", "DOOGRU AQQ");
    }

    public LiveData<List<UserTopTrack>> getUserTopTracks(){
        return userTopTracks;
    }

}
