package per.sue.gear2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import per.sue.gear2.R;


/**
 *
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 *
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author antoine vianey
 *
 */
public class GearCustomDialog extends Dialog {

    public GearCustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public GearCustomDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;

        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private ListView listView;

        private String leftText;
        private String rightText;
        private int  rightTextColor;
        private int  leftTextColor;

        private double perHeight;



        private boolean isBottomUp ;

        private OnClickListener
                positiveButtonClickListener,
                negativeButtonClickListener,
                leftTextonClickListener,
                rightTextonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setBottomUp(boolean  b) {
            isBottomUp = b;
            return this;
        }

        public Builder setPerHeight(double perheight) {
            this.perHeight = perheight;
            return this;
        }

        /**
         * Set the Dialog message from String
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder addLeftText(String text, OnClickListener listener ){
            this.leftText = text;
            this.leftTextonClickListener = listener;
            return this;
        }

        public Builder addRightText(String text, OnClickListener listener ){
            this.rightText = text;
            this.rightTextonClickListener = listener;
            return this;
        }

        public Builder addRightText(String text,int color, OnClickListener listener ){
            this.rightText = text;
            this.rightTextonClickListener = listener;
            this.rightTextColor = color;
            return this;
        }

        public Builder addRightText(String text){
            this.rightText = text;
            return this;
        }

        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder addListView(List<?> items, AdapterView.OnItemClickListener onItemClick){
            String[] arr = new String[items.size()];
            items.toArray(arr);
            int i = 0;
            for (Object obj : items) {
               if(null != obj) arr[i] = obj.toString();
                i++;
            }
            return addListView(arr, 0, 0, onItemClick);
        }

        public Builder addListView(String[] items, AdapterView.OnItemClickListener onItemClick){
            return addListView(items, 0, 0, onItemClick);
        }

        public Builder addListView(String[] items, int selectTyleResId, int styleResId, AdapterView.OnItemClickListener onItemClick){
            int resId = android.R.layout.simple_list_item_single_choice;
            if(selectTyleResId != 0){
                resId =  selectTyleResId;
            }
            if(Build.VERSION.SDK_INT >= 21){
                listView = new ListView(context, null, 0, styleResId);
            }else{
                listView = new ListView(context, null, styleResId);
            }

            listView.setAdapter(new ArrayAdapter<>(context, resId, items));
            listView.setOnItemClickListener(onItemClick);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            return this;
        }

        public  Builder addListView(ListView listView){
            this.listView = listView;
            return this;
        }

        public ListView getListView(){
            return listView;
        }

        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public GearCustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final GearCustomDialog dialog = new GearCustomDialog(context,
                    R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.dialog_custom_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // set the dialog title
            if(!TextUtils.isEmpty(title) ){
                ((TextView) layout.findViewById(R.id.title)).setText(title);
                ((TextView) layout.findViewById(R.id.title)).setVisibility(View.VISIBLE);
            }else{
                ((TextView) layout.findViewById(R.id.title)).setVisibility(View.GONE);
            }



            if(TextUtils.isEmpty(title) ){
                layout.findViewById(R.id.title_layout).setVisibility(View.GONE);

            }else{
                layout.findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
            }

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }

            if(negativeButtonText == null && positiveButtonText == null){
                layout.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
            }else{
                layout.findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
            }

            if(leftText != null) {
                TextView leftTextView = ((TextView) layout.findViewById(R.id.leftOpt));
                leftTextView .setText(leftText);
                if(leftTextonClickListener != null){
                    leftTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            leftTextonClickListener.onClick(dialog,0);
                        }
                    });
                }

            }

            if(rightText != null) {
                TextView rightTextView = ((TextView) layout.findViewById(R.id.rightOpt));
                rightTextView .setText(rightText);
                rightTextView.setTextColor(rightTextColor);

                if(rightTextonClickListener != null){
                    rightTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rightTextonClickListener.onClick(dialog,0);
                        }
                    });
                }
            }
            if(leftText == null && rightText == null){
                layout.findViewById(R.id.top_layout).setVisibility(View.GONE);
            }else{
                layout.findViewById(R.id.top_layout).setVisibility(View.VISIBLE);
            }

            // set the content message
            if (message != null ) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }else{
                layout.findViewById(R.id.message).setVisibility(View.GONE);
            }

            if(listView  != null){
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            dialog.setContentView(layout);
            if(isBottomUp){
                setBottomUp(dialog);
            }
            return dialog;
        }

        private void setBottomUp(Dialog dialog){
            Window window = dialog.getWindow();
            WindowManager windowManager =window.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); // 设置宽度
            if(perHeight != 0)
                lp.height = (int) (perHeight * display.getHeight());
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setWindowAnimations(R.style.AnimBottom);
            dialog.setCanceledOnTouchOutside(true);
            window.setGravity(Gravity.BOTTOM);
        }

    }




}