package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import q.rorbin.verticaltablayout.util.DisplayUtil;

import static android.R.attr.checked;
import static android.R.attr.gravity;
import static android.R.attr.icon;
import static android.R.attr.tileMode;
import static android.R.attr.title;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */
public class QTabView extends TabView {
    private Context mContext;
    private TextView mTitle;
    private Badge mBadgeView;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private TabBadge mTabBadge;
    private boolean mChecked;
    private Drawable mDefaultBackground;


    public QTabView(Context context) {
        super(context);
        mContext = context;
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder().build();
        mTabBadge = new TabBadge.Builder().build();
        initView();
        int[] attrs;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        } else {
            attrs = new int[]{android.R.attr.selectableItemBackground};
        }
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs);
        mDefaultBackground = a.getDrawable(0);
        a.recycle();
        setDefaultBackground();
    }

    private void initView() {
        setMinimumHeight(q.rorbin.badgeview.DisplayUtil.dp2px(mContext, 25));
        if (mTitle == null) {
            mTitle = new TextView(mContext);
            mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            mTitle.setLayoutParams(params);
            this.addView(mTitle);
        }
        initTitleView();
        initIconView();
        initBadge();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setPaddingRelative(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        mTitle.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        mTitle.setPadding(left, top, right, bottom);
    }

    private void initBadge() {
        mBadgeView = TabBadgeView.bindTab(this);
        if (mTabBadge.getBackgroundColor() != 0xFFE84E40) {
            mBadgeView.setBadgeBackgroundColor(mTabBadge.getBackgroundColor());
        }
        if (mTabBadge.getBadgeTextColor() != 0xFFFFFFFF) {
            mBadgeView.setBadgeTextColor(mTabBadge.getBadgeTextColor());
        }
        if (mTabBadge.getStrokeColor() != Color.TRANSPARENT || mTabBadge.getStrokeWidth() != 0) {
            mBadgeView.stroke(mTabBadge.getStrokeColor(), mTabBadge.getStrokeWidth(), true);
        }
        if (mTabBadge.getDrawableBackground() != null || mTabBadge.isDrawableBackgroundClip()) {
            mBadgeView.setBadgeBackground(mTabBadge.getDrawableBackground(), mTabBadge.isDrawableBackgroundClip());
        }
        if (mTabBadge.getBadgeTextSize() != 11) {
            mBadgeView.setBadgeTextSize(mTabBadge.getBadgeTextSize(), true);
        }
        if (mTabBadge.getBadgePadding() != 5) {
            mBadgeView.setBadgePadding(mTabBadge.getBadgePadding(), true);
        }
        if (mTabBadge.getBadgeNumber() != 0) {
            mBadgeView.setBadgeNumber(mTabBadge.getBadgeNumber());
        }
        if (mTabBadge.getBadgeText() != null) {
            mBadgeView.setBadgeText(mTabBadge.getBadgeText());
        }
        if (mTabBadge.getBadgeGravity() != (Gravity.END | Gravity.TOP)) {
            mBadgeView.setBadgeGravity(mTabBadge.getBadgeGravity());
        }
        if (mTabBadge.getGravityOffsetX() != 5 || mTabBadge.getGravityOffsetY() != 5) {
            mBadgeView.setGravityOffset(mTabBadge.getGravityOffsetX(), mTabBadge.getGravityOffsetY(), true);
        }
        if (mTabBadge.isExactMode()) {
            mBadgeView.setExactMode(mTabBadge.isExactMode());
        }
        if (!mTabBadge.isShowShadow()) {
            mBadgeView.setShowShadow(mTabBadge.isShowShadow());
        }
        if (mTabBadge.getOnDragStateChangedListener() != null) {
            mBadgeView.setOnDragStateChangedListener(mTabBadge.getOnDragStateChangedListener());
        }
    }

    private void initTitleView() {
        int[][] states = {{android.R.attr.state_checked, android.R.attr.state_selected}, {}};
        int[] colors = {mTabTitle.getColorSelected(), mTabTitle.getColorNormal()};
        mTitle.setTextColor(new ColorStateList(states, colors));
        mTitle.setTextSize(mTabTitle.getTitleTextSize());
        mTitle.setText(mTabTitle.getContent());
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        refreshDrawablePadding();
    }

    private void initIconView() {
        Drawable drawable = mTabIcon.getIcon();
        if (null != drawable && drawable.getBounds().isEmpty()) {
            int r = drawable.getIntrinsicWidth();
            int b = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, r, b);
        }

        TextView titleText = mTitle;
        switch (mTabIcon.getIconGravity()) {
            case Gravity.START:
                titleText.setCompoundDrawables(drawable, null, null, null);
                titleText.setPadding(titleText.getPaddingLeft() + mTabIcon.getMargin(), titleText.getPaddingTop(),
                        titleText.getPaddingRight(), titleText.getPaddingBottom());
                break;
            case Gravity.TOP:
                titleText.setCompoundDrawables(null, drawable, null, null);
                titleText.setPadding(titleText.getPaddingLeft(), titleText.getPaddingTop() + mTabIcon.getMargin(),
                        titleText.getPaddingRight(), titleText.getPaddingBottom());
                break;
            case Gravity.END:
                titleText.setCompoundDrawables(null, null, drawable, null);
                titleText.setPadding(titleText.getPaddingLeft(), titleText.getPaddingTop(),
                        titleText.getPaddingRight() + mTabIcon.getMargin(), titleText.getPaddingBottom());
                break;
            case Gravity.BOTTOM:
                titleText.setCompoundDrawables(null, null, null, drawable);
                titleText.setPadding(titleText.getPaddingLeft(), titleText.getPaddingTop(),
                        titleText.getPaddingRight(), titleText.getPaddingBottom() + mTabIcon.getMargin());
                break;
            default:
                break;
        }

        refreshDrawablePadding();
    }


    private void refreshDrawablePadding() {
        Drawable drawable = mTabIcon.getIcon();
        boolean hasDrawable = null != drawable;

        final int margin;
        if (hasDrawable && !TextUtils.isEmpty(mTabTitle.getContent())) {
            margin = mTabIcon.getMargin();
        } else {
            margin = 0;
        }

        TextView titleText = mTitle;
        titleText.setCompoundDrawablePadding(margin);
    }

    @Override
    public QTabView setBadge(TabBadge badge) {
        if (badge != null) {
            mTabBadge = badge;
        }
        initBadge();
        return this;
    }

    @Override
    public QTabView setIcon(TabIcon icon) {
        if (icon != null) {
            mTabIcon = icon;
        }
        initIconView();
        return this;
    }

    @Override
    public QTabView setTitle(TabTitle title) {
        if (title != null) {
            mTabTitle = title;
        }
        initTitleView();
        return this;
    }

    /**
     * @param resId The Drawable res to use as the background, if less than 0 will to remove the
     *              background
     */
    @Override
    public QTabView setBackground(int resId) {
        if (resId == 0) {
            setDefaultBackground();
        } else if (resId <= 0) {
            setBackground(null);
        } else {
            super.setBackgroundResource(resId);
        }
        return this;
    }

    @Override
    public TabBadge getBadge() {
        return mTabBadge;
    }

    @Override
    public TabIcon getIcon() {
        return mTabIcon;
    }

    @Override
    public TabTitle getTitle() {
        return mTabTitle;
    }

    @Override
    @Deprecated
    public ImageView getIconView() {
        return null;
    }

    @Override
    public TextView getTitleView() {
        return mTitle;
    }

    @Override
    public Badge getBadgeView() {
        return mBadgeView;
    }

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        setBackground(resid);
    }

    private void setDefaultBackground() {
        if (getBackground() != mDefaultBackground) {
            setBackground(mDefaultBackground);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setSelected(checked);
        mTitle.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}