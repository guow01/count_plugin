import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Guow
 * @Description:
 * @Date 2018/8/28 9:49
 */
@Mojo(name = "findCountTotal",requiresProject = false, defaultPhase = LifecyclePhase.PACKAGE)
public class CountMojo extends AbstractMojo {
    /**
     * @Author Guow
     * @Description: 这个集合石用来放文件的
     * @Date 2018/8/28 10:36
     */
    private static List<String> fileList = new ArrayList<String>();

    private int allLines = 0;

    /**
     * @Author Guow
     * @Description:  这个参数是引入本插件的工程传进来的文件夹
     * @Date 2018/8/28 10:36
     */
    @Parameter(property = "currentBaseDir",defaultValue = "User/pathHome")
    private String currentBaseDir;

    /**
     * @Author Guow
     * @Description:  这个参数是引入本插件的工程传入进来的文件类型 (例如.java)
     * @Date 2018/8/28 10:36
     */
    @Parameter(property = "suffix",defaultValue = ".java")
    private String suffix;

    public void execute() throws MojoExecutionException, MojoFailureException {
        List<String> fileList = scanFile(currentBaseDir);
        System.out.println("FilePath:" + currentBaseDir);
        System.out.println("FileSuffix:" + suffix);
        System.out.println("FileTotal:"+fileList.size());
        System.out.println("allLines:"+allLines);
    }

    /**
     * 递归统计文件,将所有符合条件的文件放入集合中
     */
    private List<String> scanFile(String filePath) {
        File dir = new File(filePath);
        // 递归查找所有的class文件
        for(File file : dir.listFiles()) {
            // 如果是文件夹
            if(file.isDirectory()) {
                // 进入递归 ,参数是遍历到的当前文件夹的绝对路径
                scanFile(file.getAbsolutePath());
            } else {
                if(file.getName().endsWith(suffix)) {
                    // 符合条件 添加到集合中
                    fileList.add(file.getName());
                    // 统计行数
                    allLines+=countLines(file);
                }
            }
        }
        return fileList;
    }
    private int countLines(File file) {
        int lines = 0;
        try {
            // 转换成高级流  可以按行读
            BufferedReader reader =  new BufferedReader(new FileReader(file));
            while(reader.ready()) {
                reader.readLine();
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
