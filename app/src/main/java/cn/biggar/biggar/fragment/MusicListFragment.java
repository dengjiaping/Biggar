package cn.biggar.biggar.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import cn.biggar.biggar.adapter.MusicListAdapter;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.MusicBean;
import cn.biggar.biggar.event.MusicSelectEvent;
import cn.biggar.biggar.utils.FileUtils;
import cn.biggar.biggar.utils.StorageManager;
import cn.biggar.biggar.utils.ToastUtils;

import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;


import per.sue.gear2.utils.BitmapUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class MusicListFragment extends Fragment {

    private  ListView musicListView;
    private MusicListAdapter musicListAdapter;
    private String selection;
    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");

    public static MusicListFragment getInstance() {
        MusicListFragment fragment = new MusicListFragment();
        return fragment;
    }

    public static MusicListFragment getInstance(String selection) {
        MusicListFragment fragment = new MusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selection", selection);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, null);
        musicListView = (ListView)view.findViewById(R.id.music_listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null != getArguments()){
            selection = getArguments().getString("selection");
            //selection = "  + selection + " ";
        }
        musicListAdapter = new MusicListAdapter(getActivity());
        musicListView.setAdapter(musicListAdapter);
            musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicBean musicBean = musicListAdapter.getItem(position);
                EventBus.getDefault().post(new MusicSelectEvent(musicBean));
                QupaiService qupaiService = QupaiManager.getQupaiService(getContext());
                if(null != qupaiService){
                    moveMusicFileToAPPPath(musicBean);
                }
                getActivity().finish();

            }
        });
        loadData();
    }

    private void moveMusicFileToAPPPath( MusicBean musicBean ){
        String musicName = "audio.mp3";
        String musicImage = "icon_without_name.png";

        String fileName = musicBean.getUrl().substring(musicBean.getUrl().lastIndexOf("/")+1);
        String folderName = fileName.substring(0,fileName.lastIndexOf("."));
        String rootPath = StorageManager.getInstance().getMusicPath(getActivity().getApplicationContext());

        File file = new File(rootPath + "/" + folderName);
        if(!file.exists()){
            file.mkdirs();
        }
        String musicNewPath = new StringBuilder(rootPath).append(File.separator)
                .append(folderName).append(File.separator).append(musicName).toString();
        String musicImagePath =  new StringBuilder(rootPath).append(File.separator)
                .append(folderName).append(File.separator).append(musicImage).toString();


       boolean b = FileUtils.copyFileByChannels(musicBean.getUrl(),  musicNewPath);

        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, musicBean.getAlbumId());
        if(null != albumArtUri){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),albumArtUri);
                File imageFile = new File(musicImagePath);
              //  imageFile.mkdir();
                BitmapUtils.bitmapTofile(imageFile, bitmap);
            } catch (Exception exception) {
                exception.printStackTrace();
                // log error
            }
        }
        String path =  rootPath + "/"+ folderName;
        QupaiService qupaiService = QupaiManager.getQupaiService(getContext());
        qupaiService.addMusic(Constants.getMusicId(), musicBean.getTitle(), Uri.fromFile(new File(path)).getPath());
    }

    private void loadData(){
        Observable.create(new Observable.OnSubscribe<ArrayList<MusicBean>>() {
            @Override
            public void call(Subscriber<? super ArrayList<MusicBean>> subscriber) {
                subscriber.onNext(queryData());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<MusicBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showError("加载数据失败");
                    }

                    @Override
                    public void onNext(ArrayList<MusicBean> musicBeens) {
                        musicListAdapter.setList(musicBeens);
                    }
                });
    }

    private  ArrayList<MusicBean> queryData(){
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                selection, // 查询条件
                null, // 条件的对应?的参数
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);// 排序方式
        ArrayList<MusicBean>list = new ArrayList<>();
        if(null != cursor){
            while(cursor.moveToNext()){
                list.add(getMusicBeanByCursor(cursor));
            }
        }
        cursor.close();
       return list;
    }

    private MusicBean getMusicBeanByCursor(Cursor cursor){
        // 找到歌曲标题和总时间对应的列索引
        String strTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));//歌名
        String strARTIST = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));//艺术家
        String strALBUM = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));//专辑
        String url = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));//路径
        long size = cursor.getLong( cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));//大小

        MusicBean musicBean = new MusicBean();
        musicBean.setTitle(strTitle);
        musicBean.setArtist(strARTIST);
        musicBean.setAlbum(strALBUM);
        musicBean.setAlbumId(cursor.getLong( cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)));
        musicBean.setUrl(url);
        musicBean.setSize(size);
        return musicBean;
    }

}
