package cn.biggar.biggar.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.MusicSeletActivity;
import cn.biggar.biggar.adapter.MusicContainerAdapter;
import cn.biggar.biggar.bean.MusicContainer;

import java.util.ArrayList;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicAlbumListFragment  extends Fragment {

    private ListView musicAlbumListView;
    private MusicContainerAdapter musicContainerAdapter;
    private final   String NUM_OF_SONGS = "num_of_songs";
    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");

    public static MusicAlbumListFragment getInstance() {
        MusicAlbumListFragment fragment = new MusicAlbumListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_album_list, null);
        musicAlbumListView = (ListView)view.findViewById(R.id.music_album_listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musicContainerAdapter = new MusicContainerAdapter(getContext());
        musicAlbumListView.setAdapter(musicContainerAdapter);
        musicAlbumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicContainer musicContainer = musicContainerAdapter.getItem(position);
                String selection =  MediaStore.Audio.AudioColumns.ALBUM +" = '" + musicContainer.getTitle()+"'";
                getActivity().startActivity(MusicSeletActivity.startIntent(getActivity(),selection ));
            }
        });
        loadData();
    }

    private void loadData(){
        Observable.create(new Observable.OnSubscribe<ArrayList<MusicContainer>>(){
            @Override
            public void call(Subscriber<? super ArrayList<MusicContainer>> subscriber) {
                subscriber.onNext(queryData());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<MusicContainer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<MusicContainer> musicBeens) {
                        musicContainerAdapter.setList(musicBeens);
                    }
                });
    }

    private ArrayList<MusicContainer> queryData(){
        /** 要从MediaStore检索的列 */
        final String[] projection = new String[] {
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST,
                "count(" +  MediaStore.Audio.AudioColumns.ALBUM + ") as "
                        + NUM_OF_SONGS
        };
        /** where子句 */
        String selection = "1 = 1 ) "
                + " group by ( " + MediaStore.Audio.AudioColumns.ALBUM;

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                selection, // 查询条件
                null, // 条件的对应?的参数
                null);// 排序方式


        ArrayList<MusicContainer>listData = new ArrayList<MusicContainer>();
        while (cursor.moveToNext()) {
            listData.add(getFolderByCursor(cursor));
        }
        cursor.close();
        return listData;
    }

    private  MusicContainer getFolderByCursor( Cursor cursor){

        MusicContainer item = new MusicContainer();
        int musicCount = cursor.getInt(cursor.getColumnIndex(NUM_OF_SONGS));
        String  fileName = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ALBUM));
        String artist =  cursor.getString( cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ARTIST));
        int albumId = cursor.getInt(cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ALBUM_ID));
        String describe = getActivity().getResources().getString(R.string.music_label_music_count, musicCount);
        describe = describe + artist;
        item.setSubhead(describe);
        item.setTitle(fileName);
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);
        item.setAlbumArtURI(albumArtUri);

        return item;
    }

}
