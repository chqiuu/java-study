package com.chqiuu.study.xmind.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.system.SystemUtil;
import org.xmind.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chqiu
 */
public class DoubanBookXmindGenerator {

    /**
     * 当前类路径
     */
    public static final String CLASS_PATH = DoubanBookXmindGenerator.class.getResource("/").getPath();
    /**
     * 文件分隔符
     */
    public static final String FILE_SEPARATOR = SystemUtil.getOsInfo().getFileSeparator();

    public static void main(String[] args) throws IOException, CoreException {
        // 读取目录
        String bookName = "一本书读懂24种互联网思维";
        List<String> contents = FileUtil.readLines(CLASS_PATH + bookName + ".txt", "utf-8");

        // 创建思维导图的工作空间
        IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
        IWorkbook workbook = workbookBuilder.createWorkbook();

        // 获得默认sheet
        ISheet primarySheet = workbook.getPrimarySheet();

        // 获得根主题
        ITopic rootTopic = primarySheet.getRootTopic();
        // 设置根主题的标题
        rootTopic.setTitleText(bookName);

        // 章节 topic 的列表
        ArrayList<ITopic> chapterTopics = new ArrayList<>();
        for (String content : contents) {
            // 如果是数字开头为章节名称
            if (ReUtil.isMatch("^[1-24].*?", content)) {
                // 创建章节节点
                ITopic topic = workbook.createTopic();
                topic.setTitleText(content);
                chapterTopics.add(topic);
            } else {
                // 创建小节节点
                ITopic topic = workbook.createTopic();
                topic.setTitleText(content);
                chapterTopics.get(chapterTopics.size() - 1).add(topic, ITopic.ATTACHED);
            }
        }
        // 把章节节点添加到要节点上
        chapterTopics.forEach(it -> rootTopic.add(it, ITopic.ATTACHED));
        // 保存
        workbook.save(CLASS_PATH + FILE_SEPARATOR + bookName + ".xmind");
    }
}
