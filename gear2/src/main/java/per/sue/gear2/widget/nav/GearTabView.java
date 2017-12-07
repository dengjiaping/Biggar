package per.sue.gear2.widget.nav;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by SUE on 2016/7/7 0007.
 */
public class GearTabView extends FrameLayout {

    public int mIndex;
    public int mMaxTabWidth;

    private TextView titleTextView ;
    private ImageView iconImageView;

    public GearTabView(Context context) {
        super(context);
    }

    public GearTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GearTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static GearTabView createTabView(Context context, int index, CharSequence text, int iconResId){
        GearTabView gearTabView = new GearTabView(context);
        gearTabView.mIndex = index;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        if(0 != iconResId){
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResId);
            gearTabView.setIconImageView(imageView);
            linearLayout.addView(imageView);
        }
        if(!TextUtils.isEmpty(text)){
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            gearTabView.setTitleTextView(textView);
            linearLayout.addView(textView);
        }
        // linearLayout.setPadding(padding, 0, padding, 0);
        gearTabView.addView(linearLayout, layoutParams);
        return gearTabView;
    }


    public int getIndex() {
        return mIndex;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public void setIconImageView(ImageView iconImageView) {
        this.iconImageView = iconImageView;
    }
}
