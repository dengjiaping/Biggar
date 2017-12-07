package cn.biggar.biggar.helper;

import android.hardware.Camera;

import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;

import cn.biggar.biggar.app.Constants;


/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/5
*/
public class QPHelper {


    public VideoSessionCreateInfo createVideoSessionCreateInfo(){

        //缩略图参数,可设置取得缩略图的数量，默认10张
        ThumbnailExportOptions thumbnailExportOptions =new ThumbnailExportOptions.Builder()
                .setCount(10).get();

        VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                .setWaterMarkPath(Constants.WATER_MARK_PATH)
                .setWaterMarkPosition(1)
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setBeautyProgress(100)
                .setBeautySkinOn(true)
                .setMovieExportOptions(createMovieExportOptions())
                .setThumbnailExportOptions(thumbnailExportOptions)
                .build();
        return info;
    }


    public MovieExportOptions createMovieExportOptions(){
        //压缩参数
        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                .setVideoBitrate(Constants.DEFAULT_BITRATE)
                .configureMuxer("movflags", "+faststart")
                .build();
        return movie_options;
    }

    public ProjectOptions createProjectOptions(){
        ProjectOptions projectOptions = new ProjectOptions.Builder()
                .setDurationRange(3, Constants.DEFAULT_DURATION_LIMIT)
                .setVideoSize(480, 480)
                .setVideoFrameRate(30)//帧率
                .get();
        return projectOptions;
    }
}
