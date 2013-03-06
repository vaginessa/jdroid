package com.jdroid.android.date;

import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TimePicker;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class TimePickerDialogFragment extends AbstractDialogFragment {
	
	private static final String DEFAULT_TIME_EXTRA = "defaultTime";
	
	public static void show(Fragment targetFragment, Date defaultTime) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		TimePickerDialogFragment fragment = new TimePickerDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEFAULT_TIME_EXTRA, defaultTime);
		fragment.setArguments(bundle);
		
		fragment.setTargetFragment(targetFragment, 1);
		fragment.show(fm, TimePickerDialogFragment.class.getSimpleName());
	}
	
	private Date defaultTime;
	
	public interface OnTimeSetListener {
		
		public void onTimeSet(Date time);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		defaultTime = getArgument(DEFAULT_TIME_EXTRA);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.time_picker_dialog_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getDialog().setTitle(R.string.selectTime);
		
		final TimePicker timePicker = findView(R.id.timePicker);
		timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		timePicker.setCurrentHour(DateUtils.getHour(defaultTime, true));
		timePicker.setCurrentMinute(DateUtils.getMinute(defaultTime));
		
		findView(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date time = DateUtils.getTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				((OnTimeSetListener)getTargetFragment()).onTimeSet(time);
				dismiss();
			}
		});
		
		findView(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
