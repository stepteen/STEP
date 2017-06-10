package example.com.step.util;
import android.content.Context;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_WORLD_READABLE;
import static android.content.Context.MODE_WORLD_WRITEABLE;

/**
 * Created by qinghua on 2016/11/21.
 */

public class RunFileDateUtil {


    private  List<String> items = new ArrayList<String>();//存放文件的名
    private List<String>  paths = new ArrayList<String>();//存放文件的路径
    /**
     * //读取跑步数据的文件内容
     */
    public String readFile(String fileName) throws IOException{
        File file = new File(fileName);
        String res;
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        res = EncodingUtils.getString(buffer, "UTF-8");
        fis.close();
        return res;
    }

    public List<String> getItems() {
        return items;
    }

    public List<String> getPaths() {
        return paths;
    }

    // 将跑步数据JSON字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath+"/"+fileName+".txt";
        String strContent = strcontent;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {

                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
            //FileOutputStream outStream = new FileOutputStream(file);
           // outStream.write(strContent.getBytes());
            //outStream.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath +"/"+ fileName+".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public void getFileDir() {
        try{
            items.clear();
            paths.clear();
            File f = new File("/mnt/sdcard/data/user/0/example.com.step/files");
            File[] files = f.listFiles();// 列出所有文件
            // 将所有文件存入list中
            if(files != null){
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    items.add(file.getName());
                    Log.d("zzz",file.getName());
                    paths.add(file.getPath());
                    Log.d("zzz",file.getPath());
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
