package com.runnningsnail.rxjavademo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";

	@BindView(R.id.btn_create)
	Button btnCreate;
	@BindView(R.id.btn_just)
	Button btnJust;
	@BindView(R.id.btn_map)
	Button btnMap;
	@BindView(R.id.btn_flatmap)
	Button btnFlatmap;
	@BindView(R.id.btn_from)
	Button btnFrom;
	@BindView(R.id.btn_interval)
	Button btnInterval;
	@BindView(R.id.btn_interval_range)
	Button btnIntervalRange;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

	}

	@OnClick({R.id.btn_create, R.id.btn_just, R.id.btn_map, R.id.btn_from, R.id.btn_interval, R.id.btn_interval_range,
			R.id.btn_flatmap})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_create:
				createOperator();
				break;
			case R.id.btn_just:
				justOperator();
				break;
			case R.id.btn_map:
				mapOperator();
				break;
			case R.id.btn_from:
				fromOperator();
				break;
			case R.id.btn_flatmap:
				flatMapOperator();
				break;
			case R.id.btn_interval:
				intervalOperator();
				break;
			case R.id.btn_interval_range:
				intervalRangeOperator();
				break;
			default:
				break;
		}
	}

	private void intervalRangeOperator() {
		//指定开始值和数量,数量会在开始值的基础上继续自增操作
		Observable.intervalRange(3, 5, 0, 1, TimeUnit.SECONDS)
				.subscribeOn(Schedulers.io())
				.subscribe(new Observer<Long>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "intervalRange==> onSubscribe");
					}

					@Override
					public void onNext(Long aLong) {
						HiLogger.i(TAG, "intervalRange==> onNext %s", aLong);
					}

					@Override
					public void onError(Throwable e) {
						HiLogger.i(TAG, "intervalRange==> onError");
					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "intervalRange==> onComplete");
					}
				});
	}

	private void fromOperator() {
		//输入参数是一个迭代器
		List<String> stringList = new ArrayList<>();
		stringList.add("Hello");
		stringList.add("World");
		Observable.fromIterable(stringList).subscribe(new Observer<String>() {
			@Override
			public void onSubscribe(Disposable d) {
				HiLogger.i(TAG, "fromIterable==> onSubscribe ");
			}

			@Override
			public void onNext(String s) {
				HiLogger.i(TAG, "fromIterable==> onNext %s", s);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				HiLogger.i(TAG, "fromIterable==> onComplete");
			}
		});

		//输入参数是一个泛型或数组
		Integer[] integers = new Integer[]{1, 2, 3};
		Observable.fromArray(integers).subscribe(new Observer<Integer>() {
			@Override
			public void onSubscribe(Disposable d) {
				HiLogger.i(TAG, "fromArray==> onSubscribe");
			}

			@Override
			public void onNext(Integer integer) {
				HiLogger.i(TAG, "fromArray==> onNext %s", integer);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				HiLogger.i(TAG, "fromArray==> onComplete");
			}
		});

		//输入参数是一个callable对象 这个对象的call方法的执行结果会被发射出去
		Observable.fromCallable(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "Hello";
			}
		}).subscribe(new Observer<String>() {
			@Override
			public void onSubscribe(Disposable d) {
				HiLogger.i(TAG, "fromCallable==> onSubscribe");
			}

			@Override
			public void onNext(String s) {
				HiLogger.i(TAG, "fromCallable==> onNext %s", s);
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				HiLogger.i(TAG, "fromCallable==> onComplete");
			}
		});
	}

	private void flatMapOperator() {
		List<Person> personList = new ArrayList<>();
		personList.add(new Person("小明"));
		personList.add(new Person("小王"));
		personList.add(new Person("小田"));

		Observable.fromIterable(personList)
				.flatMap(new Function<Person, ObservableSource<Plan>>() {
					@Override
					public ObservableSource<Plan> apply(Person person) throws Exception {
						return Observable.fromIterable(person.getPlanList());
					}
				})
				.flatMap(new Function<Plan, ObservableSource<String>>() {
					@Override
					public ObservableSource<String> apply(Plan plan) throws Exception {
						return Observable.fromIterable(plan.getActionList());
					}
				})
				.subscribe(new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "flatMap==> onSubscribe ");
					}

					@Override
					public void onNext(String s) {
						HiLogger.i(TAG, "flatMap==> onNext %s", s);
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "flatMap==> onComplete ");
					}
				});

	}

	private void mapOperator() {

		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				HiLogger.i(TAG, "map==> subscribe");
				emitter.onNext("1");
				emitter.onNext("2");
				emitter.onNext("3");
				emitter.onComplete();
			}
		}).subscribeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.io())
				.map(new Function<String, Integer>() {
					@Override
					public Integer apply(String s) throws Exception {
						HiLogger.i(TAG, "map==> %s", s);
						return Integer.parseInt(s);
					}
				}).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Integer>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "map==> onSubscribe");
					}

					@Override
					public void onNext(Integer integer) {
						HiLogger.i(TAG, "map==> onNext %s", integer);
					}

					@Override
					public void onError(Throwable e) {
						HiLogger.e(TAG, "map==> ", e);
					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "map==> onComplete ");
					}
				});


		Observable.just("1", "2", "3 ").

				subscribeOn(Schedulers.io())
				.

						subscribeOn(AndroidSchedulers.mainThread())
				.

						observeOn(Schedulers.io())
				.

						map(new Function<String, Integer>() {
							@Override
							public Integer apply(String s) throws Exception {
								HiLogger.i(TAG, "map2==> %s", s);
								return Integer.parseInt(s);
							}
						}).

				observeOn(AndroidSchedulers.mainThread())
				.

						subscribe(new Observer<Integer>() {
							@Override
							public void onSubscribe(Disposable d) {
								HiLogger.i(TAG, "map2==> onSubscribe");
							}

							@Override
							public void onNext(Integer integer) {
								HiLogger.i(TAG, "map2==> onNext %s", integer);
							}

							@Override
							public void onError(Throwable e) {
								HiLogger.e(TAG, "map2==> ", e);
							}

							@Override
							public void onComplete() {
								HiLogger.i(TAG, "map2==> onComplete ");
							}
						});
	}

	private void justOperator() {
		//just操作符最多只能传递10个参数
		Observable.just("Hello").subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						HiLogger.i(TAG, "just==> %s", s);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						HiLogger.i(TAG, "just==> %s", throwable);
					}
				}, new Action() {
					@Override
					public void run() throws Exception {
						HiLogger.i(TAG, "just==> complete");
					}
				});
	}

	private void createOperator() {
		//create操作符需要自己使用emitter去触发观察者的事件
		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				HiLogger.i(TAG, "create==> subscribe");
				emitter.onNext("create==> Hello");
				emitter.onNext("create==> World");
			}
		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
				.subscribe(new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "create==> onSubscribe");
					}

					@Override
					public void onNext(String s) {
						HiLogger.i(TAG, "create==> onNext %s", s);
					}

					@Override
					public void onError(Throwable e) {
						HiLogger.i(TAG, "create==> onError ");
					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "create==> onComplete ");
					}
				});

		Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> emitter) throws Exception {
				HiLogger.i(TAG, "create==> subscribe2");
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "create==> onSubscribe2");
					}

					@Override
					public void onNext(String s) {
						HiLogger.i(TAG, "create==> onNext2 %s", s);
					}

					@Override
					public void onError(Throwable e) {
						HiLogger.i(TAG, "create==> onError2 ");
					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "create==> onComplete2 ");
					}
				});
	}

	private void intervalOperator() {
		//周期性任务
		Observable.interval(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
				.doOnNext(new Consumer<Long>() {
					@Override
					public void accept(Long aLong) throws Exception {
						HiLogger.i(TAG, "interval==> doOnNext %s", aLong);
					}
				}).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Long>() {
					@Override
					public void onSubscribe(Disposable d) {
						HiLogger.i(TAG, "interval==> onSubscribe");
					}

					@Override
					public void onNext(Long aLong) {
						HiLogger.i(TAG, "interval==> onNext %s", aLong);
					}

					@Override
					public void onError(Throwable e) {
						HiLogger.i(TAG, "interval==> onError");
					}

					@Override
					public void onComplete() {
						HiLogger.i(TAG, "interval==> onComplete");
					}
				});
	}


}
