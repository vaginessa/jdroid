package com.jdroid.android.sample.ui.sqlite;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.application.AndroidApplication;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.IdGenerator;

public class SQLiteFragment extends AbstractFragment {

	private static Long lastId;
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.sqlite_fragment;
	}

	/**
	 * @see AbstractFragment#onViewCreated(View, Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.sqliteAdd).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						SampleSQLiteEntity entity = new SampleSQLiteEntity();
						lastId = IdGenerator.getRandomLongId();
						entity.setId(lastId);
						entity.setField(IdGenerator.getRandomLongId().toString());
						repository.add(entity);
					}
				});
			}
		});
		findView(R.id.sqliteUpdate).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
							SampleSQLiteEntity entity = repository.get(lastId);
							if (entity != null) {
								entity.setField(IdGenerator.getRandomLongId().toString());
								repository.update(entity);
							}
						}
					}
				});
			}
		});
		findView(R.id.sqliteRemove).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						if (lastId != null) {
							Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
							repository.remove(lastId);
							lastId = null;
						}
					}
				});
			}
		});
		findView(R.id.sqliteRemoveAll).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						repository.removeAll();
					}
				});
			}
		});
		findView(R.id.sqliteGetAll).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExecutorUtils.execute(new Runnable() {
					@Override
					public void run() {
						final Repository<SampleSQLiteEntity> repository = AndroidApplication.get().getRepositoryInstance(SampleSQLiteEntity.class);
						executeOnUIThread(new Runnable() {
							@Override
							public void run() {
								((TextView)findView(R.id.results)).setText(repository.getAll().toString());
							}
						});
					}
				});
			}
		});
	}
}
