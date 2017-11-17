package razerdp.github.com.lib.interfaces.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by 大灯泡 on 2017/10/12.
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
