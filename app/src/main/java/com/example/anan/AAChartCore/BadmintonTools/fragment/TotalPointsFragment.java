package com.example.anan.AAChartCore.BadmintonTools.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartAnimationType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAChart;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AADataElement;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAOptions;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAScrollablePlotArea;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AASubtitle;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AATitle;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAXAxis;
import com.example.anan.AAChartCore.AAChartCoreLib.AATools.AAJSStringPurer;
import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
import com.example.anan.AAChartCore.BadmintonTools.data.Game;
import com.example.anan.AAChartCore.BadmintonTools.tool.SharedPreferenceUtil;
import com.example.anan.AAChartCore.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TotalPointsFragment extends Fragment {
    private AAChartView aaChartView1;
    private AAChartModel aaChartModel;
    private AAOptions aaOptions;
    LinkedHashMap<String, Integer> playerScoresMap = new LinkedHashMap<>();
    Object[][] datas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_total_points, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取数据
        List<String> registerdPlayers = SharedPreferenceUtil.getList("players");

        for (String player : registerdPlayers) {
            List<Game> games = DBUtil.GameDBManager.getInstance().getGameRecordByPlayer(player);

            int totalScore = 0;
            for (Game game : games) {
                totalScore += game.getScore_12();
            }
            playerScoresMap.put(player, totalScore);
        }

        // 通过ArrayList构造函数把map.entrySet()转换成list
        List<LinkedHashMap.Entry<String, Integer>> list = new ArrayList<>(playerScoresMap.entrySet());
        // 通过比较器实现比较排序
        Collections.sort(list, new Comparator<LinkedHashMap.Entry<String, Integer>>() {

            @Override
            public int compare(LinkedHashMap.Entry<String, Integer> t1, LinkedHashMap.Entry<String, Integer> t2) {
                return t2.getValue().compareTo(t1.getValue());
            }
        });

        int row = 0;
        int rowCount = list.size();
        datas = new Object[rowCount][];

        for (LinkedHashMap.Entry<String, Integer> set : list) {
            datas[row++] = new Object[]{set.getKey(), set.getValue()};
        }
        row = 0;

        //绘制图表
        aaChartView1 = getActivity().findViewById(R.id.AAChartView1);

        AAChartModel aaChartModel = configureChartModel();
        if (aaOptions == null) {
            aaOptions = aaChartModel.aa_toAAOptions();
        }

        aaChartView1.aa_drawChartWithChartOptions(aaOptions);
    }


    private AAChartModel configureChartModel() {
        AAChartModel aaChartModel = new AAChartModel()
                .chartType(AAChartType.Bar)
                .title("")
                .yAxisTitle("")
                .legendEnabled(false)
                .yAxisGridLineWidth(0f)
                .scrollablePlotArea(
                        new AAScrollablePlotArea()
                                .minWidth(3000)
                                .scrollPositionX(1f)
                )
                .series(new AASeriesElement[]{
                        new AASeriesElement()
                                .name("")
                                .data(configureSeriesDataArray())

                });
        this.aaChartModel = aaChartModel;

        configureColumnChartAndBarChartStyle();
        return aaChartModel;
    }

    private void configureColumnChartAndBarChartStyle() {
        if (aaChartModel.chartType.equals(AAChartType.Bar) ) {
            String pureJSStr = AAJSStringPurer.pureJavaScriptFunctionString(
//                    "Source: <a href=\"https://highcharts.uservoice.com/forums/55896-highcharts-javascript-api\">UserVoice</a>"
                    "数据来源：互怼混双羽毛球群"
            );

            AASeriesElement element = new AASeriesElement()
                    .data(datas);

            AAOptions aaOptions = new AAOptions()
                    .chart(new AAChart()
                            .type(AAChartType.Bar)
                            .scrollablePlotArea(
                                    new AAScrollablePlotArea()
                                            .minHeight(900)
                            ))
                    .title(new AATitle()
                            .text("2021年互怼羽毛球群积分排行榜"))
                    .subtitle(new AASubtitle()
                            .text(pureJSStr))
                    .xAxis(new AAXAxis()
                            .type("category"))
                    .series(new AASeriesElement[]{element});
            this.aaOptions = aaOptions;
        } else {
            aaChartModel
                    .categories(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"})
                    .legendEnabled(true)
                    .colorsTheme(new String[]{"#fe117c","#ffc069","#06caf4","#7dffc0"})
                    .animationType(AAChartAnimationType.EaseOutCubic)
                    .animationDuration(1200);
        }
    }

    private AADataElement[] configureSeriesDataArray() {
        int maxRange = 388;
        AADataElement[] numberArr1 = new AADataElement[maxRange];

        double y1;
        int max = 38, min = 1;
        int random = (int) (Math.random() * (max - min) + min);
        for (int i = 0; i < maxRange; i++) {
            y1 = Math.sin(random * (i * Math.PI / 180)) + i * 2 * 0.01;
            AADataElement aaDataElement = new AADataElement()
                    .y((float) y1);

            numberArr1[i] = aaDataElement;
        }

        return numberArr1;
    }
}
