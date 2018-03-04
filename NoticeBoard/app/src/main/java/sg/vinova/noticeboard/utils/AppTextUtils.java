package sg.vinova.noticeboard.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jacky on 6/15/17.
 */

public class AppTextUtils extends ClickableSpan {

    private OnTextClickListener listener;

    public AppTextUtils(OnTextClickListener listener){
        listener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (listener!=null) listener.onTextClick();
    }

    public interface OnTextClickListener {
        void onTextClick();
    }

    public static void ClickText(TextView view, final String clickableText,
                                final AppTextUtils.OnTextClickListener listener){
        CharSequence text = view.getText();
        String string = text.toString();
        AppTextUtils span = new AppTextUtils(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;
        if (text instanceof Spannable){
            ((Spannable) text).setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m==null) || !(m instanceof LinkMovementMethod)){
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
