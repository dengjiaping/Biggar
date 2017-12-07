package cn.biggar.biggar.fragment;

import android.database.Cursor;
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
import cn.biggar.biggar.adapter.MusicFolderAdapter;
import cn.biggar.biggar.bean.FolderBean;

import java.io.File;
import java.util.ArrayList;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicFolderFragment  extends Fragment {

    private ListView musicFolderListView;
    private MusicFolderAdapter musicFolderAdapter;

    private final   String NUM_OF_SONGS = "num_of_songs";

    public static MusicFolderFragment getInstance() {
        MusicFolderFragment fragment = new MusicFolderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_folder_list, null);
        musicFolderListView = (ListView)view.findViewById(R.id.music_folder_listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musicFolderAdapter = new MusicFolderAdapter(getActivity());
        musicFolderListView.setAdapter(musicFolderAdapter);
        musicFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FolderBean folderBean = musicFolderAdapter.getItem(position);
                String selection =  MediaStore.Audio.AudioColumns.DATA  +" like '" + folderBean.getFolderPath() +"%'";
                getActivity().startActivity(MusicSeletActivity.startIntent(getActivity(),selection ));
            }
        });
        loadData();
    }

    private void loadData(){
        Observable.create(new Observable.OnSubscribe<ArrayList<FolderBean>>(){
            @Override
            public void call(Subscriber<? super ArrayList<FolderBean>> subscriber) {
                subscriber.onNext(queryData());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<FolderBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<FolderBean> musicBeens) {
                        musicFolderAdapter.setList(musicBeens);
                    }
                });
    }

    private ArrayList<FolderBean> queryData(){
        /** 要从MediaStore检索的列 */
        final String[] projection = new String[] {
                MediaStore.Files.FileColumns.DATA,
                "count(" + MediaStore.Files.FileColumns.PARENT + ") as "
                        + NUM_OF_SONGS };
        /** where子句 */
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " = "
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " ) "
                + " group by ( " + MediaStore.Files.FileColumns.PARENT;

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                projection,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                selection, // 查询条件
                null, // 条件的对应?的参数
                null);// 排序方式


        ArrayList<FolderBean>listData = new ArrayList<FolderBean>();
        while (cursor.moveToNext()) {
            listData.add(getFolderByCursor(cursor));
        }
        cursor.close();
        return listData;
    }

    private  FolderBean getFolderByCursor( Cursor cursor){
        int indexData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
        int indexNum = cursor.getColumnIndex(NUM_OF_SONGS);
        int musicCount;
        String filePath;
        String fileName;
        FolderBean item = new FolderBean();

        // 获取每个目录下的歌曲数量
        musicCount = cursor.getInt(indexNum);
        item.setFileCount(musicCount);

        // 获取文件的路径，如/storage/sdcard0/MIUI/music/Baby.mp3
        filePath = cursor.getString(indexData);

        // 获取文件所属文件夹的路径，如/storage/sdcard0/MIUI/music
        filePath = filePath.substring(0,
                filePath.lastIndexOf(File.separator));

        // 获取文件所属文件夹的名称，如music
        fileName = filePath.substring(filePath
                .lastIndexOf(File.separator) + 1);
        item.setFolderName(fileName);
        item.setFolderPath(filePath);
        return item;
    }
}
