package cn.reinforce.utils;

import java.io.File;

/**
 * 文件相关方法
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class FileUtil {

    private FileUtil(){

    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 检测文件是否为图片
     *
     * @param filename 检测的文件名
     * @return true 图片,false其他文件
     */
    public static boolean isPic(String filename) {

        String reg = "[\\s\\S]+.(png|gif|bmp|jpg|jpeg)";

        return filename.toLowerCase().matches(reg);
    }

    /**
     * 检测文件是否为FreeMarker的模版文件
     *
     * @param fileName
     * @return
     */
    public static boolean isFreeMarkerTemplate(String fileName) {

        String reg = "[\\s\\S]+.(ftl)";

        return fileName.toLowerCase().matches(reg);
    }
}
