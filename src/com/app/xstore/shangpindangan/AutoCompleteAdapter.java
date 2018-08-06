package com.app.xstore.shangpindangan;

import java.util.ArrayList;
import java.util.List;

import com.app.xstore.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AutoCompleteAdapter<T> extends BaseAdapter implements Filterable {

    private static final int MODE_NONE = 0x000;                 // 0000b
    public static final int MODE_CONTAINS = 0x001;              // 0001b
    public static final int MODE_STARTSWITH = 0x002;            // 0010b
    public static final int MODE_SPLIT = 0x004;                 // 0100b
    private static final String SPLIT_SEPARATOR = "[,.\\s]+";  // 分隔符，默认为空白符、英文逗号、英文句号
    private static boolean isFound = false;   // 当MODE_STARTSWITH模式匹配成功时，不再进行MODE_SPLIT模式的匹配
    private int defaultMode = MODE_STARTSWITH;                  // 0110b

    private LayoutInflater inflater;
    private ArrayFilter mArrayFilter;
    private List<T> mOriginalValues;      // 所有的item
    private List<T> mObjects;                  // 过滤后的item
    private final Object mLock = new Object();      // 同步锁
    private int maxMatch = 10;                      // 最多显示的item数目，负数表示全部
    private int simpleItemHeight;                   // 单行item的高度值，故需要在XML中固定父布局的高度值

    private char previewChar = '@';                 // 默认字符
    private boolean isSupportPreview = false;       // 是否可以使用@符号进行预览全部提示内容

    public AutoCompleteAdapter(Context context, List<T> mOriginalValues) {
        this(context, mOriginalValues, -1);
    }

    public AutoCompleteAdapter(Context context, List<T> mOriginalValues, int maxMatch) {
        this.mOriginalValues = mOriginalValues;
        // 初始化时将其设置成mOriginalValues，避免在未进行数据保存时执行删除操作导致程序的崩溃
        this.mObjects = mOriginalValues;   
        this.maxMatch = maxMatch;
        inflater = LayoutInflater.from(context);
        initViewHeight();
    }

    private void initViewHeight() {
        View view = inflater.inflate(R.layout.simple_dropdown_item_1line, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_item);
        linearLayout.measure(0, 0);
        // 其他方法获取的高度值会因View尚未被绘制而获取到0
        simpleItemHeight = linearLayout.getMeasuredHeight();
    }

    public int getSimpleItemHeight() {
        return simpleItemHeight;                // 5 * 2 + 28(dp) => 103(px)
    }
    
    public void setSupportPreview(boolean isSupportPreview){
        this.isSupportPreview = isSupportPreview;
    }

    public void setSupportPreview(boolean isSupportPreview, char previewChar){
        this.isSupportPreview = isSupportPreview;
        this.previewChar = previewChar;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_dropdown_item_1line, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_simple_item);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_simple_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mObjects.get(position).toString());
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T value = mObjects.remove(position);

                if (mDeleteListener != null) {
                    mDeleteListener.onSimpleItemDeletedListener(value);
                }

                if (mFilterListener != null) {
                    mFilterListener.onFilterResultsListener(mObjects.size());
                }

                mOriginalValues.remove(value);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tv;
        ImageView iv;
    }

    public void setDefaultMode(int defaultMode) {
        this.defaultMode = defaultMode;
    }

    public void add(T item) {
        mOriginalValues.add(item);
        notifyDataSetChanged();         //
    }

    public void clear() {
        if(mOriginalValues != null && !mOriginalValues.isEmpty()) {
            mOriginalValues.clear();
            notifyDataSetChanged();         //
        }
    }

    // Interface
    public interface OnSimpleItemDeletedListener<T> {
        void onSimpleItemDeletedListener(T value);
    }

    private OnSimpleItemDeletedListener<T> mDeleteListener;

    public void setOnSimpleItemDeletedListener(OnSimpleItemDeletedListener<T> listener) {
        this.mDeleteListener = listener;
    }

    // Interface
    public interface OnFilterResultsListener {
        void onFilterResultsListener(int count);
    }

    private OnFilterResultsListener mFilterListener;

    public void setOnFilterResultsListener(OnFilterResultsListener listener) {
        this.mFilterListener = listener;
    }

    @Override
    public Filter getFilter() {
        if (mArrayFilter == null) {
            mArrayFilter = new ArrayFilter(mFilterListener);
        }
        return mArrayFilter;
    }

    private class ArrayFilter extends Filter {

        private OnFilterResultsListener listener;

        public ArrayFilter(OnFilterResultsListener listener) {
            this.listener = listener;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                if (isSupportPreview) {
                    int index = prefix.toString().indexOf(String.valueOf(previewChar));
                    if (index != -1) {
                        prefix = prefix.toString().substring(index + 1);
                    }
                }
            
                String prefixString = prefix.toString().toLowerCase();      // prefixString
                final int count = mOriginalValues.size();                   // count
                final ArrayList<T> newValues = new ArrayList<T>(count); // newValues

                for (int i = 0; i < count; i++) {
                    final T value = mOriginalValues.get(i);            // value
                    final String valueText = value.toString().toLowerCase();           // valueText

                    // 1. 匹配所有
                    if ((defaultMode & MODE_CONTAINS) != MODE_NONE) {
                        if (valueText.contains(prefixString)) {
                            newValues.add(value);
                        }
                    } else {    // support: defaultMode = MODE_STARTSWITH | MODE_SPLIT
                        // 2. 匹配开头
                        if ((defaultMode & MODE_STARTSWITH) != MODE_NONE) {
                            if (valueText.startsWith(prefixString)) {
                                newValues.add(value);
                                isFound = true;
                            }
                        }
                        // 3. 分隔符匹配，效率低
                        if (!isFound && (defaultMode & MODE_SPLIT) != MODE_NONE) {
                            final String[] words = valueText.split(SPLIT_SEPARATOR);
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                        if(isFound) {   // 若在MODE_STARTSWITH模式中匹配，则再次复位进行下一次判断
                            isFound = false;
                        }
                    }

                    if (maxMatch > 0) {             // 限制显示item的数目
                        if (newValues.size() > maxMatch - 1) {
                            break;
                        }
                    }
                } // for (int i = 0; i < count; i++)
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;

            if (results.count > 0) {
                // 由于当删除提示框中的记录行时，而AutoCompleteTextView此时内容又不改变，故不会触发FilterResults事件
                // 导致删除记录行时，提示框的高度不会发生相应的改变
                // 解决方法：需要在ImageView的点击监听器中也调用OnFilterResultsListener.onFilterResultsListener()
                // 来共同完成
                if (listener != null) {
                    listener.onFilterResultsListener(results.count);
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

