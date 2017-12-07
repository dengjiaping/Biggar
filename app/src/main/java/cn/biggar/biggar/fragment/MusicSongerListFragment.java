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
public class MusicSongerListFragment extends Fragment {

    private ListView musicSongerListView;
    private MusicContainerAdapter musicContainerAdapter;
    private final   String NUM_OF_SONGS = "num_of_songs";
    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");

    public static MusicSongerListFragment getInstance() {
        MusicSongerListFragment fragment = new MusicSongerListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_songer_list, null);
        musicSongerListView = (ListView)view.findViewById(R.id.music_songer_listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musicContainerAdapter = new MusicContainerAdapter(getContext());
        musicSongerListView.setAdapter(musicContainerAdapter);
        musicSongerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicContainer musicContainer = musicContainerAdapter.getItem(position);
                String selection =  MediaStore.Audio.AudioColumns.ARTIST +" = '" + musicContainer.getTitle()+"'";
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
                "count(" +  MediaStore.Audio.AudioColumns.ARTIST + ") as "
                        + NUM_OF_SONGS
        };
        /** where子句 */
        String selection = "1 = 1 ) "
                + " group by ( " + MediaStore.Audio.AudioColumns.ARTIST;

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
        int indexAlbumId = cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ALBUM_ID);
        int indexArtist =  cursor.getColumnIndex( MediaStore.Audio.AudioColumns.ARTIST);
       // int indexData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        int indexNum = cursor.getColumnIndex(NUM_OF_SONGS);
        int musicCount;
        String filePath;
        String fileName;
        MusicContainer item = new MusicContainer();

        // 获取每个目录下的歌曲数量
        musicCount = cursor.getInt(indexNum);
        String describe = getActivity().getResources().getString(R.string.music_label_music_count, musicCount);
        item.setSubhead(describe);
        fileName = cursor.getString(indexArtist);
        int albumId = cursor.getInt(indexAlbumId);
        item.setTitle(fileName);
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);
        item.setAlbumArtURI(albumArtUri);

        return item;
    }

}
