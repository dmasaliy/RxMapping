package com.example.rxmapping;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Rx";
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMotoObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Function<Motorcycle, Observable<Motorcycle>>() {
                    @Override
                    public Observable<Motorcycle> apply(Motorcycle motorcycle)  {
                        return getModelObservable(motorcycle);
                    }
                }).subscribe(new Observer<Motorcycle>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
            @Override
            public void onNext(Motorcycle motorcycle) {
                Log.d(TAG, "onNext: " + motorcycle);
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
    private Observable<Motorcycle> getMotoObservable(){
        final List<Motorcycle> motorcycles = new ArrayList<>();
        motorcycles.add(new Motorcycle("Honda"));
        motorcycles.add(new Motorcycle("Suzuki"));
        motorcycles.add(new Motorcycle("BMW"));
        motorcycles.add(new Motorcycle("Kawasaki"));
        return Observable.create(new ObservableOnSubscribe<Motorcycle>() {
            @Override
            public void subscribe(ObservableEmitter<Motorcycle> emitter) throws Exception {
                for (Motorcycle motorcycle: motorcycles) {
                    if (!emitter.isDisposed()){
                        emitter.onNext(motorcycle);
                    }
                }
                if (!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
    private Observable<Motorcycle> getModelObservable(final Motorcycle motorcycle){
        return Observable.create(new ObservableOnSubscribe<Motorcycle>() {
            @Override
            public void subscribe(ObservableEmitter<Motorcycle> emitter) throws Exception {
                Model model = null;
                switch (motorcycle.getBrand()){
                    case "Honda":
                        model = new Model("CBR1000RR");
                        break;
                    case "Suzuki":
                        model = new Model("SV1000");
                        break;
                    case "BMW":
                        model = new Model("R1200GS");
                        break;
                    case "Kawasaki":
                        model = new Model("ZZR1400");
                        break;
                }
                if (!emitter.isDisposed()){
                    motorcycle.setModel(model);
                    Thread.sleep(300);
                    emitter.onNext(motorcycle);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}