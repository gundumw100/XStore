package com.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 图片缩放时java.lang.IllegalArgumentException: pointerIndex out of range解决方案
 * 
 * @see http://blog.csdn.net/nnmmbb/article/details/28419779
 * @author Ni Guijun
 *
 */
public class FixedViewPager extends android.support.v4.view.ViewPager {  
	  
    public FixedViewPager(Context context) {  
        super(context);  
    }  
  
    public FixedViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        try {  
            return super.onTouchEvent(ev);  
        } catch (IllegalArgumentException ex) {  
            ex.printStackTrace();  
        }  
        return false;  
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        try {  
            return super.onInterceptTouchEvent(ev);  
        } catch (IllegalArgumentException ex) {  
            ex.printStackTrace();  
        }  
        return false;  
    }  
}  

