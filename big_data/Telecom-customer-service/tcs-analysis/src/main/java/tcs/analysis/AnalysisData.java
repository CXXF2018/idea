package tcs.analysis;

import org.apache.hadoop.util.ToolRunner;
import tcs.analysis.tool.AnalysisTextTool;

//分析数据
public class AnalysisData {
    public static void main(String[] args) throws Exception {

        int result = ToolRunner.run(new AnalysisTextTool(), args);
    }
}
