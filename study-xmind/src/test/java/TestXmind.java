import org.xmind.core.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestXmind {

    @Test
    public void test() throws IOException, CoreException {

        IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
        IWorkbook workbook = workbookBuilder.createWorkbook();

        // 获得默认sheet
        ISheet primarySheet = workbook.getPrimarySheet();
        // 获得根主题
        ITopic rootTopic = primarySheet.getRootTopic();
        rootTopic.setTitleText("根主题");

        // 创建主题
        ITopic topic1 = workbook.createTopic();
        topic1.setTitleText("子主题1");
        // 添加子主题
        rootTopic.add(topic1, ITopic.ATTACHED);
        // 创建主题
        ITopic topic2 = workbook.createTopic();
        topic2.setTitleText("子主题2");

        // 创建主题
        ITopic topic3 = workbook.createTopic();
        topic3.setTitleText("子主题3");
        topic2.add(topic3, ITopic.ATTACHED);


        // 添加子主题
        rootTopic.add(topic1, ITopic.ATTACHED);
        // 添加子主题
        rootTopic.add(topic2, ITopic.ATTACHED);
        // 保存
        workbook.save("test.xmind");
    }
}
