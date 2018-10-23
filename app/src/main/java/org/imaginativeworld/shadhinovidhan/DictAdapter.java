package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.imaginativeworld.shadhinovidhan.listeners.ClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DictAdapter extends RecyclerView.Adapter<DictAdapter.DictViewHolder> {

    private static ClickListener clickListener;

    CursorAdapter mCursorAdapter;

    Context mContext;

    public DictAdapter(Context context, Cursor cursor) {
        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {

            private SimpleCursorAdapter.ViewBinder mViewBinder;

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context)
                        .inflate(R.layout.result_list_layout, viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                final SimpleCursorAdapter.ViewBinder binder = mViewBinder;
                final int[] from = new int[]{
                        1,
                        2,
                        3
                };
                final int[] to = new int[]{
                        R.id.txt_pron,
                        R.id.txt_meaning,
                        R.id.txt_synonyms
                };

                final int count = to.length;

                for (int i = 0; i < count; i++) {
                    final View v = view.findViewById(to[i]);
                    if (v != null) {
                        boolean bound = false;
                        if (binder != null) {
                            bound = binder.setViewValue(v, cursor, from[i]);
                        }

                        if (!bound) {
                            String text = cursor.getString(from[i]);
                            String text2 = text;

                            // Separate and format the meaning
                            if (i == 1) { // meaning view
                                text = so_tools.meaning_htmlfy(text);
                            }

                            if (text == null) {
                                text = "";
                            }

                            if (v instanceof TextView) {

                                if (i == 1) { // meaning view

                                    ((TextView) v).setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                                    /**
                                     * The text of the TextView is compiled in html
                                     * so we save the string in TAG properties.. :)
                                     */
                                    v.setTag(text2);

                                } else {
                                    ((TextView) v).setText(text);
//                                    setViewText((TextView) v, text);
                                }
                            } else {
                                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                        " view that can be bounds by this CSCA");
                            }
                        }
                    }
                }

            }
        };
    }

    @NonNull
    @Override
    public DictAdapter.DictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.result_list_layout, parent, false);

//        DictViewHolder vh = new DictViewHolder(v);
//        return vh;

        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new DictViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull DictAdapter.DictViewHolder holder, int position) {
//        holder.mTextView.setText(mDataset[position]);
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DictAdapter.clickListener = clickListener;
    }

    /**
     * ***************************************************************************************
     * ***************************************************************************************
     * ***************************************************************************************
     */

    public static class DictViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        View v;

        public DictViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

}
