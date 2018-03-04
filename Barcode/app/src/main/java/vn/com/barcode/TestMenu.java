package vn.com.barcode;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import junit.framework.Test;

import vn.com.barcode.popup.MultiMenuPopup;
import vn.com.barcode.popup.RelativePopupWindow;

/**
 * Created by Jacky Hua on 04/03/2018.
 */

public class TestMenu extends Activity  implements MultiMenuPopup.OnClickPopupMenuListener {
    Button btnShow;
    private boolean hideEditMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_menu_activity);
        btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditMenu = false;

                MultiMenuPopup menu = new MultiMenuPopup(getBaseContext(), view, hideEditMenu, !hideEditMenu);
                menu.setOnClickPopupMenuListener(TestMenu.this);
                menu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.BELOW,
                        RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT);

            }
        });
    }

    @Override
    public void onClickEdit() {

    }

    @Override
    public void onClickDelete() {

    }

    @Override
    public void blockAuthor() {

    }

    @Override
    public void report() {

    }
}
