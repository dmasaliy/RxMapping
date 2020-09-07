package com.example.rxmapping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ThirdActivity extends AppCompatActivity {
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Observable.concat(getEuropeanMotorObservable(), getJapanMotorObservable())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Motorcycle>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Motorcycle motorcycle) {
                        Log.d(MainActivity.TAG, "onNExt: " + motorcycle);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private Observable<Motorcycle> getEuropeanMotorObservable(){
        final List<Motorcycle> motorcycleList = new ArrayList<>();
        motorcycleList.add(new Motorcycle("BMW", "7777" ));
        motorcycleList.add(new Motorcycle("Triumph", "Tiger 1000"));

        return Observable
                .create(new ObservableOnSubscribe<Motorcycle>() {
                    @Override
                    public void subscribe(ObservableEmitter<Motorcycle> emitter) throws Exception {
                        for(Motorcycle moto: motorcycleList){
                            if(!emitter.isDisposed()){
                                Thread.sleep(100);
                                emitter.onNext(moto);
                            }
                        }
                        if(!emitter.isDisposed())
                            emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io());
    }

    private Observable<Motorcycle> getJapanMotorObservable(){
        final List<Motorcycle> motorcycleList = new ArrayList<>();
        motorcycleList.add(new Motorcycle("Suzuki", "SV1000" ));
        motorcycleList.add(new Motorcycle("Kawasaki", "ZZR1400"));

        return Observable
                .create(new ObservableOnSubscribe<Motorcycle>() {
                    @Override
                    public void subscribe(ObservableEmitter<Motorcycle> emitter) throws Exception {
                        for(Motorcycle moto: motorcycleList){
                            if(!emitter.isDisposed()){
                                Thread.sleep(1000);
                                emitter.onNext(moto);
                            }
                        }
                        if(!emitter.isDisposed())
                            emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io());
    }
}