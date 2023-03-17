package com.ean.usercenter.once;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

/**
 * @description:TODO
 * @author:Povlean
 */
@Slf4j
public class TableListener implements ReadListener<XingQiuTableUserInfo> {

    @Override
    public void invoke(XingQiuTableUserInfo data, AnalysisContext analysisContext) {
        System.out.println(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("已解析完成");
    }

}
