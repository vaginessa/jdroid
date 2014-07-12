package com.jdroid.android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.jdroid.android.R;

/**
 * Layout which allows to implement Quick Return pattern for a ListView. QuickReturnLayout set its own OnScrollListener
 * to the list, is you change it directly QuickReturnLayout will not work. If you need to add an OnScrollListener to the
 * list use the {@link QuickReturnLayout#setOnScrollListener(OnScrollListener)} method.
 * 
 * @author Javier Nonis
 */
public class QuickReturnLayout extends FrameLayout {
	
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_SCROLLING = 2;
	
	private int headerId;
	private int listId;
	private View headerView;
	private View headerPlaceHolderView;
	private ListView listView;
	private OnScrollListener onScrollListener;
	
	private int headerState = STATE_ONSCREEN;
	private int oldFirstVisiblePosition;
	private int oldChildTop;
	private int oldChildHeight;
	private int headerScroll;
	private int scrollDirection;
	private boolean isAnimating = false;
	private Runnable delayedAnimation;
	private boolean isQuickReturnEnable = true;
	
	public QuickReturnLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public QuickReturnLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.quickReturnLayout, 0, 0);
		try {
			headerId = a.getResourceId(R.styleable.quickReturnLayout_header, 0);
			if (headerId == 0) {
				throw new IllegalArgumentException("The header attribute is required and must refer to a valid child.");
			}
			listId = a.getResourceId(R.styleable.quickReturnLayout_list, 0);
			if (listId == 0) {
				throw new IllegalArgumentException("The list attribute is required and must refer to a valid child.");
			}
		} finally {
			a.recycle();
		}
	}
	
	/**
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		listView = (ListView)findViewById(listId);
		headerView = findViewById(headerId);
		headerPlaceHolderView = new View(getContext());
		headerPlaceHolderView.setLayoutParams(new AbsListView.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		listView.addHeaderView(headerPlaceHolderView);
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if ((scrollState == SCROLL_STATE_IDLE) && (headerState == STATE_SCROLLING) && !isAnimating) {
					// Post an animation to show/hide header is it is not completely opened after some time.
					delayedAnimation = new Runnable() {
						
						@Override
						public void run() {
							if (!isAnimating) {
								isAnimating = true;
								translateAnimation();
							}
						}
					};
					postDelayed(delayedAnimation, 100);
				} else {
					// Remove previous posted animation.
					if (delayedAnimation != null) {
						removeCallbacks(delayedAnimation);
					}
				}
				if (onScrollListener != null) {
					onScrollListener.onScrollStateChanged(view, scrollState);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (onScrollListener != null) {
					onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				}
				
				if (!isQuickReturnEnable || isAnimating || (view.getChildCount() == 0)) {
					return;
				}
				
				final int firstVisiblePosition = listView.getFirstVisiblePosition();
				View child = listView.getChildAt(0);
				int childTop = child.getTop();
				int childHeight = child.getHeight() == 0 ? child.getMeasuredHeight() : child.getHeight();
				int oldheaderScroll = headerScroll;
				
				if (oldFirstVisiblePosition < firstVisibleItem) {
					// Scroll down.
					headerScroll += childTop - oldChildTop - oldChildHeight;
				} else if (oldFirstVisiblePosition > firstVisibleItem) {
					// Scroll up.
					headerScroll += (childTop - oldChildTop) + childHeight;
				} else {
					headerScroll += childTop - oldChildTop;
				}
				oldFirstVisiblePosition = firstVisiblePosition;
				oldChildTop = childTop;
				oldChildHeight = childHeight;
				// Use a threshold verification to dismiss small movements.
				if (Math.abs(headerScroll - oldheaderScroll) > 3) {
					scrollDirection = headerScroll > oldheaderScroll ? 1 : -1;
				}
				
				final int headerViewHeight = headerView.getHeight() == 0 ? headerView.getMeasuredHeight()
						: headerView.getHeight();
				
				switch (headerState) {
					case STATE_OFFSCREEN:
						if (headerScroll > -headerViewHeight) {
							headerState = STATE_SCROLLING;
							// Check on screen limit.
							if (headerScroll > 0) {
								headerScroll = 0;
							}
							// Check off screen limit
							if (headerScroll < -headerViewHeight) {
								headerScroll = -headerViewHeight;
							}
						} else {
							headerScroll = -headerViewHeight;
						}
						break;
					
					case STATE_ONSCREEN:
						if (headerScroll < 0) {
							headerState = STATE_SCROLLING;
							// Check on screen limit.
							if (headerScroll > 0) {
								headerScroll = 0;
							}
							// Check off screen limit
							if (headerScroll < -headerViewHeight) {
								headerScroll = -headerViewHeight;
							}
						} else {
							headerScroll = 0;
						}
						break;
					
					case STATE_SCROLLING:
						// Check on screen limit.
						if (headerScroll > 0) {
							headerState = STATE_ONSCREEN;
							headerScroll = 0;
							scrollDirection = 1;
						}
						// Check off screen limit
						if (headerScroll < -headerViewHeight) {
							headerState = STATE_OFFSCREEN;
							headerScroll = -headerViewHeight;
							scrollDirection = -1;
						}
						break;
				}
				// Translate header view
				translateHeaderView();
			}
		});
	}
	
	/**
	 * @see android.widget.FrameLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		android.view.ViewGroup.LayoutParams headerPlaceHolderParams = headerPlaceHolderView.getLayoutParams();
		headerPlaceHolderParams.width = headerView.getMeasuredWidth();
		headerPlaceHolderParams.height = headerView.getMeasuredHeight();
		headerPlaceHolderView.setLayoutParams(headerPlaceHolderParams);
		if (isQuickReturnEnable) {
			MarginLayoutParams params = (MarginLayoutParams)listView.getLayoutParams();
			params.topMargin = 0;
			listView.setLayoutParams(params);
		} else {
			headerScroll = 0;
			headerState = STATE_ONSCREEN;
			translateHeaderView();
			
			MarginLayoutParams params = (MarginLayoutParams)listView.getLayoutParams();
			params.topMargin = headerView.getMeasuredHeight();
			listView.setLayoutParams(params);
		}
	}
	
	/**
	 * Allows to set an OnScrollListener to ListView. QuickReturnLayout set its own OnScrollListener to the list, is you
	 * change it directly QuickReturnLayout will not work.
	 * 
	 * @param l listener
	 */
	public void setOnScrollListener(OnScrollListener l) {
		onScrollListener = l;
	}
	
	/**
	 * Allows to disable Quick Return behavior.
	 * 
	 * @param enable
	 */
	public void setQuickReturnEnable(boolean enable) {
		isQuickReturnEnable = enable;
		if (isQuickReturnEnable) {
			listView.addHeaderView(headerPlaceHolderView);
		} else {
			headerScroll = 0;
			headerState = STATE_ONSCREEN;
			translateHeaderView();
			listView.removeHeaderView(headerPlaceHolderView);
			
			MarginLayoutParams params = (MarginLayoutParams)listView.getLayoutParams();
			params.topMargin = headerView.getMeasuredHeight();
			listView.setLayoutParams(params);
		}
	}
	
	/**
	 * Translate the header view to current scroll position. This method provides support to pre-honeycomb android
	 * versions.
	 */
	private void translateHeaderView() {
		headerView.setTranslationY(headerScroll);
	}
	
	/**
	 * Creates an animation to show/close header if it is shown partially. TranslateAnimation works different in
	 * pre-honeycomb devices, so this method provides different animations for each Android version.
	 */
	private void translateAnimation() {
		final int headerViewHeight = headerView.getHeight() == 0 ? headerView.getMeasuredHeight()
				: headerView.getHeight();
		translateAnimationHoneycomb(headerViewHeight, listView.getFirstVisiblePosition());
	}
	
	private void translateAnimationHoneycomb(int headerViewHeight, int firstVisiblePosition) {
		final int finalHeaderState;
		final int finalHeaderScroll;
		final int translationY;
		
		if (scrollDirection > 0) {
			translationY = 0;
			finalHeaderState = STATE_ONSCREEN;
			finalHeaderScroll = 0;
		} else {
			if (firstVisiblePosition > 0) {
				translationY = -headerViewHeight;
				finalHeaderState = STATE_OFFSCREEN;
				finalHeaderScroll = -headerViewHeight;
			} else {
				
				translationY = oldChildTop;
				finalHeaderState = STATE_SCROLLING;
				finalHeaderScroll = oldChildTop;
			}
		}
		
		ObjectAnimator anim = ObjectAnimator.ofFloat(headerView, "y", translationY);
		anim.setDuration(200);
		anim.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isAnimating = false;
				headerState = finalHeaderState;
				headerScroll = finalHeaderScroll;
				translateHeaderView();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		anim.start();
	}
	
	public void smoothScrollToPosition(int position) {
		listView.smoothScrollToPositionFromTop(position, headerView.getHeight());
	}
	
	public void setSelectionAfterHeaderView() {
		listView.setSelectionAfterHeaderView();
	}
}
