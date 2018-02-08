package cn.faury.android.library.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件过滤器类
 */
public class FFileFilter implements FilenameFilter {

    private List<String> extensions = new ArrayList<>();

    public FFileFilter(String... extensions) {
        for (String extension : extensions) {
            if (extension != null) {
                this.extensions.add(extension);
            }
        }
    }

    @Override
    public boolean accept(File dir, String filename) {
        boolean accept = false;
        if (filename != null && filename.trim().length() > 0) {
            for (String extension : extensions) {
                if (filename.endsWith(extension)) {
                    accept = true;
                    break;
                }
            }
        }
        return accept;
    }
}
