package com.chqiuu.study.tess4j;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class BizLicenseOcrTests {

    @Test
    void getBizLicenseInfo() throws Exception {
        BizLicenseOcr bizLicenseOcr = new BizLicenseOcr();
        BizLicenseInfo bizLicenseInfo = bizLicenseOcr.getInfo("D:\\data\\ocr\\BizLicense\\2.jpg");
        System.out.println(bizLicenseInfo);
    }

    @Test
    void ocr() throws Exception {
        Tesseract tesseract = new Tesseract();
        // 指定训练数据集合的路径
        tesseract.setDatapath("D:\\PrivateProperty\\tessdata");tesseract.setLanguage("chi_sim");
        // 指定识别图片
        File imgDir = new File("D:\\data\\ocr\\BizLicense\\2.jpg");
        long startTime = System.currentTimeMillis();
        String ocrResult = tesseract.doOCR(imgDir);
        // 输出识别结果
        System.out.println("OCR Result: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
