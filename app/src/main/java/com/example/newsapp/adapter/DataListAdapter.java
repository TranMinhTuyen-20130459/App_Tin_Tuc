package com.example.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.data.NewsDao;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class DataListAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;
    private ArrayList<News> mDataList;
    private boolean isShowViewed;   // true nếu đanh hiển thị các tin đã xem
    private ActionMode actionMode;
    private View rootView;

    public DataListAdapter(Context context, int resource, ArrayList<News> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.mResource = resource;
        this.mDataList = dataList;
    }

    public DataListAdapter(Context context, int resource, ArrayList<News> dataList, boolean isShowViewed, View root) {
        this(context, resource, dataList);
        this.isShowViewed = isShowViewed;
        this.rootView = root;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.news_item, null);
        }

        TextView title = view.findViewById(R.id.title);
        TextView timer = view.findViewById(R.id.time);
        ImageView image = view.findViewById(R.id.img);
        title.setText(mDataList.get(position).getTitle());
        timer.setText(mDataList.get(position).getDate());
//        Picasso.get().load(mDataList.get(position).getLinkImage()).into(image);

        /* Thực hiện các thao tác nhấn và nhấn giữ sẽ hiển thị một menu cho phép người dùng
         * xóa một hay nhiều item.
         * Chỉ có thể thực hiện khi danh sách này đang hiển thị các tin đã xem.
         */
        if (isShowViewed) {
            view.setOnLongClickListener(v -> {
                if (actionMode == null) {
                    // chưa bật ActionMode
                    AppCompatActivity activity = (AppCompatActivity) mContext;
                    actionMode = activity.startSupportActionMode(callback); // bật ActionMode
                }
                // Thực hiện thao tác nhấn trên item này.
                clickItem(position, v);
                return true;
            });

            view.setOnClickListener(v -> {
                if (actionMode != null) {
                    // Nếu đang bật ActionMode, thực hiện thao tác nhấn.
                    clickItem(position, v);
                } else {
                    // Nếu không bật ActionMode, mở trang tin tức.
                    Intent intent = new Intent(mContext, NewsDetailActivity.class)
                            .putExtra(Constants.KEY_NEWS_DETAILS, mDataList.get(position).getLink())
                            .putExtra(Constants.KEY_VIEWED_NEWS, mDataList.get(position));
                    mContext.startActivity(intent);
                }
            });
        }

        return view;
    }

    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.viewed_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                // Người dùng nhấn vào nút xóa.
                deleteNews();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            resetData();
        }
    };

    private void resetData() {
        mDataList.forEach(news -> news.setSelected(false));
        notifyDataSetChanged();
    }

    private void clickItem(int position, View view) {
        News news = mDataList.get(position);
        // Đảo ngược trạng thái, selected -> unselected và ngược lại.
        news.setSelected(!news.isSelected());
        view.setSelected(news.isSelected());

        if (mDataList.stream().noneMatch(News::isSelected)) {
            // Nếu không có item nào được nhấn, hủy ActionMode.
            actionMode.finish();
        }
    }

    /**
     * Phương thức được gọi khi người dùng nhấn vào nút delete để xóa các News đã chọn.
     */
    private void deleteNews() {
        AtomicBoolean undo = new AtomicBoolean(false);  // kiểm tra người dùng có nhấn UNDO hay không, true -> có.
        final ArrayList<News> oldNews = new ArrayList<>(mDataList); // giữ lại danh sách cũ.
        // Lấy ra các News người dùng đã chọn
        final List<News> selectedNews = mDataList.stream().filter(News::isSelected).collect(Collectors.toList());
        // và xóa chúng.
        mDataList.removeAll(selectedNews);
        notifyDataSetChanged();
        // Tạo một SnackBar xuất hiện ngay sau khi người dùng nhấn xóa.
        Snackbar.make(rootView, "Deletes " + selectedNews.size() + " news", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // Người dùng nhấn UNDO, lấy lại danh sách cũ đã lưu trước đó.
                    mDataList = oldNews;
                    resetData();
                    undo.set(true);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        // Khi SnackBar biến mất, kiểm tra người dùng có nhấn UNDO hay không.
                        if (!undo.get()) {
                            // Nếu không, xóa news trong database.
                            NewsDao dao = new NewsDao(mContext);
                            if (dao.removeNews(extractLinks(selectedNews))) {
                                Toast.makeText(mContext, "Delete successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .show();
        actionMode.finish();
    }

    /**
     * Chuyển đổi danh sách các News thành mảng chứa link của các News dó.
     * Dùng làm tham số cho mệnh đề where của các câu truy vấn.
     */
    private String[] extractLinks(List<News> news) {
        String[] result = new String[news.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = news.get(i).getLink();
        }
        return result;
    }
}

