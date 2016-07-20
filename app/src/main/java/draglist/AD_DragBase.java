package draglist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;

/**
 * Created by yeqinfu on 16-7-14.
 */

public abstract class AD_DragBase<T> extends BaseAdapter {
	protected List<T> ts;

	protected LayoutInflater inflater;

	protected boolean isMoving=false;//????????

	public AD_DragBase(Context context) {
		super();
		mContext=context;
		inflater = LayoutInflater.from(context);
		ts = new ArrayList<T>();
	}

	public void setDatas(List<T> t) {
		if (t != null && t.size() >= 0) {
			this.ts.clear();
			this.ts.addAll(t);
			notifyDataSetChanged();
		}
	}

	public void addDatas(List<T> t) {
		if (t != null && t.size() >= 0) {
			this.ts.addAll(t);
			notifyDataSetChanged();
		}
	}

	private Context mContext;

	/**
	 * ?????????Item
	 *
	 * @param showItem
	 */
	public void showDropItem(boolean showItem) {
		this.mShowItem = showItem;
	}

	/**
	 * ???????????
	 *
	 * @param position
	 */
	public void setInvisiblePosition(int position) {
		mInvisilePosition = position;
	}

	/**
	 * //?????drag listview getview??new????view??
	 * @param position
	 * @param convertView
	 * @param parent
     * @return
     */
	public abstract View initItemView(int position, View convertView, ViewGroup parent);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/***
		 * ??????????????????????ListView?????????.
		 * ?????????????????????????????????????LisView????
		 */
		convertView = initItemView(position,convertView,parent);
		if (isChanged) {// ?????????

			if (position == mInvisilePosition) {

				if (!mShowItem) {// ?????????????????Item????
					// ??item??????????????????????????????????
//					convertView.findViewById(R.id.drag_item_layout).setBackgroundColor(0x0000000000);
					convertView.setBackgroundColor(0x0000000000);
					int vis = View.INVISIBLE;
					setVisibilityUI(convertView, vis);
					// ??Item?????
				}else{
					convertView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
					int vis = View.VISIBLE;
					setVisibilityUI(convertView, vis);
				}

			}

			if (mLastFlag != -1) {

				if (mLastFlag == 1) {

					if (position > mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, -mHeight);
						convertView.startAnimation(animation);
					}

				}
				else if (mLastFlag == 0) {

					if (position < mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, mHeight);
						convertView.startAnimation(animation);
					}

				}

			}
		}

		return convertView;
	}

	private void
	setVisibilityUI(View convertView, int vis) {
		if (convertView instanceof ViewGroup){
            ViewGroup viewGroup= (ViewGroup) convertView;
            for (int i=0;i<viewGroup.getChildCount();i++){
                View view=viewGroup.getChildAt(i);
                view.setVisibility(vis);
            }
        }
	}

	protected int		mInvisilePosition	= -1;	// ???????Item???
	protected boolean	isChanged			= true;// ????????
	protected boolean	mShowItem			= false;// ????????Item???

	/***
	 * ????ListView???.
	 *
	 * @param startPosition
	 *            ?????position
	 * @param endPosition
	 *            ?????position
	 */
	public void exchange(int startPosition, int endPosition) {
		Object startObject = getItem(startPosition);

		if (startPosition < endPosition) {
			ts.add(endPosition + 1, (T) startObject);
			ts.remove(startPosition);
		}
		else {
			ts.add(endPosition, (T) startObject);
			ts.remove(startPosition + 1);
		}

		isChanged = true;
	}

	/**
	 * ????Item??
	 *
	 * @param startPosition
	 *            // ?????
	 * @param endPosition
	 *            // ???????
	 */
	public void exchangeCopy(int startPosition, int endPosition) {
		Object startObject = getCopyItem(startPosition);

		if (startPosition < endPosition) {// ????
			mCopyList.add(endPosition + 1, (T) startObject);
			mCopyList.remove(startPosition);
		}
		else {// ????????
			mCopyList.add(endPosition, (T) startObject);
			mCopyList.remove(startPosition + 1);
		}

		isChanged = true;
	}

	/**
	 * ?????Item
	 *
	 * @param pos
	 *            // ??????
	 */
	private void removeItem(int pos) {
		if (ts != null && ts.size() > pos) {
			ts.remove(pos);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * ????(??)Item?
	 *
	 * @param position
	 * @return
	 */
	public Object getCopyItem(int position) {
		return mCopyList.get(position);
	}

	/**
	 * ?????
	 *
	 * @param start
	 *            // ????????
	 * @param obj
	 */
	public void addDragItem(int start, Object obj) {
		ts.remove(start);// ????
		ts.add(start, (T) obj);// ?????
	}

	private ArrayList<T> mCopyList = new ArrayList<T>();

	public void copyList() {
		mCopyList.clear();
		for (T str : ts) {
			mCopyList.add(str);
		}
	}

	public void postList() {
		ts.clear();
		for (T str : mCopyList) {
			ts.add(str);
		}
		if (listener != null) {
			listener.onDragStop(ts);
		}
	}


	private boolean	isSameDragDirection	= true;	// ????????????

	public int getmLastFlag() {
		return mLastFlag;
	}

	private int		mLastFlag			= -1;
	private int		mHeight;
	private int		mDragPosition		= -1;

	/**
	 * ??????????????
	 *
	 * @param value
	 */
	public void setIsSameDragDirection(boolean value) {
		isSameDragDirection = value;
	}

	/**
	 * ????????
	 *
	 * @param flag
	 */
	public void setLastFlag(int flag) {
		mLastFlag = flag;
	}

	/**
	 * ????
	 *
	 * @param value
	 */
	public void setHeight(int value) {
		mHeight = value;
	}

	/**
	 * ????????
	 *
	 * @param position
	 */
	public void setCurrentDragPosition(int position) {
		mDragPosition = position;
	}

	/**
	 * ????????
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private Animation getFromSelfAnimation(int x, int y) {
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
		translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(100);
		translateAnimation.setInterpolator(new AccelerateInterpolator());
		return translateAnimation;
	}

	onDragStopListener listener;

	public void setOnDragStopListener(onDragStopListener listener) {
		this.listener = listener;
	}

	public interface onDragStopListener<T> {
		void onDragStop(List<T> afterList);
	}

	public List<T> getTs() {
		return ts;
	}

	@Override
	public int getCount() {
		return ts.size();
	}

	@Override
	public Object getItem(int position) {
		return ts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public T getItemObject(int position) {
		return ts.get(position);
	}
}
