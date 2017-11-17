package razerdp.github.com.ui.interpolator;

import android.view.animation.LinearInterpolator;

/**
 * Created by 大灯泡 on 2017/3/24.
 */

public class SpringInterpolator extends LinearInterpolator {
    private float factor;

    public SpringInterpolator() {
        factor = 0.4f;
    }

    public SpringInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
