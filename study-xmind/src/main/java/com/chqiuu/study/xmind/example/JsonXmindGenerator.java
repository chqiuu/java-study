package com.chqiuu.study.xmind.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import org.xmind.core.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chqiu
 */
public class JsonXmindGenerator {

    /**
     * 当前类路径
     */
    public static final String CLASS_PATH = JsonXmindGenerator.class.getResource("/").getPath();
    /**
     * 文件分隔符
     */
    public static final String FILE_SEPARATOR = SystemUtil.getOsInfo().getFileSeparator();

    public static void main(String[] args) throws IOException, CoreException {
        // 读取目录
        String jsonName = "P7架构学习路线";
        JSONObject json = JSONUtil.readJSONObject(new File(CLASS_PATH + jsonName + ".json"), Charset.defaultCharset());
        // 创建思维导图的工作空间
        IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
        IWorkbook workbook = workbookBuilder.createWorkbook();
        // 获得默认sheet
        ISheet primarySheet = workbook.getPrimarySheet();
        // 获得根主题
        ITopic rootTopic = primarySheet.getRootTopic();
        // 设置根主题的标题
        rootTopic.setTitleText(json.getStr("title"));
        JSONArray jsonArray = json.getJSONArray("children");
        jsonArray.forEach(a -> {
            jsonConvertXmind(workbook, rootTopic, (JSONObject) a);
        });

        // 保存
        workbook.save(CLASS_PATH + FILE_SEPARATOR + jsonName + ".xmind");
    }

    private static void jsonConvertXmind(IWorkbook workbook, ITopic parantTopic, JSONObject jsonObject) {
        // 创建章节节点
        ITopic topic = workbook.createTopic();
        topic.setTitleText(jsonObject.getStr("title"));
        JSONArray jsonArray = jsonObject.getJSONArray("children");
        jsonArray.forEach(a -> {
            jsonConvertXmind(workbook, topic, (JSONObject) a);
        });
        parantTopic.add(topic);
    }
}
