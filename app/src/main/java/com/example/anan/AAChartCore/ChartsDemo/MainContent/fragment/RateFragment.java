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
                .title("Total fruit consumption, grouped by gender")
                .subtitle("stacked and grouped")
                .yAxisTitle("Number of fruits")
                .chartType(AAChartType.Column)
                .legendEnabled(false)//隐藏图例(底部可点按的小圆点)
                .stacking(AAChartStackingType.Normal)
                .scrollablePlotArea(
                        new AAScrollablePlotArea()
                                .minWidth(1000)
                )
                .categories(new String[]{"Apples", "Oranges", "Pears","Grapes","Bananas", "Apples", "Oranges", "Pears","Grapes","Bananas"
                        ,"Apples", "Oranges", "Pears","Grapes","Bananas", "Apples", "Oranges", "Pears","Grapes","Bananas"
                        ,"Apples", "Oranges", "Pears","Grapes","Bananas", "Apples", "Oranges", "Pears","Grapes","Bananas"
                        ,"Apples", "Oranges", "Pears","Grapes","Bananas", "Apples", "Oranges", "Pears","Grapes","Bananas"})
                .dataLabelsEnabled(true)
                .series(new AASeriesElement[] {
                                new AASeriesElement()
                                        .name("John")
                                        .data(new Object[]{5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2})
                                        .stack("male")
//                                ,
//                                new AASeriesElement()
//                                        .name("Joe")
//                                        .data(new Object[]{3,4,4,2,5,})
//                                        .stack("male")
                                ,
                                new AASeriesElement()
                                        .name("Jane")
                                        .data(new Object[]{2,5,6,2,1,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2})
                                        .stack("female")
                                ,
                                new AASeriesElement()
                                        .name("Janet")
                                        .data(new Object[]{3,0,4, 4,3,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2,5,3,4,7,2})
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

