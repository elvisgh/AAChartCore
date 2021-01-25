package com.example.anan.AAChartCore.ChartsDemo.MainContent.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartModel;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AAChartView;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartCreator.AASeriesElement;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartStackingType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAChartEnum.AAChartType;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAOptions;
import com.example.anan.AAChartCore.AAChartCoreLib.AAOptionsModel.AAScrollablePlotArea;
import com.example.anan.AAChartCore.R;

public class RateFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AAOptions aaOptions = customStackedAndGroupedColumnChartTooltip();

        AAChartView aaChartView = getActivity().findViewById(R.id.AAChartView);
        aaChartView.aa_drawChartWithChartOptions(aaOptions);

    }

    private AAOptions customStackedAndGroupedColumnChartTooltip() {
        AAChartModel aaChartModel = new AAChartModel()
                .title("2021年互怼羽毛球群胜率排行榜")
                .subtitle("红色：净胜分（胜+1，败-1）黄色：胜局 蓝色：败局")
                .yAxisTitle("")
                .chartType(AAChartType.Column)
                .legendEnabled(false)//隐藏图例(底部可点按的小圆点)
                .stacking(AAChartStackingType.Normal)
                .scrollablePlotArea(
                        new AAScrollablePlotArea()
                                .minWidth(1000)
                )
                .categories(new String[]{"杰斯","露露","罗乾","云儿"})
                .dataLabelsEnabled(true)
                .series(new AASeriesElement[] {
                                new AASeriesElement()
                                        .name("正负值")
                                        .data(new Object[]{5,3,4,7,2})
                                        .stack("male")
//                                ,
//                                new AASeriesElement()
//                                        .name("Joe")
//                                        .data(new Object[]{3,4,4,2,5,})
//                                        .stack("male")
                                ,
                                new AASeriesElement()
                                        .name("胜局")
                                        .data(new Object[]{2,5,6,2})
                                        .stack("female")
                                ,
                                new AASeriesElement()
                                        .name("败局")
                                        .data(new Object[]{3,0,4,4})
                                        .stack("female")
                                ,
                        }
                );

        /*Custom Tooltip Style --- 自定义图表浮动提示框样式及内容*/
        AAOptions aaOptions = aaChartModel.aa_toAAOptions();
        aaOptions.tooltip
                .shared(false)
                .formatter("function () {\n" +
                        "                return '<b>'\n" +
                        "                + this.x\n" +
                        "                + '</b><br/>'\n" +
                        "                + this.series.name\n" +
                        "                + ': '\n" +
                        "                + this.y\n" +
                        "                + '<br/>'\n" +
                        "                + 'Total: '\n" +
                        "                + this.point.stackTotal;\n" +
                        "     }");

        return aaOptions;
    }
}

