package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PairItemRecyclerViewType;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;

import java.util.Date;
import java.util.List;

public class FirebaseRemoteConfigFragment extends AbstractRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<Object> items = Lists.newArrayList();
		items.add("");
		for(RemoteConfigParameter each: AbstractApplication.get().getRemoteConfigParameters()) {
			items.add(new Pair<String, Object>(each.getKey(), FirebaseRemoteConfigHelper.getString(each)));
		}

		List<RecyclerViewType> recyclerViewTypes = Lists.<RecyclerViewType>newArrayList(new HeaderRecyclerViewType(), new PairItemRecyclerViewType() {
			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return FirebaseRemoteConfigFragment.this;
			}
		});
		setAdapter(new RecyclerViewAdapter(recyclerViewTypes, items));
	}

	public class HeaderRecyclerViewType extends RecyclerViewType<String, HeaderViewHolder> {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_firebase_remote_config_header;
		}

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			HeaderViewHolder viewHolder = new HeaderViewHolder(view);
			viewHolder.fetchTimeMillis = findView(view, R.id.fetchTimeMillis);
			viewHolder.lastFetchStatus = findView(view, R.id.lastFetchStatus);
			viewHolder.fetch = findView(view, R.id.fetch);
			return viewHolder;
		}

		@Override
		public void fillHolderFromItem(String item, HeaderViewHolder holder) {
			holder.fetch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					FirebaseRemoteConfigHelper.fetchNow();
				}
			});

			String fetchDate = DateUtils.formatDateTime(new Date(FirebaseRemoteConfigHelper.getFirebaseRemoteConfig().getInfo().getFetchTimeMillis()));
			holder.fetchTimeMillis.setText("Fetch Date: " + fetchDate);

			holder.lastFetchStatus.setText("Last Fetch Status: " + FirebaseRemoteConfigHelper.getFirebaseRemoteConfig().getInfo().getLastFetchStatus());
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return FirebaseRemoteConfigFragment.this;
		}
	}

	public static class HeaderViewHolder extends RecyclerView.ViewHolder {

		public TextView fetchTimeMillis;
		public TextView lastFetchStatus;
		public TextView fetch;

		public HeaderViewHolder(View itemView) {
			super(itemView);
		}
	}
}
